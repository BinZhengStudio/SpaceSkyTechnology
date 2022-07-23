package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.network.client.*;
import cn.bzgzs.spaceplane.network.server.PlaneRotateSyncPacket;
import cn.bzgzs.spaceplane.sounds.SoundEventList;
import cn.bzgzs.spaceplane.util.VecHelper;
import cn.bzgzs.spaceplane.world.item.ItemList;
import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.BlockUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BasePlaneEntity extends Entity {
	private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SPEED_UP = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TRACTOR = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ENGINE_POWER = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.INT);
	public static final int TOTAL_FIRE_TIME = 5;
	private float zRot;
	public float zRotO;
	private float deltaPitch;
	private float deltaYaw;
	private float deltaRoll;
	private int lerpPosSteps;
	private int lerpRotateSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpPitch;
	private double lerpYaw;
	private double lerpRoll;
	private boolean inputEngineOnActivation;
	private boolean inputLandingGearActivation;
	private boolean inputLookUp;
	private boolean inputLookDown;
	private boolean inputLeftRoll;
	private boolean inputRightRoll;
	private boolean inputClimbUp;
	private boolean inputDecline;
	private boolean inputLeft;
	private boolean inputRight;
	private boolean inputSpeedUp;
	private boolean inputLaunchMissile;
	private boolean inputInterceptorMissile;
	private boolean inputLaunchCannonball;
	private int fireTimeLeft;
	private int fireTimeRight;
	private int inWaterParts;
	private Status status;
	private Status oldStatus;
	protected List<PlanePart> parts = new ArrayList<>();

	public BasePlaneEntity(EntityType<?> type, Level world) {
		super(type, world);
		this.defineParts();
		this.noPhysics = true;
	}

//	public BasePlaneEntity(Level world, double x, double y, double z) {
//		this(EntityTypeList.TEST.get(), world);
//		this.setPos(x, y, z);
//		this.xo = x;
//		this.yo = y;
//		this.zo = z;
//	}

	protected abstract double getMaxAccel();

	public abstract void defineParts();

	protected abstract double getHorizontalRidingOffset();

	@Override
	public abstract double getPassengersRidingOffset();

	protected abstract double getMaxDeltaRotate();

	protected abstract double getGroundLiftFactor();

	/**
	 * 重力加速度，是正数
	 * @return 重力加速度的绝对值
	 */
	protected abstract double getGravity();

	protected abstract double getStandRes();

	protected abstract double getLieRes();

	protected abstract double getLieRotateRes();

	protected abstract double getTakeOffSpeed();

	protected abstract double getAirLiftFactor();

	/**
	 * 空气阻力因数，是正数
	 * @return 空气阻力因数的绝对值
	 */
	protected abstract double getAirResFactor();

	protected abstract double getAirRotateRes();

	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions size) {
		return size.height / 2.0F;
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ENGINE_ON, false);
		this.entityData.define(SPEED_UP, false);
		this.entityData.define(LEFT, false);
		this.entityData.define(RIGHT, false);
		this.entityData.define(LANDING_GEAR, true);
		this.entityData.define(TRACTOR, false);
		this.entityData.define(FUEL, 0); // 燃油量，单位mB
		this.entityData.define(ENGINE_POWER, 0);
	}

	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		PartEntity<?>[] partEntities = new PartEntity[0];
		return this.parts.toArray(partEntities);
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected Vec3 getRelativePortalPosition(Direction.Axis pAxis, BlockUtil.FoundRectangle pPortal) {
		return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(pAxis, pPortal));
	}


	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	public boolean hurt(PlanePart part, DamageSource source, float amount) { // TODO
		return false;
	}

	@Override
	public void push(Entity entity) { // TODO
		if (entity instanceof BasePlaneEntity) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.push(entity);
			}
		} else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.push(entity);
		}
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpPosSteps = 10;
	}

	public void lerpRotate(float pitch, float yaw, float roll) {
		this.lerpPitch = pitch;
		this.lerpYaw = yaw;
		this.lerpRoll = roll;
		this.lerpRotateSteps = 10;
	}

	@Override
	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	@Override
	public void tick() {
		this.oldStatus = this.status;
		this.status = this.getStatus();

		super.tick();
		this.tickLerpPos();
		this.tickLerpRotate();
		if (this.isControlledByLocalInstance()) {
			switch (this.status) {
				case STAND_ON_GROUND -> {
					if (this.getTractor()) {
						this.controlTractor();
					} else {
						this.liftStandOnGround();
						this.resistanceStandOnGround();
					}
				}
				case LIE_ON_GROUND -> {
					this.liftStandOnGround();
					this.resistanceLieOnGround();
				}
				case ABOVE_WATER -> {
					this.liftStandOnGround();
					this.resistanceAboveWater();
				}
				case IN_AIR -> {
					this.flyInAir();
					this.controlInAir();
					this.inAirResistance();
				}
				case UNDER_WATER -> {
					this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
					this.setEngineState(false);
					if (this.level.isClientSide) {
						NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePacket(this.getEngineState()));
					}
				}
				default -> this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -this.getGravity(), 0.0D));
			}
			if (this.level.isClientSide) {
				if (this.inputLaunchCannonball) {
					NetworkHandler.INSTANCE.sendToServer(new PlaneLaunchCannonballPacket(true, this));
					NetworkHandler.INSTANCE.sendToServer(new PlaneLaunchCannonballPacket(false, this));
				}
				if (this.getEngineState()) {
					this.setDeltaMovement(this.getDeltaMovement().add(new Vec3d(0.0D, 0.0D, this.getMaxAccel() * this.getEnginePower() / 100.0D).xRot(this.getXRotRad()).yRot(this.getYRotRad())));
				}
			}
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}

		if (this.level.isClientSide) {
			if (this.getEngineState() && this.inputSpeedUp) {
				if (this.getEnginePower() < 100) {
					this.setEnginePower(Math.min(100, this.getEnginePower() + 2));
					NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePowerPacket(this));
				}
			} else {
				if (this.getEnginePower() > 0) {
					this.setEnginePower(Math.max(this.getEnginePower() - 5, 0));
					NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePowerPacket(this));
				}
			}
			NetworkHandler.INSTANCE.sendToServer(new PlaneRotateChangedPacket(this));
		}

		if (!this.level.isClientSide) {
			if (this.fireTimeLeft > 0) {
				--this.fireTimeLeft;
			}
			if (this.fireTimeRight > 0) {
				--this.fireTimeRight;
			}
//			this.playSound(SoundEventList.PLANE_ENGINE.get(), 1.0F, 1.0F); TODO 声音未完善
			List<ServerPlayer> players = ((ServerLevel) this.level).getPlayers((predicate) -> true);
			players.forEach((player) -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PlaneRotateSyncPacket(this)));
		}

		this.parts.forEach(part -> part.updatePos(true)); // TODO collision

//		this.checkInsideBlocks();
		// 推动实体。TODO 以后要重写
		List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (!entity.hasPassenger(this)) {
					this.push(entity);
				}
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.zRotO = this.getZRot();
	}

	private void controlTractor() {
		if (this.level.isClientSide) {
			if (this.inputSpeedUp) {
				this.move(MoverType.SELF, new Vec3d(0.0D, 0.0D, 0.35D).yRot(this.getYRotRad()));
			}
			if (this.inputLeft) {
				this.setYRot(this.getYRot() - 2);
			}
			if (this.inputRight) {
				this.setYRot(this.getYRot() + 2);
			}
		}
	}

	private void liftStandOnGround() {
		double lift = this.getGroundLiftFactor() * this.getDeltaMovement().horizontalDistanceSqr();
		if (this.inputClimbUp) {
			lift *= 2;
		} else if (this.inputDecline) {
			lift *= -2;
		}
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, lift - this.getGravity(), 0.0D));
	}

	private void resistanceStandOnGround() {
		this.deltaPitch *= this.getLieRotateRes();
		this.deltaYaw *= this.getLieRotateRes();
		this.deltaRoll *= this.getLieRotateRes();
		Vec3d res = new Vec3d(0.0D, 0.0D, this.inputDecline ? 1.1D * this.getStandRes() : this.getStandRes()).yRot(this.getYRotRad());
		Vec3d airLift = new Vec3d(0.0D, 0.0D, -this.getAirResFactor() * this.getDeltaMovement().horizontalDistanceSqr()).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res.add(airLift))));
	}

	private void resistanceLieOnGround() {
		this.deltaPitch *= this.getLieRotateRes();
		this.deltaYaw *= this.getLieRotateRes();
		this.deltaRoll *= this.getLieRotateRes();
		Vec3d res = new Vec3d(0.0D, 0.0D, this.inputDecline ? 1.1D * this.getLieRes() : this.getLieRes()).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res)));
	}

	private void resistanceAboveWater() { // TODO 实际数值根据进水体积决定
		Vec3d airRes = new Vec3d(0.0D, 0.0D, -this.getAirResFactor() * this.getDeltaMovement().horizontalDistanceSqr()).xRot(this.getXRotRad()).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
		this.deltaPitch *= 0.9D;
		this.deltaYaw *= 0.9D;
		this.deltaRoll *= 0.9D;
	}

	private void tickLerpPos() {
		if (this.isControlledByLocalInstance()) {
			this.lerpPosSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpPosSteps > 0) {
			double x = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpPosSteps;
			double y = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpPosSteps;
			double z = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpPosSteps;
			--this.lerpPosSteps;
			this.setPos(x, y, z);
		}
	}

	private void tickLerpRotate() {
		if (this.isControlledByLocalInstance()) {
			this.lerpRotateSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpRotateSteps > 0) {
			double pitch = Mth.wrapDegrees(this.lerpPitch - (double) this.getXRot());
			double yaw = Mth.wrapDegrees(this.lerpYaw - (double) this.getYRot());
			double roll = Mth.wrapDegrees(this.lerpRoll - (double) this.getZRot());
			this.setXRot(this.getXRot() + (float) pitch / (float) this.lerpRotateSteps);
			this.setYRot(this.getYRot() + (float) yaw / (float) this.lerpRotateSteps);
			this.setZRot(this.getZRot() + (float) roll / (float) this.lerpRotateSteps);
			--this.lerpRotateSteps;
			this.setRot(this.getXRot(), this.getYRot(), this.getZRot());
		}
	}

	private void flyInAir() {
		Vec3 motion = this.getDeltaMovement();
		if (motion.y > -0.5D) {
			this.fallDistance = 1.0F;
		}

		Vec3 lookAngle = this.getLookAngle(); // 实体朝向的向量
		double lookDistance = lookAngle.horizontalDistance(); // 实体朝向水平长度
		double motionDistance = motion.horizontalDistance(); // 实体运动水平速度
		double cosXRot = Math.cos(-this.getXRotRad());
		double cosXRotSqr = cosXRot * cosXRot;
		double gravity = 0.08D * (-1.0D + cosXRotSqr * 0.75D);
		motion = this.getDeltaMovement().add(0.0D, gravity, 0.0D); // 下落处理
		if (motion.y < 0.0D && lookDistance > 0.0D) { // 下落时的水平方向加速
			double d6 = motion.y * -0.1D * cosXRotSqr;
			motion = motion.add(lookAngle.x * d6 / lookDistance, d6, lookAngle.z * d6 / lookDistance);
		}

		if (-this.getXRotRad() < 0.0F && lookDistance > 0.0D) { // 头朝斜上方的水平方向减速和斜向上加速
			double d10 = motionDistance * -Math.sin(-this.getXRotRad()) * 0.04D;
			motion = motion.add(-lookAngle.x * d10 / lookDistance, d10 * 3.2D, -lookAngle.z * d10 / lookDistance);
		}

		if (lookDistance > 0.0D) { // ?
			motion = motion.add((lookAngle.x / lookDistance * motionDistance - motion.x) * 0.1D, 0.0D, (lookAngle.z / lookDistance * motionDistance - motion.z) * 0.1D);
		}

		double lift = this.getLookSpeed() >= this.getTakeOffSpeed() ? 0.02D : this.getAirLiftFactor() * this.getLookSpeedSqr();
		if (this.inputClimbUp) {
			lift *= 2;
		} else if (this.inputDecline) {
			lift *= -2;
		}
		Vec3d liftVec = new Vec3d(0.0D, lift, 0.0D).zRot(-this.getZRotRad()).xRot(this.getXRotRad()).yRot(this.getYRotRad());
		motion = motion.add(liftVec);

		this.setRot(this.getXRot() + this.deltaPitch, this.getYRot() + this.deltaYaw, this.getZRot() + this.deltaRoll);
		this.setDeltaMovement(motion);
	}

	private void controlInAir() {
		if (this.level.isClientSide) {
			double speedRatio = this.getLookSpeed() / 20.0D; // 当前速度与最快速度的比值
			double deltaRotate = speedRatio * this.getMaxDeltaRotate();
			if (this.inputLookUp) {
				this.deltaPitch -= deltaRotate * Math.cos(this.getZRotRad()); // MC中，仰视则pitch为负，故用减法
				this.deltaYaw += deltaRotate * Math.sin(this.getZRotRad());
			}
			if (this.inputLookDown) {
				this.deltaPitch += deltaRotate * Math.cos(this.getZRotRad());
				this.deltaYaw -= deltaRotate * Math.sin(this.getZRotRad());
			}
			if (this.inputLeftRoll) {
				this.deltaYaw += deltaRotate * Math.sin(this.getXRotRad());
				this.deltaRoll -= deltaRotate * Math.cos(this.getXRotRad());
			}
			if (this.inputRightRoll) {
				this.deltaYaw -= deltaRotate * Math.sin(this.getXRotRad());
				this.deltaRoll += deltaRotate * Math.cos(this.getXRotRad());
			}
			if (this.inputLeft) {
				this.deltaPitch -= deltaRotate * Math.cos(this.getXRotRad()) * Math.sin(this.getZRotRad());
				this.deltaYaw -= deltaRotate * Math.cos(this.getXRotRad()) * Math.cos(this.getZRotRad());
				this.deltaRoll -= deltaRotate * Math.sin(this.getXRotRad());
			}
			if (this.inputRight) {
				this.deltaPitch += deltaRotate * Math.cos(this.getXRotRad()) * Math.sin(this.getZRotRad());
				this.deltaYaw += deltaRotate * Math.cos(this.getXRotRad()) * Math.cos(this.getZRotRad());
				this.deltaRoll += deltaRotate * Math.sin(this.getXRotRad());
			}
		}
	}

	private void inAirResistance() {
		this.deltaPitch *= this.getAirRotateRes();
		this.deltaYaw *= this.getAirRotateRes();
		this.deltaRoll *= this.getAirRotateRes();

		Vec3d res;
		if (this.getDeltaMovement().dot(this.getLookAngle()) >= 0) {
			res = new Vec3d(0.0D, 0.0D, this.getAirResFactor() * this.getLookSpeedSqr()).xRot(this.getXRotRad()).yRot(this.getYRotRad());
		} else {
			res = new Vec3d(0.0D, 0.0D, -this.getAirResFactor() * this.getLookSpeedSqr()).xRot(this.getXRotRad()).yRot(this.getYRotRad());
		}
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res)));
	}

	public void launchCannonBall(boolean left, Vec3 initialSpeed, float pitch, float yaw, float roll) {
//		if (this.cannonball > 0)
		if (left && this.fireTimeLeft <= 0) {
			CannonballEntity cannonball = new CannonballEntity(this.level, this, -28, -13, 0, initialSpeed, pitch, yaw, roll);
			this.level.addFreshEntity(cannonball);
			this.fireTimeLeft = TOTAL_FIRE_TIME;
		}
		if (!left && this.fireTimeRight <= 0) {
			CannonballEntity cannonball = new CannonballEntity(this.level, this, 28, -13, 0, initialSpeed, pitch, yaw, roll);
			this.level.addFreshEntity(cannonball);
			this.fireTimeRight = TOTAL_FIRE_TIME;
		}
//		this.cannonball--;
	}

	protected Vec3d getCenterPos() {
		return new Vec3d(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());
	}

	public void setInputEngineOnActivation(boolean activation) {
		if (!activation) this.changeEngineState();
		this.inputEngineOnActivation = activation;
	}

	@Override
	public void move(MoverType type, Vec3 motion) { // TODO
		this.level.getProfiler().push("move");
		Vec3 collideVec = this.collide(motion);
		double d0 = collideVec.lengthSqr();
		if (d0 > 1.0E-7D) {
			this.setPos(this.getX() + collideVec.x, this.getY() + collideVec.y, this.getZ() + collideVec.z);
		}
		this.level.getProfiler().pop();

		this.level.getProfiler().push("resetMotion");
		boolean xCollide = !Mth.equal(motion.x, collideVec.x);
		boolean zCollide = !Mth.equal(motion.z, collideVec.z);
		this.horizontalCollision = xCollide || zCollide;
		this.verticalCollision = motion.y != collideVec.y;
		this.verticalCollisionBelow = this.verticalCollision && motion.y < 0.0D;
		if (this.isRemoved()) {
			this.level.getProfiler().pop();
		} else {
			Vec3 vec31 = this.getDeltaMovement();
			if (this.horizontalCollision || this.verticalCollision) {
				this.setDeltaMovement(xCollide ? 0.0D : vec31.x, this.verticalCollision ? 0.0D : vec31.y, zCollide ? 0.0D : vec31.z);
			}

//			this.tryCheckInsideBlocks();
//			float f2 = this.getBlockSpeedFactor();
//			this.setDeltaMovement(this.getDeltaMovement().multiply(f2, 1.0D, f2));
		}
		this.level.getProfiler().pop();
	}

	private Vec3 collide(Vec3 motion) {
		Vec3 vec3 = motion;
		List<PlanePart> yCollideParts = new ArrayList<>();
		for (PlanePart part : this.parts) {
			Vec3 collide = part.collide(motion, yCollideParts);
			if (vec3.lengthSqr() > collide.lengthSqr()) {
				vec3 = collide;
			}
		}
		for (PlanePart yCollidePart : yCollideParts) {
			if (yCollidePart.isWheel()) { // TODO 等待测试
				if (Math.abs(this.getXRot()) < 30.0F) this.deltaPitch -= this.getXRot();
				if (Math.abs(this.getZRot()) < 30.0F || Math.abs(this.getZRot()) > 150.0F)
					this.deltaRoll -= this.getZRot();
			}
		}
		return vec3;
	}

	protected void explode() {
//		this.level.explode(null, this.getX(), this.getY(), this.getZ(), this.entityData.get(FUEL), flag, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
//		this.dropItems();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 5.0F, true, Explosion.BlockInteraction.DESTROY);
		this.discard();
	}

	public void changeEngineState() {
		if (this.inputEngineOnActivation) {
			if (this.getEngineState()) {
				this.setEngineState(false);
			} else {
//				if (this.entityData.get(FUEL) > 0) { TODO
				if (!this.getTractor()) this.setEngineState(true);
//				}
			}
			if (this.level.isClientSide) {
				NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePacket(this.getEngineState()));
			}
		}
	}

	public boolean getEngineState() {
		return this.entityData.get(ENGINE_ON);
	}

	public void setEngineState(boolean state) {
		this.entityData.set(ENGINE_ON, state);
	}

	public void setInputLandingGearActivation(boolean activation) {
		if (!activation) this.changeLandingGear();
		this.inputLandingGearActivation = activation;
	}

	public void changeLandingGear() {
		if (this.inputLandingGearActivation) {
			this.entityData.set(LANDING_GEAR, !this.getLandingGear());
			if (this.level.isClientSide) {
				NetworkHandler.INSTANCE.sendToServer(new PlaneLandingGearPacket(this.getLandingGear()));
			}
		}
	}

	public boolean getLandingGear() {
		return this.entityData.get(LANDING_GEAR);
	}

	public void setLandingGear(boolean state) {
		this.entityData.set(LANDING_GEAR, state);
	}

	public boolean getTractor() {
		return this.entityData.get(TRACTOR);
	}

	public void setTractor(boolean state) {
		if (this.level.isClientSide) {
			NetworkHandler.INSTANCE.sendToServer(new PlaneTractorPacket(state));
		} else {
			if (!state && this.getTractor() && this.getControllingPassenger() instanceof Player player) {
				player.addItem(new ItemStack(ItemList.TRACTOR.get()));
			}
		}
		this.entityData.set(TRACTOR, state);
	}

	public int getEnginePower() {
		return this.entityData.get(ENGINE_POWER);
	}

	public void setEnginePower(int power) {
		this.entityData.set(ENGINE_POWER, power);
	}

	public double getLookSpeedSqr() {
		return VecHelper.projectionLengthSqr(this.getDeltaMovement(), this.getLookAngle());
	}

	public double getLookSpeed() {
		return VecHelper.projectionLength(this.getDeltaMovement(), this.getLookAngle());
	}

	private Status getStatus() {
		if (this.checkUnderWater()) {
			return Status.UNDER_WATER;
		} else if (this.isAboveWater()) {
			return Status.ABOVE_WATER;
		} else {
			return Objects.requireNonNullElse(this.onGroundStatus(), Status.IN_AIR);
		}
	}

	private Status onGroundStatus() {
		int onLandWheel = 0;
		int onLandPart = 0;
		for (PlanePart part : this.parts) {
			if (part.isOnLand()) {
				onLandPart++;
				if (part.isWheel()) onLandWheel++;
			}
		}
		if (onLandWheel >= 3) return Status.STAND_ON_GROUND;
		if (onLandPart >= 3) return Status.LIE_ON_GROUND;
		return null;
	}

	private boolean isAABBOnLand() { // TODO
		AABB aabb = this.getBoundingBox();
		AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001D, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
		int i = Mth.floor(aabb1.minX) - 1;
		int j = Mth.ceil(aabb1.maxX) + 1;
		int k = Mth.floor(aabb1.minY) - 1;
		int l = Mth.ceil(aabb1.maxY) + 1;
		int i1 = Mth.floor(aabb1.minZ) - 1;
		int j1 = Mth.ceil(aabb1.maxZ) + 1;
		VoxelShape voxelshape = Shapes.create(aabb1);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int l1 = i; l1 < j; ++l1) {
			for (int i2 = i1; i2 < j1; ++i2) {
				int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
				if (j2 != 2) {
					for (int k2 = k; k2 < l; ++k2) {
						if (j2 <= 0 || k2 != k && k2 != l - 1) {
							pos.set(l1, k2, i2);
							BlockState blockstate = this.level.getBlockState(pos);
							if (!blockstate.isAir() && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level, pos).move(l1, k2, i2), voxelshape, BooleanOp.AND)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isAboveWater() {
		this.inWaterParts = 0;
		for (PlanePart part : this.parts) {
			if (!part.isLandingGear()) {
				if (part.checkUnderWater()) {
					this.inWaterParts++;
				}
			}
		}
		return this.inWaterParts > 0 && this.inWaterParts < this.parts.size() - 6; // -6是为了减去起落架的部分
	}

	private boolean isAABBAboveWater() { // TODO
		AABB aabb = this.getBoundingBox();
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.ceil(aabb.minY + 0.001D);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					pos.set(k1, l1, i2);
					FluidState fluidstate = this.level.getFluidState(pos);
					if (fluidstate.is(FluidTags.WATER)) {
						float f = (float) l1 + fluidstate.getHeight(this.level, pos);
						flag |= aabb.minY < (double) f;
					}
				}
			}
		}
		return flag;
	}

	private boolean checkUnderWater() {
		boolean flag;
		for (PlanePart part : this.parts) {
			if (!part.isLandingGear()) {
				flag = part.checkUnderWater();
				if (!flag) return false;
			}
		}
		return true;
	}

	private boolean checkAABBUnderWater() { // TODO
		AABB aabb = this.getBoundingBox();
		double d0 = aabb.maxY + 0.001D;
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.maxY);
		int l = Mth.ceil(d0);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					pos.set(k1, l1, i2);
					FluidState fluidstate = this.level.getFluidState(pos);
					if (fluidstate.is(FluidTags.WATER) && d0 < (double) ((float) pos.getY() + fluidstate.getHeight(this.level, pos))) {
						if (!fluidstate.isSource()) {
							return true;
						}
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	// TODO 临时代码
	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			passenger.setPos(this.calculateRiderPosition());
			passenger.setYRot(passenger.getYRot() + this.deltaYaw);
			passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaYaw);
			this.clampRotation(passenger);
		}
	}

	private Vec3 calculateRiderPosition() {
		return new Vec3(this.getX(), this.getY(), this.getZ()).add(this.calculateRiderOffset());
	}

	private Vec3 calculateRiderOffset() {
		return new Vec3(0.0D, this.getEyeHeight(), 0.0D)
				.add(new Vec3d(0.0D, 0.0D, this.getHorizontalRidingOffset()).xRot(this.getXRotRad()).yRot(this.getYRotRad()))
				.add(new Vec3d(0.0D, -1.62D, 0.0D));
	}

	protected void clampRotation(Entity entityToUpdate) {
		entityToUpdate.setYBodyRot(this.getYRot());
		float yaw = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
		float yaw1 = Mth.clamp(yaw, -105.0F, 105.0F);
		entityToUpdate.yRotO += yaw1 - yaw;
		entityToUpdate.setYRot(entityToUpdate.getYRot() + yaw1 - yaw);
		entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 *= 64.0D;
		return distance < d0 * d0;
	}

	@Override
	public void onPassengerTurned(Entity entityToUpdate) {
		this.clampRotation(entityToUpdate);
	}

	public enum Status {
		ABOVE_WATER, // 以后写水路两栖飞机可能用到
		STAND_ON_GROUND,
		LIE_ON_GROUND,
		IN_AIR,
		UNDER_WATER
	}

	public enum Part {
		HEAD,
		BODY,
		ENGINE,
		WINDSHIELD,
		LEFT_WING,
		LEFT_FRONT_WING,
		RIGHT_WING,
		RIGHT_FRONT_WING,
		LEFT_RUDDER,
		RIGHT_RUDDER,
		LEFT_LANDING_GEAR,
		RIGHT_LANDING_GEAR,
		FRONT_LANDING_GEAR,
		LEFT_LANDING_GEAR_WHEEL,
		RIGHT_LANDING_GEAR_WHEEL,
		FRONT_LANDING_GEAR_WHEEL,
	}

	public double getXRotRad() {
		return Math.toRadians(-this.getXRot());
	}

	public double getYRotRad() {
		return -Math.toRadians(this.getYRot());
	}

	public float getZRot() {
		return this.zRot;
	}

	public double getZRotRad() {
		return Math.toRadians(this.zRot);
	}

	public void setZRot(float zRot) {
		if (!Float.isFinite(zRot)) {
			Util.logAndPauseIfInIde("Invalid entity rotation: " + zRot + ", discarding.");
		} else {
			this.zRot = zRot;
		}
	}

	public void setRot(float pitch, float yaw, float roll) {
		boolean flag = false;
		if (pitch < -90) {
			flag = true;
			pitch = -pitch - 180.0F;
		}
		if (pitch > 90) {
			flag = true;
			pitch = -pitch + 180.0F;
		}
		if (flag) {
			yaw += 180.0F;
			roll += 180.0F;
			this.deltaPitch = -this.deltaPitch;
			if (this.getControllingPassenger() instanceof Player player) {
				player.setYBodyRot(Mth.wrapDegrees(this.getYRot() + 180.0F));
				player.setYRot(Mth.wrapDegrees(player.getYRot() + 180.0F));
				player.setYHeadRot(player.getYRot());
			}
			this.xRotO = Mth.wrapDegrees(pitch);
			this.yRotO = Mth.wrapDegrees(yaw);
			this.zRotO = Mth.wrapDegrees(roll);
		}

		this.setXRot(Mth.wrapDegrees(pitch));
		this.setYRot(Mth.wrapDegrees(yaw));
		this.setZRot(Mth.wrapDegrees(roll));
	}

	@Override
	public void setRot(float yaw, float pitch) {
		this.setRot(pitch, yaw, this.getZRot());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("EngineState")) this.setEngineState(tag.getBoolean("EngineState"));
		if (tag.contains("LandingGear")) this.setLandingGear(tag.getBoolean("LandingGear"));
		if (tag.contains("Tractor")) this.setTractor(tag.getBoolean("Tractor"));
		if (tag.contains("EnginePower")) this.setEnginePower(tag.getInt("EnginePower"));
		if (tag.contains("ZRot")) this.setZRot(tag.getFloat("ZRot"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putBoolean("EngineState", this.getEngineState());
		tag.putBoolean("LandingGear", this.getLandingGear());
		tag.putBoolean("Tractor", this.getTractor());
		tag.putInt("EnginePower", this.getEnginePower());
		tag.putFloat("ZRot", this.getZRot());
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (player.getItemInHand(hand).is(ItemList.TRACTOR.get()) && this.status == Status.STAND_ON_GROUND && !this.getTractor() && !this.getEngineState()) {
			if (this.getControllingPassenger() == null && !this.level.isClientSide) {
				this.setTractor(true);
				if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
				return InteractionResult.CONSUME;
			}
			return InteractionResult.PASS;
		} else {
			if (!this.level.isClientSide) {
				return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
			} else {
				return InteractionResult.SUCCESS;
			}
		}
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().isEmpty() && passenger instanceof Player;
	}

	@Override
	public Entity getControllingPassenger() {
		return this.getFirstPassenger();
	}

	public void setClientInput(boolean lookUp, boolean lookDown, boolean leftRoll, boolean rightRoll,
							   boolean climbUp, boolean decline, boolean left, boolean right, boolean speedUp,
							   boolean launchMissile, boolean interceptorMissile, boolean launchCannonball) {
		this.inputLookUp = lookUp;
		this.inputLookDown = lookDown;
		this.inputLeftRoll = leftRoll;
		this.inputRightRoll = rightRoll;
		this.inputClimbUp = climbUp;
		this.inputDecline = decline;
		this.inputLeft = left;
		this.inputRight = right;
		this.inputSpeedUp = speedUp;
		this.inputLaunchMissile = launchMissile;
		this.inputInterceptorMissile = interceptorMissile;
		this.inputLaunchCannonball = launchCannonball;
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.network.client.PlaneEnginePacket;
import cn.bzgzs.spaceplane.network.client.PlaneEnginePowerPacket;
import cn.bzgzs.spaceplane.network.client.PlaneLandingGearPacket;
import cn.bzgzs.spaceplane.network.client.PlaneTractorPacket;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class TestPlaneEntity extends Entity {
	private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SPEED_UP = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TRACTOR = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ENGINE_POWER = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.INT);
	public static final int LANDING_GEAR_HEIGHT = 2;
	private float outOfControlTicks;
	private float zRot;
	private float deltaPitch;
	private float deltaYaw;
	private float deltaRoll;
	private int lerpSteps;
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
	private TestPlaneEntity.Status status;
	private TestPlaneEntity.Status oldStatus;

	public TestPlaneEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

//	public TestPlaneEntity(Level world, double x, double y, double z) {
//		this(EntityTypeList.TEST.get(), world);
//		this.setPos(x, y, z);
//		this.xo = x;
//		this.yo = y;
//		this.zo = z;
//	}

	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions size) {
		return 3.5F;
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

	@Override
	public boolean canCollideWith(Entity entity) {
		return canVehicleCollide(this, entity);
	}

	public static boolean canVehicleCollide(Entity entity, Entity vehicle) {
		return (vehicle.canBeCollidedWith() || vehicle.isPushable()) && !entity.isPassengerOfSameVehicle(vehicle);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected Vec3 getRelativePortalPosition(Direction.Axis pAxis, BlockUtil.FoundRectangle pPortal) {
		return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(pAxis, pPortal));
	}

	protected double getHorizontalRidingOffset() {
		return 2.0D;
	}

	@Override
	public double getPassengersRidingOffset() {
		return -0.81D;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void push(Entity entity) {
		if (entity instanceof TestPlaneEntity) {
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
		this.lerpYaw = yaw;
		this.lerpPitch = pitch;
		this.lerpSteps = 10;
	}

	@Override
	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	@Override
	public void tick() {
		this.oldStatus = this.status;
		this.status = this.getStatus();
		if (this.status != Status.UNDER_WATER) {
			this.outOfControlTicks = 0.0F;
		} else {
			++this.outOfControlTicks;
		}

		if (!this.level.isClientSide && this.outOfControlTicks >= 60.0F) {
			this.ejectPassengers();
		}

		super.tick();
		this.tickLerp();
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
				default -> this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
			}
			if (this.level.isClientSide && this.getEngineState()) {
				this.setDeltaMovement(this.getDeltaMovement().add(new Vec3d(0.0D, 0.0D, this.getEnginePower() / 100.0D).xRot(this.getXRotRad()).yRot(this.getYRotRad()).zRot(this.getZRotRad())));
			}
			this.move(MoverType.SELF, this.getDeltaMovement()); // TODO 需要覆写
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}

		if (this.level.isClientSide) {
			if (this.getEngineState() && this.inputSpeedUp) {
				if (this.getEnginePower() < 100) {
					this.setEnginePower(Math.min(100, this.getEnginePower() + 2));
					NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePowerPacket(this.getEnginePower()));
				}
			} else {
				if (this.getEnginePower() > 0) {
					this.setEnginePower(Math.max(this.getEnginePower() - 5, 0));
					NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePowerPacket(this.getEnginePower()));
				}
			}
			// TODO
//			NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(this.getSpeedUp(), this.getLeft(), this.getRight()));
		}

		this.checkInsideBlocks(); // TODO 以后要覆写的
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
		float gravity = 0.04F;
		double lift = 0.017777777777777778D * this.getDeltaMovement().horizontalDistanceSqr();
		if (this.inputClimbUp) {
			lift *= 2;
		} else if (this.inputDecline) {
			lift *= -2;
		}
		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, lift - gravity, 0.0D));
	}

	private void resistanceStandOnGround() {
		this.deltaPitch *= 0.9F;
		this.deltaYaw *= 0.9F;
		this.deltaRoll *= 0.9F;
		Vec3d res = new Vec3d(0.0D, 0.0D, this.inputDecline ? 0.23D : 0.2D).yRot(this.getYRotRad());
		Vec3d airLift = new Vec3d(0.0D, 0.0D, -0.04D * this.getDeltaMovement().horizontalDistanceSqr()).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res.add(airLift))));
	}

	private void resistanceLieOnGround() {
		this.deltaPitch *= 0.6F;
		this.deltaYaw *= 0.6F;
		this.deltaRoll *= 0.6F;
		Vec3d res = new Vec3d(0.0D, 0.0D, this.inputDecline ? 1.1D : 1.0D).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res)));
	}

	private void resistanceAboveWater() { // TODO 实际数值根据进水体积决定
		Vec3d airRes = new Vec3d(0.0D, 0.0D, -0.04D * this.getDeltaMovement().horizontalDistanceSqr()).yRot(this.getYRotRad());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
		this.deltaPitch *= 0.9D;
		this.deltaYaw *= 0.9D;
		this.deltaRoll *= 0.9D;
	}

	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
			double d3 = Mth.wrapDegrees(this.lerpYaw - (double) this.getYRot());
			this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
			this.setXRot(this.getXRot() + (float) (this.lerpPitch - (double) this.getXRot()) / (float) this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d1, d2);
			this.setRot(this.getYRot(), this.getXRot());
		}
	}

	private void flyInAir() { // TODO 麻了，整个都要重写的
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

		double lift = this.getLookSpeed() >= 1.5 ? 0.02D : 0.008888888888888888D * this.getDeltaMovement().horizontalDistanceSqr();
		if (this.inputClimbUp) {
			lift *= 2;
		} else if (this.inputDecline) {
			lift *= -2;
		}
		Vec3d liftVec = new Vec3d(0.0D, lift, 0.0D).xRot(this.getXRotRad()).yRot(this.getYRotRad()).zRot(this.getZRotRad());
		motion = motion.add(liftVec);

		this.setRot(this.getXRot() + this.deltaPitch, this.getYRot() + this.deltaYaw, this.getZRot() + this.deltaRoll);
		this.setDeltaMovement(motion);
	}

	private void controlInAir() {
		if (this.level.isClientSide) {
			double speedRatio = this.getLookSpeed() / 20.0D; // 当前速度与最快速度的比值
			double deltaRotate = speedRatio * 5.0D;
			if (this.inputLookUp) {
				this.deltaPitch -= deltaRotate * Math.cos(this.getZRotRad()); // MC中，仰视则pitch为负，故用减法
				this.deltaYaw += deltaRotate * Math.sin(this.getZRotRad());
			}
			if (this.inputLookDown) {
				this.deltaPitch += deltaRotate * Math.cos(this.getZRotRad());
				this.deltaYaw -= deltaRotate * Math.sin(this.getZRotRad());
			}
			if (this.inputLeftRoll) {
				this.deltaYaw += deltaRotate * Math.abs(Math.sin(this.getXRotRad()));
				this.deltaRoll -= deltaRotate * Math.abs(Math.cos(this.getXRotRad()));
			}
			if (this.inputRightRoll) {
				this.deltaYaw -= deltaRotate * Math.abs(Math.sin(this.getXRotRad()));
				this.deltaRoll += deltaRotate * Math.abs(Math.cos(this.getXRotRad()));
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
		this.deltaPitch *= 0.9F;
		this.deltaYaw *= 0.9F;
		this.deltaRoll *= 0.9F;

		Vec3d res = new Vec3d(0.0D, 0.0D, 0.04D * this.getLookSpeedSqr()).xRot(this.getXRotRad()).yRot(this.getYRotRad()).zRot(this.getZRotRad());
		this.setDeltaMovement(this.getDeltaMovement().add(VecHelper.calcResistance(this.getDeltaMovement(), res)));
	}

	public void setControlState(boolean speedUp, boolean left, boolean right) {
		this.setSpeedUp(speedUp);
		this.setLeft(left);
		this.setRight(right);
	}

	public void setInputEngineOnActivation(boolean activation) {
		if (!activation) this.changeEngineState();
		this.inputEngineOnActivation = activation;
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

	public boolean getSpeedUp() {
		return this.entityData.get(SPEED_UP);
	}

	private void setSpeedUp(boolean speedUp) {
		this.entityData.set(SPEED_UP, speedUp);
	}

	public boolean getLeft() {
		return this.entityData.get(LEFT);
	}

	private void setLeft(boolean left) {
		this.entityData.set(LEFT, left);
	}

	public boolean getRight() {
		return this.entityData.get(RIGHT);
	}

	private void setRight(boolean right) {
		this.entityData.set(RIGHT, right);
	}

	private Status getStatus() {
		if (this.checkUnderWater()) {
			return Status.UNDER_WATER;
		} else if (this.isAboveWater()) {
			return Status.ABOVE_WATER;
		} else {
			float f = this.getGroundFriction();
			if (f > 0.0F) {
				return this.getLandingGear() ? Status.STAND_ON_GROUND : Status.LIE_ON_GROUND;
			} else {
				return Status.IN_AIR;
			}
		}
	}

	public float getGroundFriction() {
		AABB aabb = this.getBoundingBox();
		AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001D, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
		int i = Mth.floor(aabb1.minX) - 1;
		int j = Mth.ceil(aabb1.maxX) + 1;
		int k = Mth.floor(aabb1.minY) - 1;
		int l = Mth.ceil(aabb1.maxY) + 1;
		int i1 = Mth.floor(aabb1.minZ) - 1;
		int j1 = Mth.ceil(aabb1.maxZ) + 1;
		VoxelShape voxelshape = Shapes.create(aabb1);
		float f = 0.0F;
		int k1 = 0;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int l1 = i; l1 < j; ++l1) {
			for (int i2 = i1; i2 < j1; ++i2) {
				int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
				if (j2 != 2) {
					for (int k2 = k; k2 < l; ++k2) {
						if (j2 <= 0 || k2 != k && k2 != l - 1) {
							pos.set(l1, k2, i2);
							BlockState blockstate = this.level.getBlockState(pos);
							if (!(blockstate.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level, pos).move(l1, k2, i2), voxelshape, BooleanOp.AND)) {
								f += blockstate.getFriction(this.level, pos, this);
								++k1;
							}
						}
					}
				}
			}
		}

		return f / (float) k1;
	}

	public boolean isOnLand() { // TODO 以后要自行编写碰撞检测
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
		return new Vec3(0.0D, this.getEyeHeight() + this.getPassengersRidingOffset(), 0.0D)
				.add(new Vec3d(0.0D, 0.0D, this.getHorizontalRidingOffset()).xRot(this.getXRotRad()).yRot(this.getYRotRad()).zRot(this.getZRotRad()));
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

	protected void setRot(float pitch, float yaw, float roll) {
		this.setRot(yaw, pitch);
		this.setZRot(Mth.wrapDegrees(roll));
	}

	@Override
	protected void setRot(float yaw, float pitch) {
		this.setXRot(Mth.wrapDegrees(pitch));
		this.setYRot(Mth.wrapDegrees(yaw));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setEngineState(tag.getBoolean("EngineState"));
		this.setLandingGear(tag.getBoolean("LandingGear"));
		this.setTractor(tag.getBoolean("Tractor"));
		this.setEnginePower(tag.getInt("EnginePower"));
		this.setZRot(tag.getFloat("ZRot"));
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
		} else if (this.outOfControlTicks < 60.0F) {
			if (!this.level.isClientSide) {
				return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
			} else {
				return InteractionResult.SUCCESS;
			}
		} else {
			return InteractionResult.PASS;
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

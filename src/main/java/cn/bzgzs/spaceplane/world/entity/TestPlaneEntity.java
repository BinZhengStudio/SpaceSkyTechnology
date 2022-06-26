package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.network.client.PlaneEnginePacket;
import cn.bzgzs.spaceplane.network.client.PlaneLandingGearPacket;
import net.minecraft.BlockUtil;
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
	// 没啥用的数据同步
	private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SPEED_UP = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.INT);
	public static final int LANDING_GEAR_HEIGHT = 2;
	private float invFriction;
	private float outOfControlTicks;
	private float deltaYawRotate;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYRot;
	private double lerpXRot;
	private boolean inputEngineOnActivation;
	private boolean inputLandingGearActivation;
	private boolean inputLeft;
	private boolean inputRight;
	private boolean inputSpeedUp;
	private float landFriction;
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
		return size.height;
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
		this.entityData.define(FUEL, 0); // 燃油量，单位mB
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

	@Override
	public double getPassengersRidingOffset() {
		return -0.1D;
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
		this.lerpYRot = yaw;
		this.lerpXRot = pitch;
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
		if (this.status != Status.OTHER) {
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
			this.lift(); // 升力
			this.resistance(); // 阻力
			if (this.level.isClientSide) {
				this.controlPlane();
				// 经测试，不发包不影响游戏体验
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(this.getSpeedUp(), this.getLeft(), this.getRight()));
			}

			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
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

	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
			double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
			this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
			this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d1, d2);
			this.setRot(this.getYRot(), this.getXRot());
		}
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
			if (this.entityData.get(ENGINE_ON)) {
				if (this.entityData.get(LANDING_GEAR) && this.status == Status.ON_LAND) {
					this.entityData.set(ENGINE_ON, false);
				}
			} else {
//				if (this.entityData.get(FUEL) > 0) {
				this.entityData.set(ENGINE_ON, true);
//				}
			}
			if (this.level.isClientSide) {
				NetworkHandler.INSTANCE.sendToServer(new PlaneEnginePacket(this.entityData.get(ENGINE_ON)));
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
			this.entityData.set(LANDING_GEAR, !this.entityData.get(LANDING_GEAR));
			if (this.level.isClientSide) {
				NetworkHandler.INSTANCE.sendToServer(new PlaneLandingGearPacket(this.entityData.get(LANDING_GEAR)));
			}
		}
	}

	public boolean getLandingGear() {
		return this.entityData.get(LANDING_GEAR);
	}

	public void setLandingGear(boolean state) {
		this.entityData.set(LANDING_GEAR, state);
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

	private TestPlaneEntity.Status getStatus() {
		if (this.checkUnderWater()) {
			return Status.OTHER;
		} else if (this.isAboveWater()) {
			return Status.ABOVE_WATER;
		} else {
			float f = this.getGroundFriction();
			if (f > 0.0F) {
				this.landFriction = f;
				return Status.ON_LAND;
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

	private void resistance() { // TODO 计算阻力
		this.invFriction = 0.05F; // 运动和旋转阻力，数值越小代表阻力越大
		if (this.status == Status.ABOVE_WATER) {
			this.invFriction = 0.7F;
		} else if (this.status == Status.OTHER) {
			this.invFriction = 0.0F;
		} else if (this.status == Status.IN_AIR) {
			this.invFriction = 0.9F;
		} else if (this.status == Status.ON_LAND) { // TODO 需要添加擦地阻力
			if (this.entityData.get(ENGINE_ON) && this.entityData.get(LANDING_GEAR)) {
				this.invFriction = 0.99F;
			} else {
				this.invFriction = 0.9F;
			}
			this.deltaYawRotate = 0.0F;
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply(this.invFriction, 1.0D, this.invFriction));
		this.deltaYawRotate *= this.invFriction;
	}

	private void lift() { // 升力
		float gravity = this.isNoGravity() ? 0.0F : -0.04F;
		double lift = 0.0D;
		if (this.oldStatus == Status.IN_AIR && this.status == Status.ON_LAND) { // 从空中降落地面
//			this.setPos(this.getX(), (double)(this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D, this.getZ()); TODO
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
		}

		this.setDeltaMovement(this.getDeltaMovement().add(0.0D, gravity, 0.0D));
	}

	private void controlPlane() {
		if (this.isVehicle()) {
			float f = 0.0F;
			if (this.status == Status.ON_LAND && !this.entityData.get(ENGINE_ON) && this.entityData.get(LANDING_GEAR)) {
				if (this.inputLeft) {
					--this.deltaYawRotate;
				}

				if (this.inputRight) {
					++this.deltaYawRotate;
				}
			} else if (this.status == Status.IN_AIR) { // TODO 根据速度确定加速度
				if (this.inputLeft) {
					--this.deltaYawRotate;
				}

				if (this.inputRight) {
					++this.deltaYawRotate;
				}
			}
			this.setYRot(this.getYRot() + this.deltaYawRotate);

			if (this.inputSpeedUp) {
				if (this.entityData.get(ENGINE_ON)) {
					f += 0.1F;
				} else if (this.status == Status.ON_LAND && this.entityData.get(LANDING_GEAR)) {
					f += 0.04F;
				}
			}

//			this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f));
			this.setDeltaMovement(this.getDeltaMovement().add(Math.sin(Math.toRadians(-this.getYRot())) * f, 0.0D, Math.cos(Math.toRadians(this.getYRot())) * f));
//			this.setControlState(this.inputSpeedUp, this.inputLeft, this.inputRight);
		}
	}

	// TODO 临时代码
	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			passenger.setPos(this.calculateRiderPosition());
			passenger.setYRot(passenger.getYRot() + this.deltaYawRotate);
			passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaYawRotate);
			this.clampRotation(passenger);
		}
	}

	private Vec3 calculateRiderPosition() {
		return new Vec3(this.getX(), this.getY(), this.getZ()).add(this.calculateRiderOffset());
	}

	private Vec3 calculateRiderOffset() {
		double yaw = -Math.toRadians(this.getYRot()); // 转换为弧度制
		return new Vec3(0, 0, 2).add(0, this.getEyeHeight(), 0).yRot((float) yaw);
	}

	protected void clampRotation(Entity entityToUpdate) {
		entityToUpdate.setYBodyRot(this.getYRot());
		float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
		float f1 = Mth.clamp(f, -105.0F, 105.0F);
		entityToUpdate.yRotO += f1 - f;
		entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
		entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
	}

	@Override
	public void onPassengerTurned(Entity entityToUpdate) {
		this.clampRotation(entityToUpdate);
	}

	public enum Status {
		ABOVE_WATER, // 以后写水路两栖飞机可能用到
		ON_LAND,
		IN_AIR,
		OTHER
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.entityData.set(ENGINE_ON, tag.getBoolean("EngineState"));
		this.entityData.set(LANDING_GEAR, tag.getBoolean("LandingGear"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putBoolean("EngineState", this.entityData.get(ENGINE_ON));
		tag.putBoolean("LandingGear", this.entityData.get(LANDING_GEAR));
	}

	@Override
	public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
		if (pPlayer.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (this.outOfControlTicks < 60.0F) {
			if (!this.level.isClientSide) {
				return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
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

	public void setClientInput(boolean left, boolean right, boolean speedUp) {
		this.inputLeft = left;
		this.inputRight = right;
		this.inputSpeedUp = speedUp;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

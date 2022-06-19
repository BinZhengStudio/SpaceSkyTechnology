package cn.bzgzs.spaceplane.world.entity;

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

import javax.annotation.Nullable;
import java.util.List;

public class TestPlane2 extends Entity {
	private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> ENGINE_START = SynchedEntityData.defineId(TestPlaneEntity.class, EntityDataSerializers.BOOLEAN);
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
	private boolean inputLeft;
	private boolean inputRight;
	private boolean inputSpeedUp;
	private float landFriction;
	private TestPlaneEntity.Status status;
	private TestPlaneEntity.Status oldStatus;
	private double lastYDelta;

	public TestPlane2(EntityType<?> type, Level world) {
		super(type, world);
	}

//	public TestPlaneEntity(Level world, double x, double y, double z) {
//		this(EntityTypeList.TEST.get(), world);
//		this.setPos(x, y, z);
//		this.xo = x;
//		this.yo = y;
//		this.zo = z;
//	}

	protected float getEyeHeight(Pose pose, EntityDimensions size) {
		return size.height;
	}

	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ENGINE_ON, false);
		this.entityData.define(ENGINE_START, false);
	}

	public boolean canCollideWith(Entity entity) {
		return canVehicleCollide(this, entity);
	}

	public static boolean canVehicleCollide(Entity entity, Entity vehicle) {
		return (vehicle.canBeCollidedWith() || vehicle.isPushable()) && !entity.isPassengerOfSameVehicle(vehicle);
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean isPushable() {
		return false;
	}

	protected Vec3 getRelativePortalPosition(Direction.Axis pAxis, BlockUtil.FoundRectangle pPortal) {
		return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(pAxis, pPortal));
	}

	public double getPassengersRidingOffset() {
		return -0.1D;
	}

	public boolean hurt(DamageSource pSource, float pAmount) {
		return false;
	}

	public void push(Entity entity) {
		if (entity instanceof TestPlaneEntity) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.push(entity);
			}
		} else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.push(entity);
		}
	}

	public boolean isPickable() {
		return !this.isRemoved();
	}

	public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
		this.lerpX = pX;
		this.lerpY = pY;
		this.lerpZ = pZ;
		this.lerpYRot = pYaw;
		this.lerpXRot = pPitch;
		this.lerpSteps = 10;
	}

	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	public void tick() {
		this.oldStatus = this.status;
		this.status = this.getStatus();
		if (this.status != TestPlaneEntity.Status.OTHER) {
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
			this.calculateLift(); // TODO 升力
			if (this.level.isClientSide) {
				this.controlPlane();
//				this.level.sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
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

	private void setControlState(boolean left, boolean right, boolean speedUp) {
		this.inputLeft = left;
		this.inputRight = right;
		this.inputSpeedUp = speedUp;
	}

	private TestPlaneEntity.Status getStatus() {
		if (this.checkUnderWater()) {
			return TestPlaneEntity.Status.OTHER;
		} else if (this.isAboveWater()) {
			return TestPlaneEntity.Status.ABOVE_WATER;
		} else {
			float f = this.getGroundFriction();
			if (f > 0.0F) {
				this.landFriction = f;
				return TestPlaneEntity.Status.ON_LAND;
			} else {
				return TestPlaneEntity.Status.IN_AIR;
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

	private void calculateLift() { // TODO 计算升力的部分
		double gravity = this.isNoGravity() ? 0.0D : -0.04D;
		this.invFriction = 0.05F; // 运动和旋转阻力，数值越小代表阻力越大
		if (this.oldStatus == TestPlaneEntity.Status.ON_LAND && this.status != TestPlaneEntity.Status.ON_LAND) { // 从空中降落地面
//			this.setPos(this.getX(), (double)(this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D, this.getZ()); TODO
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
			this.lastYDelta = 0.0D;
			this.status = TestPlaneEntity.Status.ON_LAND;
		} else {
			if (this.status == TestPlaneEntity.Status.ABOVE_WATER) {
				this.invFriction = 0.7F;
			} else if (this.status == TestPlaneEntity.Status.OTHER) {
				this.invFriction = 0.0F;
			} else if (this.status == TestPlaneEntity.Status.IN_AIR) {
				this.invFriction = 0.9F;
			} else if (this.status == TestPlaneEntity.Status.ON_LAND) {
				this.invFriction = this.landFriction;
				if (this.getControllingPassenger() instanceof Player) {
					this.landFriction /= 2.0F;
				}
			}

			Vec3 motion = this.getDeltaMovement();
			this.setDeltaMovement(motion.x * this.invFriction, motion.y + gravity, motion.z * this.invFriction);
			this.deltaYawRotate *= this.invFriction;
		}
	}

	private void controlPlane() {
		if (this.isVehicle()) {
			float f = 0.0F;
			if (this.inputLeft) {
				--this.deltaYawRotate;
			}

			if (this.inputRight) {
				++this.deltaYawRotate;
			}

			this.setYRot(this.getYRot() + this.deltaYawRotate);
			if (this.inputSpeedUp) {
				f += 0.04F;
			}

			this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * f, 0.0D, Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * f));
			this.setControlState(this.inputLeft, this.inputRight, this.inputSpeedUp);
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

	public void onPassengerTurned(Entity entityToUpdate) {
		this.clampRotation(entityToUpdate);
	}

	public enum Status {
		ABOVE_WATER,
		ON_LAND,
		IN_AIR,
		OTHER
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {

	}

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

	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().isEmpty() && passenger instanceof Player;
	}

	@Nullable
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

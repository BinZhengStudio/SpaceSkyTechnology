package cn.bzgzs.spaceplane.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * 战斗机的抽象类<br>
 * 以下注释所述的战机坐标系，均以飞机为原点，飞机头朝向为<strong>Z轴</strong>正方向，飞机EyeHeight为Y轴原点，按右手定则建立的坐标系<br>
 * 本类中定义所有关于角度的变量，均采用弧度制
 */
public abstract class BasePlaneEntity extends Entity {
	// 发动机输出大小
	protected static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴方向上的速度，以下变量中的X、Y、Z轴，均为战机坐标系中的X、Y、Z轴
	protected static final EntityDataAccessor<Float> X_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> Y_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> Z_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴方向上的加速度
	protected static final EntityDataAccessor<Float> ACCEL_X = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> ACCEL_Y = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> ACCEL_Z = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 爬升和下降的速度
	protected static final EntityDataAccessor<Float> CLIMB_AND_DECLINE_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转方向，代表X、Y、Z轴，均采用弧度制
	protected static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转速度，分别代表X、Y、Z轴
	protected static final EntityDataAccessor<Float> PITCH_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> YAW_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> ROLL_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转加速度
	protected static final EntityDataAccessor<Float> PITCH_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> YAW_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Float> ROLL_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 是否抛副油箱
	protected static final EntityDataAccessor<Boolean> AUXILIARY_TANK = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);
	// 起落架是否放下
	protected static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);
	// 爬升与下降
	protected static final EntityDataAccessor<Boolean> CLIMBING_UP = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> DECLINING = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);

	public BasePlaneEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() { // 用于客户端与服务端同步数据
		this.entityData.define(POWER, 0.0F); // 油门
		this.entityData.define(X_SPEED, 0.0F); // X轴速度
		this.entityData.define(Y_SPEED, 0.0F); // Y轴速度
		this.entityData.define(Z_SPEED, 0.0F); // Z轴速度
		this.entityData.define(ACCEL_X, 0.0F); // X轴加速度
		this.entityData.define(ACCEL_Y, 0.0F); // Y轴加速度
		this.entityData.define(ACCEL_Z, 0.0F); // Z轴加速度
		this.entityData.define(CLIMB_AND_DECLINE_SPEED, 0.0F); // 爬升和下降速度
		this.entityData.define(PITCH, 0.0F); // X轴旋转
		this.entityData.define(YAW, 0.0F); // Y轴旋转
		this.entityData.define(ROLL, 0.0F); // Z轴旋转
		this.entityData.define(YAW_SPEED, 0.0F); // X轴旋转速度
		this.entityData.define(PITCH_SPEED, 0.0F); // Y轴旋转速度
		this.entityData.define(ROLL_SPEED, 0.0F); // Z轴旋转速度
		this.entityData.define(YAW_ACCEL, 0.0F); // X轴旋转加速度
		this.entityData.define(PITCH_ACCEL, 0.0F); // Y轴旋转加速度
		this.entityData.define(ROLL_ACCEL, 0.0F); // Z轴旋转加速度
		this.entityData.define(AUXILIARY_TANK, false); // 是否抛副油箱
		this.entityData.define(LANDING_GEAR, false); // 起落架是否放下
		this.entityData.define(CLIMBING_UP, false); // 是否爬升
		this.entityData.define(DECLINING, false); // 是否下降
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.SOUNDS;
	}

	/**
	 * 因为Minecraft原版的实体Y坐标是按照实体底部的Y坐标来计算的，如果直接用getY()获取的Y坐标进行绕Z轴旋转的操作，会出现bug。<br>
	 * 原版的这个方法是用来设置生物的眼睛高度，我们同时用于设置Y偏移。
	 *
	 * @param pose 没用的参数
	 * @param size 没用的参数
	 * @return Y轴偏移
	 */
	@Override
	protected abstract float getEyeHeight(Pose pose, EntityDimensions size);

	/**
	 * 获取飞行员的骑乘位置偏移，以战机坐标系为基准
	 *
	 * @return 骑乘位置偏移
	 */
	public abstract Vec3 getRiderOffset();

	/**
	 * 获取发动机的最大功率
	 *
	 * @return 最大功率
	 */
	public abstract int getMaxPower();

	/**
	 * 获取飞机旋转的加速度，与飞机的机动性能相关。<br>
	 * 单位：rad/(tick*tick)。
	 *
	 * @return 旋转加速度
	 */
	public abstract float getRotateAccel();

	/**
	 * 功能正如其名。
	 *
	 * @return 最大旋转速度，单位：rad/tick
	 */
	public abstract float getMaxRotateSpeed();

	/**
	 * 获取飞机移动的加速度，与飞机的机动性能相关。<br>
	 * 如需设置飞机在爬升和下降的加速度，请使用{@link #getClimbAndDeclineAccel()}方法。<br>
	 * 单位：m/(tick*tick)。
	 *
	 * @return 移动加速度
	 */
	public abstract float getMotionAccel();

	/**
	 * 最大移动速度，单位：m/tick
	 *
	 * @return 最大移动速度
	 */
	public abstract float getMaxMotionSpeed();

	/**
	 * 飞机爬升和下降的加速度，与飞机的机动性能相关。<br>
	 * 单位：m/(tick*tick)。
	 *
	 * @return 爬升和下降加速度
	 */
	public abstract float getClimbAndDeclineAccel();

	/**
	 * 最大爬升和下降速度，单位：m/tick
	 *
	 * @return 最大爬升和下降速度
	 */
	public abstract float getMaxClimbAndDeclineSpeed();

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public boolean canBeCollidedWith() {
		return true; // TODO 以后要自己搞碰撞检测
	}

	@Override
	public void positionRider(Entity passenger) {
		if (this.hasPassenger(passenger)) {
			passenger.setPos(this.calculateRiderPosition());
		}
	}

	private Vec3 calculateRiderPosition() {
		return new Vec3(this.getX(), this.getY(), this.getZ()).add(this.calculateRiderOffset());
	}

	private Vec3 calculateRiderOffset() {
		float pitch = this.entityData.get(PITCH); // TODO 需要转换为相对于世界的坐标系
		double yaw = -Math.toRadians(this.getYRot()); // 转换为弧度制
		double roll = Math.toRadians(this.getXRot()); // TODO 需要修改
		return this.getRiderOffset().add(0, this.getEyeHeight(), 0).xRot(pitch).yRot((float) yaw).zRot((float) roll);
	}

	private Vec3 calculateResistance() { // TODO 需要修改
		Vec3 vec3 = Vec3.ZERO;
		Vec3 motion = this.getDeltaMovement();
		vec3.add(motion).scale(-0.1);
		return this.getDeltaMovement().scale(-0.1);
	}

	private Vec3 calculateMotion() { // TODO 还需要加入其他的速度
		float climb = this.entityData.get(CLIMB_AND_DECLINE_SPEED);
		Vec3 vec3 = new Vec3(0, climb, 0);
//		vec3.add(this.calculateResistance());
		return vec3;
//		Vec3 vector3d = this.getDeltaMovement();
//		double d0 = this.getX() + vector3d.x;
//		double d1 = this.getY() + vector3d.y;
//		double d2 = this.getZ() + vector3d.z;
//		if (vector3d.y - this.getClimbAndDeclineAccel() < -this.getMaxClimbAndDeclineSpeed()) {
//			this.setDeltaMovement(new Vec3(0, -this.getMaxClimbAndDeclineSpeed(), 0));
//		} else {
//			this.setDeltaMovement(this.getDeltaMovement().add(0,-this.getClimbAndDeclineAccel(), 0));
//		}
//		this.setPos(d0, d1, d2);
	}

	@Override
	public void tick() {
		if (!this.isRemoved() && this.level.isLoaded(new BlockPos(this.getX(), this.getY(), this.getZ()))) {
			super.tick();
			if (this.entityData.get(CLIMBING_UP)) {
				this.entityData.set(CLIMB_AND_DECLINE_SPEED, Math.min(this.getMaxClimbAndDeclineSpeed(), this.entityData.get(CLIMB_AND_DECLINE_SPEED) + this.getClimbAndDeclineAccel()));
			}

			if (this.entityData.get(DECLINING)) {
				this.entityData.set(CLIMB_AND_DECLINE_SPEED, Math.max(-this.getMaxClimbAndDeclineSpeed(), this.entityData.get(CLIMB_AND_DECLINE_SPEED) - this.getClimbAndDeclineAccel()));
			}

			Vec3 vector3d = this.getDeltaMovement();
			double d0 = this.getX() + vector3d.x;
			double d1 = this.getY() + vector3d.y;
			double d2 = this.getZ() + vector3d.z;
			this.setDeltaMovement(this.calculateMotion());
			this.setDeltaMovement(this.getDeltaMovement().add(this.calculateResistance()));
			this.setPos(d0, d1, d2);
		}
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) { // TODO 根据子弹、炮弹等调整
		return super.hurt(pSource, pAmount);
	}

	public void setInput(int key, boolean isDown) {
		if (isDown) {
			switch (key) {
				case 0 -> System.out.println("按下了W");
				case 1 -> System.out.println("按下了a");
				case 2 -> System.out.println("按下了s");
				case 3 -> System.out.println("按下了d");
				case 4 -> this.entityData.set(CLIMBING_UP, true);
				case 5 -> System.out.println("left");
				case 6 -> this.entityData.set(DECLINING, true);
				case 7 -> System.out.println("right");
				case 8 -> System.out.println("按下了space");
			}
		} else {
			switch (key) {
				case 0 -> System.out.println("按下了W-");
				case 1 -> System.out.println("按下了a-");
				case 2 -> System.out.println("按下了s-");
				case 3 -> System.out.println("按下了d-");
				case 4 -> this.entityData.set(CLIMBING_UP, false);
				case 5 -> System.out.println("left-");
				case 6 -> this.entityData.set(DECLINING, false);
				case 7 -> System.out.println("right-");
				case 8 -> System.out.println("按下了space-");
			}
		}
	}

	/**
	 * 飞机炸掉之后留下的东西
	 *
	 * @return 留下的所有东西
	 */
	public abstract NonNullList<ItemStack> getDropItem();

	@Override
	public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
		super.lerpTo(pX, pY, pZ, pYaw, pPitch, pPosRotationIncrements, pTeleport);
	}

	@Override
	public void lerpHeadTo(float pYaw, int pPitch) {
		super.lerpHeadTo(pYaw, pPitch);
	}

	@Override
	public void lerpMotion(double pX, double pY, double pZ) {
		super.lerpMotion(pX, pY, pZ);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (!this.level.isClientSide) {
			player.stopRiding();
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected boolean canAddPassenger(Entity entity) { // 只有玩家可以乘上飞机
		return this.getPassengers().size() < 1 && entity instanceof Player;
	}

	@Nullable
	@Override
	public Entity getControllingPassenger() {
		return this.getFirstPassenger();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.entityData.set(X_SPEED, tag.getFloat("xSpeed"));
		this.entityData.set(Y_SPEED, tag.getFloat("ySpeed"));
		this.entityData.set(Z_SPEED, tag.getFloat("zSpeed"));
		this.entityData.set(PITCH, tag.getFloat("pitch"));
		this.entityData.set(YAW, tag.getFloat("yaw"));
		this.entityData.set(ROLL, tag.getFloat("roll"));
		this.entityData.set(PITCH_SPEED, tag.getFloat("pitchSpeed"));
		this.entityData.set(YAW_SPEED, tag.getFloat("yawSpeed"));
		this.entityData.set(ROLL_SPEED, tag.getFloat("rollSpeed"));
		this.entityData.set(AUXILIARY_TANK, tag.getBoolean("auxiliaryTank"));
		this.entityData.set(LANDING_GEAR, tag.getBoolean("landingGear"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putFloat("xSpeed", this.entityData.get(X_SPEED));
		tag.putFloat("ySpeed", this.entityData.get(Y_SPEED));
		tag.putFloat("zSpeed", this.entityData.get(Z_SPEED));
		tag.putFloat("pitch", this.entityData.get(PITCH));
		tag.putFloat("yaw", this.entityData.get(YAW));
		tag.putFloat("roll", this.entityData.get(ROLL));
		tag.putFloat("pitchSpeed", this.entityData.get(PITCH_SPEED));
		tag.putFloat("yawSpeed", this.entityData.get(YAW_SPEED));
		tag.putFloat("rollSpeed", this.entityData.get(ROLL_SPEED));
		tag.putBoolean("auxiliaryTank", this.entityData.get(AUXILIARY_TANK));
		tag.putBoolean("landingGear", this.entityData.get(LANDING_GEAR));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

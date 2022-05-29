package cn.bzgzs.spaceplane.world.entity;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public abstract class BasePlaneEntity extends Entity {
	// 发动机输出大小
	private static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴方向上的速度，以下变量中的X、Y、Z轴，均以飞机为原点，飞机头朝向为X轴正方向，按右手定则建立坐标系
	private static final EntityDataAccessor<Float> X_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> Y_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> Z_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴方向上的加速度
	private static final EntityDataAccessor<Float> ACCEL_X = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ACCEL_Y = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ACCEL_Z = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转方向，代表X轴，Y、Z轴原版已提供
	private static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转速度，分别代表X、Y、Z轴
	private static final EntityDataAccessor<Float> YAW_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PITCH_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ROLL_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 各轴的旋转加速度
	private static final EntityDataAccessor<Float> YAW_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PITCH_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ROLL_ACCEL = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	// 是否抛副油箱
	private static final EntityDataAccessor<Boolean> AUXILIARY_TANK = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);
	// 起落架是否放下
	private static final EntityDataAccessor<Boolean> LANDING_GEAR = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.BOOLEAN);

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
		this.entityData.define(ROLL, 0.0F); // X轴旋转
		this.entityData.define(YAW_SPEED, 0.0F); // X轴旋转速度
		this.entityData.define(PITCH_SPEED, 0.0F); // Y轴旋转速度
		this.entityData.define(ROLL_SPEED, 0.0F); // Z轴旋转速度
		this.entityData.define(YAW_ACCEL, 0.0F); // X轴旋转加速度
		this.entityData.define(PITCH_ACCEL, 0.0F); // Y轴旋转加速度
		this.entityData.define(ROLL_ACCEL, 0.0F); // Z轴旋转加速度
		this.entityData.define(AUXILIARY_TANK, false); // 是否抛副油箱
		this.entityData.define(LANDING_GEAR, false); // 起落架是否放下
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.SOUNDS;
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public boolean canBeCollidedWith() {
		return true; // TODO 以后要自己搞碰撞检测
	}

	@Override
	public double getPassengersRidingOffset() { // TODO 根据起落架的位置调整
		return 0;
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) { // TODO 根据子弹、炮弹等调整
		return super.hurt(pSource, pAmount);
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
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

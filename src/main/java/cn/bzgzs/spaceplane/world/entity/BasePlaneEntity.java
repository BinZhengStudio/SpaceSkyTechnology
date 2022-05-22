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

public abstract class BasePlaneEntity extends Entity {
	private static final EntityDataAccessor<Float> POWER = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> X_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> Y_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> Z_SPEED = SynchedEntityData.defineId(BasePlaneEntity.class, EntityDataSerializers.FLOAT);

	public BasePlaneEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() { // 用于客户端与服务端同步数据
		this.entityData.define(POWER, 0.0F); // 油门
		this.entityData.define(X_SPEED, 0.0F); // X轴速度
		this.entityData.define(Y_SPEED, 0.0F); // Y轴速度
		this.entityData.define(Z_SPEED, 0.0F); // Z轴速度
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public double getPassengersRidingOffset() { // TODO 根据起落架的位置调整
		return super.getPassengersRidingOffset();
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) { // TODO 根据子弹、炮弹等调整
		return super.hurt(pSource, pAmount);
	}

	/**
	 * 飞机炸掉之后留下的东西
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
	protected boolean canRide(Entity entity) { // 只有玩家可以乘上飞机
		return entity instanceof Player;
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

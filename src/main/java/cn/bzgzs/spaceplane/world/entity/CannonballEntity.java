package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class CannonballEntity extends Entity implements IEntityAdditionalSpawnData {
	@Nullable
	private UUID ownerUUID;
	@Nullable
	private BasePlaneEntity cachedOwner;
	private static final int TOTAL_FIRE_TIME = 100;
	private int fireTime;
	private boolean leftOwner;
	private boolean hasBeenShot = false;
	private double speedX;
	private double speedY;
	private double speedZ;

	public CannonballEntity(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	public CannonballEntity(Level world, BasePlaneEntity plane, int pixelX, int pixelY, int pixelZ, Vec3 initialSpeed, float pitch, float yaw, float roll) {
		super(EntityTypeList.CANNONBALL.get(), world);
		Vec3d pointVec = new Vec3d(pixelX / 16.0D, pixelY / 16.0D, pixelZ / 16.0D);
		Vec3d offset = pointVec.zRot(-Math.toRadians(roll)).xRot(Math.toRadians(pitch)).yRot(-Math.toRadians(yaw));
		Vec3d yOffset = new Vec3d(0.0D, -this.getDimensions(this.getPose()).height / 2, 0.0D).xRot(Math.toRadians(pitch)).yRot(-Math.toRadians(yaw));
		this.setPos(plane.getCenterPos().add(offset).add(yOffset));
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.xOld = this.xo;
		this.yOld = this.yo;
		this.zOld = this.zo;
		this.speedX = initialSpeed.x;
		this.speedY = initialSpeed.y;
		this.speedZ = initialSpeed.z;
		this.setRot(yaw, pitch);
		this.setOwner(plane);
	}

	@Override
	protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
		return 0.5F;
	}

	public void setOwner(@Nullable BasePlaneEntity entity) {
		if (entity != null) {
			this.ownerUUID = entity.getUUID();
			this.cachedOwner = entity;
		}
	}

	@Nullable
	public BasePlaneEntity getOwner() {
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
			return this.cachedOwner;
		} else if (this.ownerUUID != null && this.level instanceof ServerLevel serverLevel) {
			if (serverLevel.getEntity(this.ownerUUID) instanceof BasePlaneEntity plane) {
				this.cachedOwner = plane;
			}
			return this.cachedOwner;
		} else {
			return null;
		}
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
	protected void addAdditionalSaveData(CompoundTag tag) {
		if (this.ownerUUID != null) {
			tag.putUUID("Owner", this.ownerUUID);
		}
		tag.putBoolean("LeftOwner", this.leftOwner);
		tag.putBoolean("HasBeenShot", this.hasBeenShot);
		tag.putDouble("SpeedX", this.speedX);
		tag.putDouble("SpeedY", this.speedY);
		tag.putDouble("SpeedZ", this.speedZ);
		tag.putInt("FIreTime", this.fireTime);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.hasUUID("Owner")) this.ownerUUID = tag.getUUID("Owner");
		if (tag.contains("LeftOwner")) this.leftOwner = tag.getBoolean("LeftOwner");
		if (tag.contains("HasBeenShot")) this.hasBeenShot = tag.getBoolean("HasBeenShot");
		if (tag.contains("SpeedX")) this.speedX = tag.getDouble("SpeedX");
		if (tag.contains("SpeedY")) this.speedY = tag.getDouble("SpeedY");
		if (tag.contains("SpeedZ")) this.speedZ = tag.getDouble("SpeedZ");
		if (tag.contains("FireTime")) this.fireTime = tag.getInt("FireTime");
	}

	@Override
	public void tick() {
		Entity entity = this.getOwner();
		if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.isLoaded(this.blockPosition())) {
			if (!this.hasBeenShot) {
				this.setDeltaMovement(this.speedX, this.speedY, this.speedZ);
				this.fireTime = TOTAL_FIRE_TIME;
				this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner(), this.blockPosition());
				this.hasBeenShot = true;
			}

			if (!this.leftOwner) {
				this.leftOwner = this.checkLeftOwner();
			}

			super.tick();

			HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
			if (hitresult.getType() != HitResult.Type.MISS && this.leftOwner) {
				this.onHit(hitresult);
			}

			if (this.fireTime > 0) {
				this.fireTime--;
			} else {
				this.selfDestruct();
			}

			this.checkInsideBlocks();
			Vec3 vec3 = this.getDeltaMovement();
			double x = this.getX() + vec3.x;
			double y = this.getY() + vec3.y;
			double z = this.getZ() + vec3.z;
			float res;
			if (this.isInWater()) {
				for (int i = 0; i < 4; ++i) {
					this.level.addParticle(ParticleTypes.BUBBLE, x - vec3.x * 0.25D, y - vec3.y * 0.25D, z - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
				}
				res = 0.7F;
			} else {
				res = 0.8125F;
			}

			Vec3d accel = new Vec3d(0.0D, 0.0D, 1.5D).xRot(this.getXRotRad()).yRot(this.getYRotRad());
			this.setDeltaMovement(vec3.add(accel).scale(res));
			this.level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
			this.setPos(x, y, z);
		} else {
			this.discard();
		}
	}

	private boolean checkLeftOwner() {
		Entity owner = this.getOwner();
		if (owner != null) {
			for (Entity entity : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (entityIn) -> !entityIn.isSpectator() && entityIn.isPickable())) {
				if (entity.getRootVehicle() == owner.getRootVehicle()) {
					return false;
				}
			}
		}

		return true;
	}

	protected void onHit(HitResult result) {
		HitResult.Type type = result.getType();
		if (type == HitResult.Type.ENTITY) {
			this.onHitEntity((EntityHitResult) result);
		}
		this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
		if (!this.level.isClientSide) {
			boolean flag = ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
			this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0F, false, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
			this.discard();
		}
	}

	protected void onHitEntity(EntityHitResult result) {
		if (!this.level.isClientSide) {
			result.getEntity().hurt(DamageSource.explosion((Explosion) null), 8.0F);
			if (result.getEntity() instanceof PlanePart part) {
				if (!(this.getOwner() != null && part.is(this.getOwner()))) {
					part.getParent().explode();
				}
			}
		}
	}

	protected void selfDestruct() {
		boolean flag = ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 3.0F /* TODO */, false, flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
		this.discard();
	}

	@Override
	public void lerpMotion(double x, double y, double z) {
		this.setDeltaMovement(x, y, z);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			double horizonLength = Math.sqrt(x * x + z * z);
			this.setXRot((float) (Mth.atan2(y, horizonLength) * (double) (180F / (float) Math.PI)));
			this.setYRot((float) (Mth.atan2(x, z) * (double) (180F / (float) Math.PI)));
			this.xRotO = this.getXRot();
			this.yRotO = this.getYRot();
			this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
		}
	}

	protected boolean canHitEntity(Entity entity) {
		if (!entity.isSpectator() && entity.isAlive() && entity.isPickable()) {
			Entity owner = this.getOwner();
			boolean flag = owner == null || this.leftOwner || !owner.isPassengerOfSameVehicle(entity);
			return flag && (entity.noPhysics || entity instanceof PlanePart) && !(entity instanceof BasePlaneEntity);
		} else {
			return false;
		}
	}

	protected void updateRotation() {
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = vec3.horizontalDistance();
		this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI))));
		this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
	}

	protected static float lerpRotation(float start, float end) {
		while (end - start < -180.0F) {
			start -= 360.0F;
		}

		while (end - start >= 180.0F) {
			start += 360.0F;
		}

		return Mth.lerp(0.2F, start, end);
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		Entity entity = this.level.getEntity(packet.getData());
		if (entity != null) {
			if (entity instanceof BasePlaneEntity plane) this.setOwner(plane);
		}
	}

	public double getXRotRad() {
		return Math.toRadians(-this.getXRot());
	}

	public double getYRotRad() {
		return -Math.toRadians(this.getYRot());
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeDouble(this.speedX);
		buffer.writeDouble(this.speedY);
		buffer.writeDouble(this.speedZ);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		this.speedX = additionalData.readDouble();
		this.speedY = additionalData.readDouble();
		this.speedZ = additionalData.readDouble();
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

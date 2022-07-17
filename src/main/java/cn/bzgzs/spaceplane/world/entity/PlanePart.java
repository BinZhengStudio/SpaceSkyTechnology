package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class PlanePart extends PartEntity<TestPlaneEntity> {
	private final Vec3d pointVec;
	private final TestPlaneEntity.Part part;
	private final EntityDimensions size;

	public PlanePart(TestPlaneEntity parent, Vec3d pointVec, TestPlaneEntity.Part part, float size) {
		super(parent);
		this.pointVec = pointVec;
		this.part = part;
		this.size = EntityDimensions.fixed(size, size);
	}

	protected void updatePos(boolean collision) {
		if (collision) {
			Vec3d offset = this.pointVec.xRot(this.getParent().getXRotRad()).yRot(this.getParent().getYRotRad()).zRot(this.getParent().getZRotRad());
			this.setPos(this.getParent().getCenterPos().add(offset).add(0.0D, -this.size.height / 2, 0.0D));
		} else {
			this.setPos(this.getParent().getCenterPos());
		}
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && this.getParent().hurt(this, source, amount);
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.getParent() == entity;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.size;
	}

	@Override
	public boolean shouldBeSaved() {
		return false;
	}

	public TestPlaneEntity.Part getPart() {
		return part;
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
	}
}

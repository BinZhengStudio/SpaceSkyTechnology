package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;

import java.util.List;

public class PlanePart extends PartEntity<TestPlaneEntity> {
	private final Vec3d pointVec;
	private final TestPlaneEntity.Part part;
	private final EntityDimensions size;

	public PlanePart(TestPlaneEntity parent, Vec3d pointVec, TestPlaneEntity.Part part, float size) {
		super(parent);
		this.pointVec = pointVec;
		this.part = part;
		this.size = EntityDimensions.fixed(size, size);
		this.refreshDimensions();
		this.setPosOld(parent.getCenterPos());
	}

//	public PlanePart(TestPlaneEntity parent, double x, double y, double z, TestPlaneEntity.Part part, float size) {
//		this(parent, new Vec3d(x, y, z), part, size);
//	}

	public PlanePart(TestPlaneEntity parent, int pixelX, int pixelY, int pixelZ, TestPlaneEntity.Part part, int pixelSize) {
		this(parent, new Vec3d(pixelX / 16.0D, pixelY / 16.0D, pixelZ / 16.0D), part, pixelSize / 16.0F);
	}

	protected void updatePos(boolean collision) {
		this.setPosOld(this.getPosition(1.0F));
		if (collision) {
			Vec3d offset = this.pointVec.zRot(-this.getParent().getZRotRad()).xRot(this.getParent().getXRotRad()).yRot(this.getParent().getYRotRad());
			this.setPos(this.getParent().getCenterPos().add(offset).add(0.0D, -this.size.height / 2, 0.0D));
			if (!this.getParent().getLandingGear() && this.isLandingGear()) {
				Vec3d gearOffset = new Vec3d(0.0D, -0.5625, 0.0D).zRot(-this.getParent().getZRotRad()).xRot(this.getParent().getXRotRad()).yRot(this.getParent().getYRotRad());
				this.setPos(this.getParent().getCenterPos().add(gearOffset).add(0.0D, -this.size.height / 2, 0.0D));
			}
		} else {
			this.setPos(this.getParent().getCenterPos());
		}
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	protected Vec3 collide(Vec3 motion, List<PlanePart> yCollideList) {
		AABB aabb = this.getBoundingBox();
		List<VoxelShape> list = this.level.getEntityCollisions(this, aabb.expandTowards(motion));
		Vec3 collideVec = motion.lengthSqr() == 0.0D ? motion : collideBoundingBox(this, motion, aabb, this.level, list);
		if (motion.y != collideVec.y) yCollideList.add(this);

		return collideVec;
	}

//	protected Vec3d getCollideRotate(Vec3 motion) {
//		AABB aabb = this.getBoundingBox().move(motion);
//
//	}
//
//	protected Vec3d getBlockInsideOffset(Vec3 motion) {
//		AABB aabb = this.getBoundingBox().move(motion);
//
//	}

	protected boolean isOnLand() { // TODO
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

	protected boolean checkUnderWater() {
		AABB aabb = this.getBoundingBox();
		double d0 = aabb.maxY + 0.001D;
		int minX = Mth.floor(aabb.minX);
		int maxX = Mth.ceil(aabb.maxX);
		int minY = Mth.floor(aabb.minY);
		int maxY = Mth.ceil(d0);
		int minZ = Mth.floor(aabb.minZ);
		int maxZ = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z) {
					pos.set(x, y, z);
					FluidState fluidstate = this.level.getFluidState(pos);
					if (fluidstate.is(FluidTags.WATER) && d0 < (double) ((float) pos.getY() + fluidstate.getHeight(this.level, pos))) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && this.getParent().hurt(this, source, amount);
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.getParent() == entity;
	}

	protected boolean isLandingGear() {
		boolean flag1 = (this.part == TestPlaneEntity.Part.FRONT_LANDING_GEAR || this.part == TestPlaneEntity.Part.FRONT_LANDING_GEAR_WHEEL);
		boolean flag2 = (this.part == TestPlaneEntity.Part.LEFT_LANDING_GEAR || this.part == TestPlaneEntity.Part.LEFT_LANDING_GEAR_WHEEL);
		boolean flag3 = (this.part == TestPlaneEntity.Part.RIGHT_LANDING_GEAR || this.part == TestPlaneEntity.Part.RIGHT_LANDING_GEAR_WHEEL);
		return flag1 || flag2 || flag3;
	}

	protected boolean isWheel() {
		boolean flag1 = (this.part == TestPlaneEntity.Part.FRONT_LANDING_GEAR_WHEEL);
		boolean flag2 = (this.part == TestPlaneEntity.Part.LEFT_LANDING_GEAR_WHEEL);
		boolean flag3 = (this.part == TestPlaneEntity.Part.RIGHT_LANDING_GEAR_WHEEL);
		return flag1 || flag2 || flag3;
	}

	private void setPosOld(Vec3 pos) {
		this.xo = pos.x;
		this.yo = pos.y;
		this.zo = pos.z;
		this.xOld = this.xo;
		this.yOld = this.yo;
		this.zOld = this.zo;
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

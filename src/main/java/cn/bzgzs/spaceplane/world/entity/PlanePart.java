package cn.bzgzs.spaceplane.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.entity.PartEntity;

public class PlanePart extends PartEntity<TestPlaneEntity> {
	public PlanePart(TestPlaneEntity parent) {
		super(parent);
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

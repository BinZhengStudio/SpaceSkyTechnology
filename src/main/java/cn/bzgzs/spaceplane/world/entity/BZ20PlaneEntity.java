package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.world.item.ItemList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BZ20PlaneEntity extends BasePlaneEntity {
	public BZ20PlaneEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public Vec3 getRiderOffset() {
		return new Vec3(0, 0.5, 2);
	}

	@Override
	public int getMaxPower() {
		return 0;
	}

	@Override
	public float getRotateAccel() {
		return this.entityData.get(AUXILIARY_TANK) ? 0.2F : 0.13F;
	}

	@Override
	public float getMaxRotateSpeed() {
		return 0.5F;
	}

	@Override
	public float getMotionAccel() {
		return this.entityData.get(AUXILIARY_TANK) ? 1.0F : 0.8F;
	}

	@Override
	public float getMaxMotionSpeed() {
		return 5.0F;
	}

	@Override
	public float getClimbAndDeclineAccel() {
		return this.entityData.get(AUXILIARY_TANK) ? 0.1F : 0.08F;
	}

	@Override
	public float getMaxClimbAndDeclineSpeed() {
		return 0.5F;
	}

	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions size) {
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getDropItem() {
		NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
		list.set(0, new ItemStack(ItemList.RHENIUM_INGOT.get()));
		return list;
	}
}

package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.world.item.ItemList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class J20PlaneEntity extends BasePlaneEntity {
	public J20PlaneEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public NonNullList<ItemStack> getDropItem() {
		NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
		list.set(0, new ItemStack(ItemList.RHENIUM_INGOT.get()));
		return list;
	}
}

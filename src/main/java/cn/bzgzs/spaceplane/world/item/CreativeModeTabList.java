package cn.bzgzs.spaceplane.world.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeModeTabList {
	public static final CreativeModeTab TAB_SPACEPLANE = new CreativeModeTab("spacePlane") {
		@NotNull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ItemList.RHENIUM_INGOT.get());
		}
	};
}

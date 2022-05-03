package cn.bzgzs.spaceplane.world.item;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.level.block.BlockList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemList {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpacePlane.MODID); // 创建一个注册器
	// vvvbbbcz:物品注册示例
	public static final RegistryObject<Item> EXAMPLE = ITEMS.register("example", () -> new Item(new Item.Properties()));
	// vvvbbbcz:方块的物品形态注册示例
	public static final RegistryObject<Item> EXAMPLE_BLOCK = ITEMS.register("example_block", () -> new BlockItem(BlockList.EXAMPLE_BLOCK.get(), new Item.Properties()));
}

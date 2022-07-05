package cn.bzgzs.spaceplane.world.item;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.item.sword.RheniumSword;
import cn.bzgzs.spaceplane.world.level.block.BlockList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemList {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpacePlane.MODID); // 创建一个注册器
	// vvvbbbcz:物品注册
	public static final RegistryObject<Item> RHENIUM_INGOT = ITEMS.register("rhenium_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<Item> PHENIUM_SWORD = ITEMS.register("rhenium_sword", RheniumSword::new);
	// vvvbbbcz:方块的物品形态注册
	public static final RegistryObject<BlockItem> RHENIUM_ORE = ITEMS.register("rhenium_ore", () -> new BlockItem(BlockList.RHENIUM_ORE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> DEEPSLATE_RHENIUM_ORE = ITEMS.register("deepslate_rhenium_ore", () -> new BlockItem(BlockList.DEEPSLATE_RHENIUM_ORE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> RHENIUM_BLOCK = ITEMS.register("rhenium_block", () -> new BlockItem(BlockList.RHENIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> NICKEL_ORE = ITEMS.register("nickel_ore", () -> new BlockItem(BlockList.NICKEL_ORE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> DEEPSLATE_NICKEL_ORE = ITEMS.register("deepslate_nickel_ore", () -> new BlockItem(BlockList.DEEPSLATE_NICKEL_ORE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> NICKEL_BLOCK = ITEMS.register("nickel_block", () -> new BlockItem(BlockList.NICKEL_BLOCK.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockList.ALLOY_FURNACE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> FLUID_PIPE = ITEMS.register("fluid_pipe", () -> new BlockItem(BlockList.FLUID_PIPE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
	public static final RegistryObject<BlockItem> STEAM_ENGINE = ITEMS.register("steam_engine", () -> new BlockItem(BlockList.STEAM_ENGINE.get(), new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE)));
}

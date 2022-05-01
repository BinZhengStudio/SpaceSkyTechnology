package cn.bzgzs.spaceplane.world.item;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemList {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpacePlane.MODID); // 创建一个注册器

	public static final Item EXAMPLE = register(new Item(new Item.Properties()), "example");// vvvbbbcz:这是一个示例

	public static Item register(Item item, String name) { // 注册物品，此种方式可防止空指针
		ITEMS.register(name, () -> item);
		return item;
	}
}

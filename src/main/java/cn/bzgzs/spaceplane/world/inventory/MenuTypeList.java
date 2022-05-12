package cn.bzgzs.spaceplane.world.inventory;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeList {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SpacePlane.MODID);

	public static final RegistryObject<MenuType<SteamEngineMenu>> STEAM_ENGINE = MENU_TYPES.register("steam_engine",  () -> new MenuType<>(SteamEngineMenu::new));
}

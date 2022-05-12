package cn.bzgzs.spaceplane.client.gui.screens;

import cn.bzgzs.spaceplane.client.gui.screens.inventory.SteamEngineScreen;
import cn.bzgzs.spaceplane.world.inventory.MenuTypeList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MenuScreenManager {
	public static void register() {
		MenuScreens.register(MenuTypeList.STEAM_ENGINE.get(), SteamEngineScreen::new);
	}
}

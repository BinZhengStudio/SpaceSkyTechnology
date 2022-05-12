package cn.bzgzs.spaceplane.client.gui.screens.inventory;

import cn.bzgzs.spaceplane.world.inventory.SteamEngineMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SteamEngineScreen extends AbstractContainerScreen<SteamEngineMenu> {
	public SteamEngineScreen(SteamEngineMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float p_97788_, int p_97789_, int p_97790_) {
		// TODO
	}
}

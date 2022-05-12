package cn.bzgzs.spaceplane.client.gui.screens.inventory;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.inventory.SteamEngineMenu;
import cn.bzgzs.spaceplane.world.level.block.entity.SteamEngineBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SteamEngineScreen extends AbstractContainerScreen<SteamEngineMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpacePlane.MODID, "textures/gui/container/steam_engine.png");

	public SteamEngineScreen(SteamEngineMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(new Button(this.leftPos + 94, this.topPos + 56, 50, 20, new TranslatableComponent("button.steam_engine.water_io"), (buttonWidget) -> {
			ItemStack stack = this.menu.getSlot(1).getItem();
			if (stack.getItem() == Items.WATER_BUCKET && this.menu.getData().get(3) <= SteamEngineBlockEntity.MAX_WATER - 1000) {
				this.menu.getSlot(1).set(new ItemStack(Items.BUCKET));
				this.menu.getData().set(3, this.menu.getData().get(3) + 1000);
			} else if (stack.getItem() == Items.BUCKET && this.menu.getData().get(3) >= 1000) {
				this.menu.getSlot(1).set(new ItemStack(Items.WATER_BUCKET));
				this.menu.getData().set(3, this.menu.getData().get(3) - 1000);
			}
		}));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.getData().get(3) > 0) {
			int textureHeight = (int) (66.0F * this.menu.getData().get(3) / SteamEngineBlockEntity.MAX_WATER);
			this.blit(poseStack, this.leftPos + 149, this.topPos + 75 - textureHeight, 176, 66 - textureHeight, 17, textureHeight);
		}
	}
}
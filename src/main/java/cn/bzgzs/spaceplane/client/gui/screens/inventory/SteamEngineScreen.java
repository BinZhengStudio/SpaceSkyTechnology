package cn.bzgzs.spaceplane.client.gui.screens.inventory;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.network.client.SteamEngineSetSpeedPacket;
import cn.bzgzs.spaceplane.network.client.SteamEngineWaterIOPacket;
import cn.bzgzs.spaceplane.world.inventory.SteamEngineMenu;
import cn.bzgzs.spaceplane.world.level.block.entity.SteamEngineBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
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
	private final TranslatableComponent setSpeed = new TranslatableComponent("label.steam_engine.speed_set");

	public SteamEngineScreen(SteamEngineMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(new Button(this.leftPos + 94, this.topPos + 56, 50, 20, new TranslatableComponent("button.steam_engine.water_io"), (buttonWidget) -> {
			ItemStack stack = this.menu.getSlot(1).getItem();
			int x = this.menu.getData().get(6);
			int y = this.menu.getData().get(7);
			int z = this.menu.getData().get(8);
			if (stack.getItem() == Items.WATER_BUCKET && this.menu.getData().get(4) <= SteamEngineBlockEntity.MAX_WATER - 1000) {
				NetworkHandler.INSTANCE.sendToServer(new SteamEngineWaterIOPacket(true, 1000, new BlockPos(x, y, z)));
			} else if (stack.getItem() == Items.BUCKET && this.menu.getData().get(5) >= 1000) {
				NetworkHandler.INSTANCE.sendToServer(new SteamEngineWaterIOPacket(false, 1000, new BlockPos(x, y, z)));
			}
		}));
		int buttonX = this.leftPos + this.font.width(this.title) + 18 + this.font.width(setSpeed);
		this.addRenderableWidget(new ImageButton(buttonX, this.topPos + 4, 9, 6, 193, 0, TEXTURE, (buttonWidget) -> {
			int x = this.menu.getData().get(6);
			int y = this.menu.getData().get(7);
			int z = this.menu.getData().get(8);
			if (this.menu.getData().get(1) < SteamEngineBlockEntity.MAX_SPEED)
				NetworkHandler.INSTANCE.sendToServer(new SteamEngineSetSpeedPacket(this.menu.getData().get(1) + 1, new BlockPos(x, y, z)));
		}));
		this.addRenderableWidget(new ImageButton(buttonX, this.topPos + 11, 9, 6, 202, 0, TEXTURE, (buttonWidget) -> {
			int x = this.menu.getData().get(6);
			int y = this.menu.getData().get(7);
			int z = this.menu.getData().get(8);
			if (this.menu.getData().get(1) > 1)
				NetworkHandler.INSTANCE.sendToServer(new SteamEngineSetSpeedPacket(this.menu.getData().get(1) - 1, new BlockPos(x, y, z)));
		}));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
		TranslatableComponent speed = new TranslatableComponent("label.steam_engine.speed");
		TranslatableComponent power = new TranslatableComponent("label.steam_engine.power");
		TranslatableComponent waterAmount = new TranslatableComponent("label.steam_engine.water_amount");
		this.font.draw(poseStack, speed, this.leftPos + 8, this.topPos + 24, 0x880000);
		this.font.draw(poseStack, power, this.leftPos + 8, this.topPos + 39, 0x826d00);
		this.font.draw(poseStack, waterAmount, this.leftPos + 8, this.topPos + 54, 0x006ee4);
		this.font.draw(poseStack, Float.toString(this.menu.getData().get(0) / 100.0F), this.leftPos + 9 + this.font.width(speed), this.topPos + 24, 0x880000);
		this.font.draw(poseStack, Integer.toString(this.menu.getData().get(2)), this.leftPos + 9 + this.font.width(power), this.topPos + 39, 0x826d00);
		this.font.draw(poseStack, Integer.toString(this.menu.getData().get(5)), this.leftPos + 9 + this.font.width(waterAmount), this.topPos + 54, 0x006ee4);

		int setSpeedX = this.leftPos + this.font.width(this.title) + 10;
		this.font.draw(poseStack, setSpeed, setSpeedX, this.topPos + 6, 0x885600);
		this.font.draw(poseStack, Integer.toString(this.menu.getData().get(1)), setSpeedX + this.font.width(setSpeed) + 1, this.topPos + 6, 0x885600);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.getData().get(5) > 0) {
			int textureHeight = (int) (64.0F * this.menu.getData().get(5) / SteamEngineBlockEntity.MAX_WATER);
			this.blit(poseStack, this.leftPos + 149, this.topPos + 73 - textureHeight, 176, 64 - textureHeight, 17, textureHeight + 1);
		}
		if (this.menu.getData().get(3) > 0) {
			int totalBurnTime = this.menu.getData().get(4) > 0 ? this.menu.getData().get(4) : 200;
			int textureHeight = (int) (13.0F * this.menu.getData().get(3) / totalBurnTime);
			this.blit(poseStack, this.leftPos + 70, this.topPos + 37 - textureHeight, 176, 79 - textureHeight, 14, textureHeight + 1);
		}
	}
}

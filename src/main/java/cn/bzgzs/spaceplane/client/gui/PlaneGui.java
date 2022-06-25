package cn.bzgzs.spaceplane.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;

public class PlaneGui extends GuiComponent {
	private final Minecraft minecraft;
	private final Font font;
	private final PoseStack poseStack;
	private final boolean state;

	public PlaneGui(PoseStack poseStack, boolean state) {
		this.minecraft = Minecraft.getInstance();
		this.font = Minecraft.getInstance().font;
		this.poseStack = poseStack;
		this.state = state;
	}

	public void render() {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.font.draw(this.poseStack, this.state ? "YES" : "NO", 0,0,0xFF0000);
	}
}

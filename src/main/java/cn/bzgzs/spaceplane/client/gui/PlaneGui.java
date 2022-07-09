package cn.bzgzs.spaceplane.client.gui;

import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlaneGui extends GuiComponent {
	private final Minecraft minecraft;
	private final Font font;
	private final PoseStack poseStack;
	private final boolean state;
	private final boolean landingGear;
	private final boolean tractor;

	public PlaneGui(PoseStack poseStack, TestPlaneEntity plane) {
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
		this.poseStack = poseStack;
		this.state = plane.getEngineState();
		this.landingGear = plane.getLandingGear();
		this.tractor = plane.getTractor();
	}

	public void render() {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.font.draw(this.poseStack, this.state ? "YES" : "NO", 0,0,0xFF0000);
		this.font.draw(this.poseStack, this.landingGear ? "YES" : "NO", 0,15,0x00FF00);
		this.font.draw(this.poseStack, this.tractor ? "YES" : "NO", 0,30,0x0000FF);
	}
}

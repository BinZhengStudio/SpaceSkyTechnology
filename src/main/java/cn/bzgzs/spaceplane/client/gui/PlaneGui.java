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
	private final int power;
	private final float pitch;
	private final float yaw;
	private final float roll;

	public PlaneGui(PoseStack poseStack, TestPlaneEntity plane) {
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
		this.poseStack = poseStack;
		this.state = plane.getEngineState();
		this.landingGear = plane.getLandingGear();
		this.tractor = plane.getTractor();
		this.power = plane.getEnginePower();
		this.pitch = plane.getXRot();
		this.yaw = plane.getYRot();
		this.roll = plane.getZRot();
	}

	public void render() {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.font.draw(this.poseStack, this.state ? "YES" : "NO", 0,0,0xFF0000);
		this.font.draw(this.poseStack, this.landingGear ? "YES" : "NO", 0,15,0x00FF00);
		this.font.draw(this.poseStack, this.tractor ? "YES" : "NO", 0,30,0x0000FF);
		this.font.draw(this.poseStack, Integer.toString(this.power), 0,45,0xFFFF00);
		this.font.draw(this.poseStack, Float.toString(this.pitch), 0,60,0xFFFF00);
		this.font.draw(this.poseStack, Float.toString(this.yaw), 0,66,0xFFFF00);
		this.font.draw(this.poseStack, Float.toString(this.roll), 0,72,0xFFFF00);
	}
}

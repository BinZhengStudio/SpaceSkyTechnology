package cn.bzgzs.spaceplane.client.event;

import cn.bzgzs.spaceplane.client.gui.PlaneGui;
import cn.bzgzs.spaceplane.client.player.KeyboardInputList;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public static void keyInputEvent(InputEvent.KeyInputEvent event) {
		if (Minecraft.getInstance().player != null) {
			if (Minecraft.getInstance().player.getVehicle() instanceof TestPlaneEntity plane) {
				plane.setClientInput(KeyboardInputList.PLANE_LEFT.isDown(), KeyboardInputList.PLANE_RIGHT.isDown(), KeyboardInputList.PLANE_SPEED_UP.isDown());
				plane.setInputEngineOnActivation(KeyboardInputList.ENGINE_ON.isDown());
				plane.setInputLandingGearActivation(KeyboardInputList.LANDING_GEAR.isDown());
				if (plane.getTractor() && KeyboardInputList.SEPARATE_TRACTOR.isDown()) {
					plane.setTractor(false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void renderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (Minecraft.getInstance().player != null) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
				if (Minecraft.getInstance().player.getVehicle() instanceof TestPlaneEntity plane) {
					new PlaneGui(event.getMatrixStack(), plane).render();
				}
			}
		}
	}
}

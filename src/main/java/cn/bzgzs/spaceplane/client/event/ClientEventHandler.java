package cn.bzgzs.spaceplane.client.event;

import cn.bzgzs.spaceplane.client.player.KeyboardInputList;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public static void keyInputEvent(InputEvent.KeyInputEvent event) {
		if (KeyboardInputList.CLIMB_UP.isDown()) {
			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
				plane.climbUp(false);
			}
		} else {
			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
				plane.climbUp(true);
			}
		}
	}
}

package cn.bzgzs.spaceplane.client.event;

import cn.bzgzs.spaceplane.client.player.KeyboardInputList;
import cn.bzgzs.spaceplane.network.ClientPlaneKeyboardInputPacket;
import cn.bzgzs.spaceplane.network.NetworkHandler;
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
//		if (KeyboardInputList.CLIMB_UP.isDown()) {
//			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
//				plane.setClimbUp(true);
//			}
//		} else {
//			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
//				plane.setClimbUp(false);
//			}
//		}
//		if (KeyboardInputList.DECLINE.isDown()) {
//			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
//				plane.setDecline(true);
//			}
//		} else {
//			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity plane) {
//				plane.setDecline(false);
//			}
//		}
		if (Minecraft.getInstance().player != null) {
			if (Minecraft.getInstance().player.getVehicle() instanceof BasePlaneEntity) {
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(0, Minecraft.getInstance().options.keyUp.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(1, Minecraft.getInstance().options.keyLeft.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(2, Minecraft.getInstance().options.keyDown.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(3, Minecraft.getInstance().options.keyRight.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(4, KeyboardInputList.CLIMB_UP.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(6, KeyboardInputList.DECLINE.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(8, Minecraft.getInstance().options.keyJump.isDown()));
				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneKeyboardInputPacket(9, KeyboardInputList.ENGINE_ON.isDown()));
			}
		}
	}
}

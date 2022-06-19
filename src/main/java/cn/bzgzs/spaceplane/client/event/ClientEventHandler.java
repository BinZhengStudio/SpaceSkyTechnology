package cn.bzgzs.spaceplane.client.event;

import cn.bzgzs.spaceplane.client.player.KeyboardInputList;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public static void keyInputEvent(InputEvent.KeyInputEvent event) {
		if (Minecraft.getInstance().player != null) {
			if (Minecraft.getInstance().player.getVehicle() instanceof TestPlaneEntity plane) {
//				plane.setClientInput(0, Minecraft.getInstance().options.keyUp.isDown());
//				plane.setClientInput(1, Minecraft.getInstance().options.keyLeft.isDown());
//				plane.setClientInput(2, Minecraft.getInstance().options.keyDown.isDown());
//				plane.setClientInput(3, Minecraft.getInstance().options.keyRight.isDown());
//				plane.setClientInput(4, KeyboardInputList.CLIMB_UP.isDown());
//				plane.setClientInput(6, KeyboardInputList.DECLINE.isDown());
//				plane.setClientInput(8, Minecraft.getInstance().options.keyJump.isDown());
//				plane.setClientInput(9, KeyboardInputList.ENGINE_ON.isDown());
				plane.setClientInput(KeyboardInputList.LEFT.isDown(), KeyboardInputList.RIGHT.isDown(), Minecraft.getInstance().options.keyJump.isDown());

//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(0, Minecraft.getInstance().options.keyUp.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(1, Minecraft.getInstance().options.keyLeft.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(2, Minecraft.getInstance().options.keyDown.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(3, Minecraft.getInstance().options.keyRight.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(4, KeyboardInputList.CLIMB_UP.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(6, KeyboardInputList.DECLINE.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(8, Minecraft.getInstance().options.keyJump.isDown()));
//				NetworkHandler.INSTANCE.sendToServer(new ClientPlaneControlPacket(9, KeyboardInputList.ENGINE_ON.isDown()));
			}
		}
	}
}

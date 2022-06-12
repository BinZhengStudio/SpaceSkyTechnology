package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
	public static final String VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SpacePlane.MODID, "instance"), () -> VERSION, VERSION::equals, VERSION::equals);
	private static int id = 0;

	public static void register() {
		INSTANCE.messageBuilder(ClientSteamEngineWaterIOPacket.class, getId()).encoder(ClientSteamEngineWaterIOPacket::encode).decoder(ClientSteamEngineWaterIOPacket::new).consumer(ClientSteamEngineWaterIOPacket::consumer).add();
		INSTANCE.messageBuilder(ClientSteamEngineSetSpeedPacket.class, getId()).encoder(ClientSteamEngineSetSpeedPacket::encode).decoder(ClientSteamEngineSetSpeedPacket::new).consumer(ClientSteamEngineSetSpeedPacket::consumer).add();
		INSTANCE.messageBuilder(ClientPlaneControlPacket.class, getId()).encoder(ClientPlaneControlPacket::encode).decoder(ClientPlaneControlPacket::new).consumer(ClientPlaneControlPacket::consumer).add();
	}

	public static int getId() {
		return id++;
	}
}

package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.network.client.*;
import cn.bzgzs.spaceplane.network.server.PlaneEnginePowerSyncPacket;
import cn.bzgzs.spaceplane.network.server.PlaneRotateSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class NetworkHandler {
	public static final String VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SpacePlane.MODID, "instance"), () -> VERSION, VERSION::equals, VERSION::equals);
	private static int id = 0;

	public static void register() {
		// Sent by Client
		register(SteamEngineWaterIOPacket.class, SteamEngineWaterIOPacket::new);
		register(SteamEngineSetSpeedPacket.class, SteamEngineSetSpeedPacket::new);
		register(PlaneEnginePacket.class, PlaneEnginePacket::new);
		register(PlaneLandingGearPacket.class, PlaneLandingGearPacket::new);
		register(PlaneTractorPacket.class,PlaneTractorPacket::new);
		register(PlaneEnginePowerPacket.class, PlaneEnginePowerPacket::new);
		register(PlaneRotateChangedPacket.class, PlaneRotateChangedPacket::new);
		register(PlaneLaunchCannonballPacket.class, PlaneLaunchCannonballPacket::new);
		register(PlaneExplodePacket.class, PlaneExplodePacket::new);

		// Sent by Server
		register(PlaneRotateSyncPacket.class, PlaneRotateSyncPacket::new);
		register(PlaneEnginePowerSyncPacket.class, PlaneEnginePowerSyncPacket::new);
	}

	private static <M extends CustomPacket> void register(Class<M> packet, Function<FriendlyByteBuf, M> decoder) {
		INSTANCE.messageBuilder(packet, id++).encoder((CustomPacket::encode)).decoder(decoder).consumer((CustomPacket::consumer)).add();
	}
}

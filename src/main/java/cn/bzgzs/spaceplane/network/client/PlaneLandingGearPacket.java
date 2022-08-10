package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PlaneLandingGearPacket extends CustomPacket {
	private final boolean landingGear;

	public PlaneLandingGearPacket(FriendlyByteBuf buf) {
		this.landingGear = buf.readBoolean();
	}

	public PlaneLandingGearPacket(boolean landingGear) {
		this.landingGear = landingGear;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(landingGear);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse(serverPlayer -> {
			if (serverPlayer.getVehicle() instanceof TestPlaneEntity plane) {
				plane.setLandingGear(this.landingGear);
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

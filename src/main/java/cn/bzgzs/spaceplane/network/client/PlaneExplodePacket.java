package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PlaneExplodePacket extends CustomPacket {
	public PlaneExplodePacket(FriendlyByteBuf buf) {
	}

	public PlaneExplodePacket() {
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse((serverPlayer) -> {
			if (serverPlayer.getVehicle() instanceof BasePlaneEntity plane) {
				plane.explode();
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

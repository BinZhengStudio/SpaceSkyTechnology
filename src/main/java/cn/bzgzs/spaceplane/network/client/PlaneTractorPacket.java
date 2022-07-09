package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneTractorPacket extends CustomPacket {
	private final boolean tractor;

	public PlaneTractorPacket(FriendlyByteBuf buf) {
		this.tractor = buf.readBoolean();
	}

	public PlaneTractorPacket(boolean tractor) {
		this.tractor = tractor;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(tractor);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof TestPlaneEntity plane) {
					plane.setTractor(this.tractor);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

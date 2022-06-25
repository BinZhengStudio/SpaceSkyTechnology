package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPlaneEnginePacket {
	private final boolean engineOn;

	public ClientPlaneEnginePacket(FriendlyByteBuf buf) {
		this.engineOn = buf.readBoolean();
	}

	public ClientPlaneEnginePacket(boolean engineOn) {
		this.engineOn = engineOn;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(engineOn);
	}

	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof TestPlaneEntity plane) {
					plane.setEngineState(this.engineOn);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

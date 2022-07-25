package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PlaneEnginePacket extends CustomPacket {
	private final boolean engineOn;

	public PlaneEnginePacket(FriendlyByteBuf buf) {
		this.engineOn = buf.readBoolean();
	}

	public PlaneEnginePacket(boolean engineOn) {
		this.engineOn = engineOn;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(engineOn);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse(serverPlayer -> {
			if (serverPlayer.getVehicle() instanceof TestPlaneEntity plane) {
				plane.setEngineState(this.engineOn);
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

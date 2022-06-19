package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPlaneControlPacket {
	private final boolean left;
	private final boolean right;
	private final boolean speedUp;

	public ClientPlaneControlPacket(FriendlyByteBuf buf) {
		this.speedUp = buf.readBoolean();
		this.left = buf.readBoolean();
		this.right = buf.readBoolean();
	}

	public ClientPlaneControlPacket(boolean speedUp, boolean left, boolean right) {
		this.speedUp = speedUp;
		this.left = left;
		this.right = right;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(speedUp);
		buf.writeBoolean(left);
		buf.writeBoolean(right);
	}

	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof TestPlaneEntity plane) {
					plane.setControlState(speedUp, left, right);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

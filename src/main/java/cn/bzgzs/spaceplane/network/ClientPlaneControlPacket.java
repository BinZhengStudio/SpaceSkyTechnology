package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPlaneControlPacket {
	private final boolean left;
	private final boolean right;
	private final boolean speedUp;

	public ClientPlaneControlPacket(FriendlyByteBuf buf) {
		this.left = buf.readBoolean();
		this.right = buf.readBoolean();
		this.speedUp = buf.readBoolean();
	}

	public ClientPlaneControlPacket(boolean left, boolean right, boolean speedUp) {
		this.left = left;
		this.right = right;
		this.speedUp = speedUp;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(left);
		buf.writeBoolean(right);
		buf.writeBoolean(speedUp);
	}

	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof BasePlaneEntity plane) {
//					plane.setInput(index, speedUp);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

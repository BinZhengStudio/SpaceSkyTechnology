package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneControlPacket extends CustomPacket {
	private final boolean left;
	private final boolean right;
	private final boolean speedUp;

	public PlaneControlPacket(FriendlyByteBuf buf) {
		this.speedUp = buf.readBoolean();
		this.left = buf.readBoolean();
		this.right = buf.readBoolean();
	}

	public PlaneControlPacket(boolean speedUp, boolean left, boolean right) {
		this.speedUp = speedUp;
		this.left = left;
		this.right = right;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(speedUp);
		buf.writeBoolean(left);
		buf.writeBoolean(right);
	}

	@Override
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

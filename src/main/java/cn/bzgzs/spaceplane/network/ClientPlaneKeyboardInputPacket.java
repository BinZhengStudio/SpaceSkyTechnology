package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPlaneKeyboardInputPacket {
	private final int index;
	private final boolean isDown;

	public ClientPlaneKeyboardInputPacket(FriendlyByteBuf buf) {
		this.index = buf.readInt();
		this.isDown = buf.readBoolean();
	}

	public ClientPlaneKeyboardInputPacket(int index, boolean isDown) {
		this.index = index;
		this.isDown = isDown;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(index);
		buf.writeBoolean(isDown);
	}

	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof BasePlaneEntity plane) {
					plane.setInput(index, isDown);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

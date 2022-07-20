package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneLaunchCannonballPacket extends CustomPacket {
	private final boolean left;

	public PlaneLaunchCannonballPacket(FriendlyByteBuf buf) {
		this.left = buf.readBoolean();
	}

	public PlaneLaunchCannonballPacket(boolean left) {
		this.left = left;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(left);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof TestPlaneEntity plane) {
					plane.launchCannonBall(this.left);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

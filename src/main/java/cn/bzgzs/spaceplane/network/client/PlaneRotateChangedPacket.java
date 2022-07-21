package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneRotateChangedPacket extends CustomPacket {
	private final float pitch;
	private final float yaw;
	private final float roll;

	public PlaneRotateChangedPacket(FriendlyByteBuf buf) {
		this.pitch = buf.readFloat();
		this.yaw = buf.readFloat();
		this.roll = buf.readFloat();
	}

	public PlaneRotateChangedPacket(TestPlaneEntity plane) {
		this.pitch = plane.getXRot();
		this.yaw = plane.getYRot();
		this.roll = plane.getZRot();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeFloat(pitch);
		buf.writeFloat(yaw);
		buf.writeFloat(roll);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof TestPlaneEntity plane) {
					plane.setRot(this.pitch, this.yaw, this.roll);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

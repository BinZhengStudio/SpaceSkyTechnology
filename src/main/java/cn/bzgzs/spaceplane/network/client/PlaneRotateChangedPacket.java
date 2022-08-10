package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
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

	public PlaneRotateChangedPacket(BasePlaneEntity plane) {
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
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse(serverPlayer -> {
			if (serverPlayer.getVehicle() instanceof BasePlaneEntity plane) {
				plane.setRot(this.pitch, this.yaw, this.roll);
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

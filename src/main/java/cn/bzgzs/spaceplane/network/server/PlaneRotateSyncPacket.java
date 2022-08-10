package cn.bzgzs.spaceplane.network.server;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PlaneRotateSyncPacket extends CustomPacket {
	private final int entity;
	private final float pitch;
	private final float yaw;
	private final float roll;

	public PlaneRotateSyncPacket(FriendlyByteBuf buf) {
		this.entity = buf.readInt();
		this.pitch = buf.readFloat();
		this.yaw = buf.readFloat();
		this.roll = buf.readFloat();
	}

	public PlaneRotateSyncPacket(BasePlaneEntity entity) {
		this.entity = entity.getId();
		this.pitch = entity.getXRot();
		this.yaw = entity.getYRot();
		this.roll = entity.getZRot();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entity);
		buf.writeFloat(pitch);
		buf.writeFloat(yaw);
		buf.writeFloat(roll);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(Minecraft.getInstance().level).ifPresentOrElse(clientLevel -> {
			if (clientLevel.getEntity(this.entity) instanceof BasePlaneEntity plane) {
				plane.lerpRotate(this.pitch, this.yaw, this.roll);
			}
		}, () -> {
			throw new NullPointerException("Fuck! World is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

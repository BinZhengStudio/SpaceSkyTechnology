package cn.bzgzs.spaceplane.network.server;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class PlaneEnginePowerSyncPacket extends CustomPacket {
	private final int entity;
	private final int power;

	public PlaneEnginePowerSyncPacket(FriendlyByteBuf buf) {
		this.entity = buf.readInt();
		this.power = buf.readInt();
	}

	public PlaneEnginePowerSyncPacket(BasePlaneEntity entity) {
		this.entity = entity.getId();
		this.power = entity.getEnginePower();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entity);
		buf.writeInt(power);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(Minecraft.getInstance().level).ifPresentOrElse(clientLevel -> {
			if (clientLevel.getEntity(this.entity) instanceof BasePlaneEntity plane) {
				plane.setEnginePower(this.power);
			}
		}, () -> {
			throw new NullPointerException("Fuck! World is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

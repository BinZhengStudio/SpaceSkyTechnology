package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneEnginePowerPacket extends CustomPacket {
	private final int enginePower;

	public PlaneEnginePowerPacket(FriendlyByteBuf buf) {
		this.enginePower = buf.readInt();
	}

	public PlaneEnginePowerPacket(BasePlaneEntity entity) {
		this.enginePower = entity.getEnginePower();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.enginePower);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof BasePlaneEntity plane) {
					plane.setEnginePower(this.enginePower);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

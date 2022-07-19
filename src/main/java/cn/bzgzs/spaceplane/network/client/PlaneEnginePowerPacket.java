package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneEnginePowerPacket extends CustomPacket {
	private final int enginePower;

	public PlaneEnginePowerPacket(FriendlyByteBuf buf) {
		this.enginePower = buf.readInt();
	}

	public PlaneEnginePowerPacket(TestPlaneEntity entity) {
		this.enginePower = entity.getEnginePower();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(this.enginePower);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {

	}
}

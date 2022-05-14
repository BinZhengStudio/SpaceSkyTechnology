package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientSteamEngineSetSpeedPacket {
	private final int speed;
	private final BlockPos pos;
	public ClientSteamEngineSetSpeedPacket(FriendlyByteBuf buf) {
		this.speed = buf.readInt();
		this.pos = buf.readBlockPos();
	}

	public ClientSteamEngineSetSpeedPacket(int speed, BlockPos pos) {
		this.speed = speed;
		this.pos = pos;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(speed);
		buf.writeBlockPos(pos);
	}

	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				Level level = context.get().getSender().getLevel();
				BlockEntity blockEntity = level.getBlockEntity(pos);
				if (blockEntity instanceof SteamEngineBlockEntity) ((SteamEngineBlockEntity) blockEntity).setSpeedByScreenSlider(this.speed);
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

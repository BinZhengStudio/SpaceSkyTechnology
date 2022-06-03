package cn.bzgzs.spaceplane.network;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
				Level level = sender.getLevel();
				if (level.isLoaded(pos)) { // 检查当前BlockPos是否已加载，防止Waiting For Server，TODO 可能要服务端优化
					if (level.getBlockEntity(pos) instanceof SteamEngineBlockEntity blockEntity) blockEntity.setSpeedByScreenButton(this.speed);
				} else {
					SpacePlane.LOGGER.error("FUCK YOU! Don't try to WAITING FOR SERVER the TeaCon!");
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

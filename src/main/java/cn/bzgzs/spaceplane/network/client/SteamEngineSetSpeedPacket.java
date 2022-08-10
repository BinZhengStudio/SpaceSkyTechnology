package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.level.block.entity.SteamEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class SteamEngineSetSpeedPacket extends CustomPacket {
	private final int speed;
	private final BlockPos pos;

	public SteamEngineSetSpeedPacket(FriendlyByteBuf buf) {
		this.speed = buf.readInt();
		this.pos = buf.readBlockPos();
	}

	public SteamEngineSetSpeedPacket(int speed, BlockPos pos) {
		this.speed = speed;
		this.pos = pos;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(speed);
		buf.writeBlockPos(pos);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse(serverPlayer -> {
			Level level = serverPlayer.getLevel();
			if (level.isLoaded(pos)) { // 检查当前BlockPos是否已加载，防止Waiting For Server
				if (level.getBlockEntity(pos) instanceof SteamEngineBlockEntity blockEntity) blockEntity.setSpeedByScreenButton(this.speed);
			} else {
				SpacePlane.LOGGER.error("FUCK YOU! Don't try to WAITING FOR SERVER the TeaCon!");
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

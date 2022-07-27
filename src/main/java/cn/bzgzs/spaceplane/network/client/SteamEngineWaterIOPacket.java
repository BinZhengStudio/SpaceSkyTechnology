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

public class SteamEngineWaterIOPacket extends CustomPacket {
	private final boolean isPourIn;
	private final int amount;
	private final BlockPos pos;

	public SteamEngineWaterIOPacket(FriendlyByteBuf buf) {
		this.isPourIn = buf.readBoolean();
		this.amount = buf.readInt();
		this.pos = buf.readBlockPos();
	}

	public SteamEngineWaterIOPacket(boolean isPourIn, int amount, BlockPos pos) {
		this.isPourIn = isPourIn;
		this.amount = amount;
		this.pos = pos;
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(isPourIn);
		buf.writeInt(amount);
		buf.writeBlockPos(pos);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> Optional.ofNullable(context.get().getSender()).ifPresentOrElse(serverPlayer -> {
			Level level = serverPlayer.getLevel();
			if (level.isLoaded(pos)) { // 检查当前BlockPos是否已加载，防止Waiting For Server
				if (level.getBlockEntity(pos) instanceof SteamEngineBlockEntity blockEntity) blockEntity.waterUseBucketIO(this.isPourIn, this.amount);
			} else {
				SpacePlane.LOGGER.error("FUCK YOU! Don't try to WAITING FOR SERVER the TeaCon!");
			}
		}, () -> {
			throw new NullPointerException("Fuck! Sender is NULL!");
		}));
		context.get().setPacketHandled(true);
	}
}

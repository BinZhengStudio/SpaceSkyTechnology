package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.BasePipeBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public abstract class BasePipeBlock extends BaseEntityBlock {
	protected BasePipeBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level world, BlockEntityType<T> type1, BlockEntityType<? extends BasePipeBlockEntity> type2) {
		return world.isClientSide ? null : createTickerHelper(type1, type2, BasePipeBlockEntity::tick);
	}
}

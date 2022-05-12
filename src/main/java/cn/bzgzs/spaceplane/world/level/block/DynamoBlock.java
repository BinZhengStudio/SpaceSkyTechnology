package cn.bzgzs.spaceplane.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DynamoBlock extends BaseEntityBlock { // TODO
	protected DynamoBlock(Properties p_49224_) {
		super(p_49224_);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos p_153215_, @Nonnull BlockState p_153216_) {
		return null;
	}

	@Override
	public @Nonnull RenderShape getRenderShape(@Nonnull BlockState p_49232_) {
		return RenderShape.MODEL;
	}
}

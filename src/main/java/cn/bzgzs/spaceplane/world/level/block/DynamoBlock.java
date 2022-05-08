package cn.bzgzs.spaceplane.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamoBlock extends BaseEntityBlock { // TODO
	protected DynamoBlock(Properties p_49224_) {
		super(p_49224_);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
		return null;
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
		return RenderShape.MODEL;
	}
}

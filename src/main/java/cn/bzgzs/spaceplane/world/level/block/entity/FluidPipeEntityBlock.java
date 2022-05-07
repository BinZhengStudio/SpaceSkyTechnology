package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FluidPipeEntityBlock extends BlockEntity {
	public FluidPipeEntityBlock(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.FLUID_PIPE.get(), pos, state);
	}
}

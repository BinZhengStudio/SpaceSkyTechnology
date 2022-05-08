package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronFluidPipeBlockEntity extends BasePipeBlockEntity {

	public IronFluidPipeBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.IRON_FLUID_PIPE.get(), pos, state);
	}

	@Override
	public int getMaxFlow() {
		return 1000;
	}
}

package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlloyFurnaceBlockEntity extends BlockEntity { // TODO
	public AlloyFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.ALLOY_FURNACE.get(), pos, state);
	}
}

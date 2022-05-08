package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EngineBlockEntity extends BlockEntity {
	public EngineBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.ENGINE.get(), pos, state);
	}
}

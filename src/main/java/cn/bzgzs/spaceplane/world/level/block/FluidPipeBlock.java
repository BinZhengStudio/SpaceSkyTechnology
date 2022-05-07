package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.FluidPipeEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidPipeBlock extends BaseEntityBlock {
	protected FluidPipeBlock() {
		super(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new FluidPipeEntityBlock(pos, state);
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
		return RenderShape.MODEL;
	}
}

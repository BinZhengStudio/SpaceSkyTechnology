package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.BlockEntityTypeList;
import cn.bzgzs.spaceplane.world.level.block.entity.IronFluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class IronFluidPipeBlock extends BasePipeBlock {
	protected IronFluidPipeBlock() {
		super(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level world, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createTicker(world, type, BlockEntityTypeList.IRON_FLUID_PIPE.get());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new IronFluidPipeBlockEntity(pos, state);
	}

	@Override
	public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}
}

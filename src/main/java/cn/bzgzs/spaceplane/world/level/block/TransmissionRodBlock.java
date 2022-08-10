package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.TransmissionRodBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TransmissionRodBlock extends BaseEntityBlock {
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	protected TransmissionRodBlock() {
		super(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL));
		this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TransmissionRodBlockEntity(pos, state);
	}

	@Override
	public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}

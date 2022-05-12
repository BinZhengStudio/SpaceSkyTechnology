package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.TransmissionRodBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransmissionRodBlock extends BaseEntityBlock {
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS; // TODO 待添加至stateDefinition

	protected TransmissionRodBlock() {
		super(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TransmissionRodBlockEntity(pos, state);
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
		return RenderShape.MODEL;
	}
}

package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.world.level.block.entity.AlloyFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class AlloyFurnaceBlock extends BaseEntityBlock {
	public AlloyFurnaceBlock() {
		super(Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new AlloyFurnaceBlockEntity(pos, state);
	}

	@Override
	public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}
}

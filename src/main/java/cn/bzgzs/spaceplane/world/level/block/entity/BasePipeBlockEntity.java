package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BasePipeBlockEntity extends BlockEntity {
	public int flow;
	public LazyOptional<IFluidStorage> storage = LazyOptional.of(() -> new IFluidStorage() {
		@Override
		public int getMaxFluidStored() {
			return 0;
		}

		@Override
		public int getFluidStored() {
			return 0;
		}

		@Override
		public int receiveFluid(int amount, boolean simulate) {
			return 0;
		}

		@Override
		public int extractFluid(int amount, boolean simulate) {
			return 0;
		}
	});

	public BasePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, BasePipeBlockEntity blockEntity) {
	}

	@Override
	public void load(CompoundTag tag) {
		this.flow = tag.getInt("Flow");
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putInt("Flow", this.flow);
		super.saveAdditional(tag);
	}

	public abstract int getMaxFlow();
}

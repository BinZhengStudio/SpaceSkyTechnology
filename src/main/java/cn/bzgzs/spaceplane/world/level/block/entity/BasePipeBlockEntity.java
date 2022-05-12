package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public abstract class BasePipeBlockEntity extends BlockEntity {
	public int flow;
	public LazyOptional<IFluidHandler> handler = LazyOptional.of(() -> new IFluidHandler() { // TODO
		@Override
		public int getTanks() {
			return 1;
		}

		@Nonnull
		@Override
		public FluidStack getFluidInTank(int tank) {
			return null;
		}

		@Override
		public int getTankCapacity(int tank) {
			return 0;
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
			return true;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}

		@Nonnull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return null;
		}

		@Nonnull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return null;
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

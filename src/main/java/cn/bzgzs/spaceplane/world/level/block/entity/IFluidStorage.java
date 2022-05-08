package cn.bzgzs.spaceplane.world.level.block.entity;

public interface IFluidStorage {
	int getMaxFluidStored();
	int getFluidStored();
	int receiveFluid(int amount, boolean simulate);
	int extractFluid(int amount, boolean simulate);
}

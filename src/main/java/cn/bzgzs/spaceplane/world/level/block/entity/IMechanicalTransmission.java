package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMechanicalTransmission {
	int getSpeed();
	boolean isSource(); // TODO 可能不用
	BlockEntity getSource();
}

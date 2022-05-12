package cn.bzgzs.spaceplane.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface IMechanicalTransmission {
	/**
	 * 获取转动角速度，单位为rad/s（1s=20tick）
	 * @return 该部件转动的角速度
	 */
	int getAngularVelocity();

	/**
	 * 获取扭力，无单位，仅限输出源需要设置
	 * @return 扭力
	 */
	float getTorque();

	/**
	 * 获取阻力数值，无单位，仅限输入源设置
	 * @return 阻力
	 */
	float getResistance();

	/**
	 * 是否为输出源
	 */
	boolean isSource();

	/**
	 * 是否是传动杆
	 */
	boolean isRod();

	/**
	 * 获取输出源，允许无输入源（例如未连接输入源的传动轴）
	 * @return 输出源
	 */
	@Nullable
	BlockEntity getSource();
}

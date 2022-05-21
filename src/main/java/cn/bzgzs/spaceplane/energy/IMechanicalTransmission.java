package cn.bzgzs.spaceplane.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface IMechanicalTransmission {
	/**
	 * 获取转速，单位为rps（1s=20tick）
	 * @return 每秒转动圈数
	 */
	float getSpeed();

	void setSpeed(float speed);

	/**
	 * 获取功率，单位TE(ThreebodyEnergy)/s，仅限输出源需要设置
	 * @return 功率
	 */
	int getPower();

	void setPower(int power);

	/**
	 * 获取扭力，无单位
	 * @return 扭力
	 */
	default float getTorque() {
		return (float) getPower() / getSpeed(); // TODO 可能产生问题
	};

	/**
	 * 获取阻力数值，无单位。输出源也要设置，以处理多个输出源并联且速度不同的情况
	 * 可能会因为工作量问题而不使用这个方法
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

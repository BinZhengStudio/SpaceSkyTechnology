package cn.bzgzs.spaceplane.util;

import net.minecraft.world.phys.Vec3;

public class Vec3Helper {
	/**
	 * 用来执行向量加减法。<br>
	 * 原版的Vec3已提供此方法，但零向量的加减会出现bug。<br>
	 *
	 * @param v1 第一个向量
	 * @param v2 第二个向量
	 * @return 相加结果
	 */
	public static Vec3 add(Vec3 v1, Vec3 v2) {
		return new Vec3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	/**
	 * 用来执行向量加减法。<br>
	 * 原版的Vec3已提供此方法，但零向量的加减会出现bug。<br>
	 * @param v1 第一个向量
	 * @param x 第二个向量的x值
	 * @param y 第二个向量的y值
	 * @param z 第二个向量的z值
	 * @return 相加结果
	 */
	public static Vec3 add(Vec3 v1, double x, double y, double z) {
		return new Vec3(v1.x + x, v1.y + y, v1.z + z);
	}

	/**
	 * 用来执行向量加减法。<br>
	 * 原版的Vec3已提供此方法，但零向量的加减会出现bug。<br>
	 * @param x1 第一个向量的x值
	 * @param y1 第一个向量的y值
	 * @param z1 第一个向量的z值
	 * @param x2 第二个向量的x值
	 * @param y2 第二个向量的y值
	 * @param z2 第二个向量的z值
	 * @return 相加结果
	 */
	public static Vec3 add(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Vec3(x1 + x2, y1 + y2, z1 + z2);
	}
}

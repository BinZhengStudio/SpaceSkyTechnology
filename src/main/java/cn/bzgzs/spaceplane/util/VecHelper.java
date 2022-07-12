package cn.bzgzs.spaceplane.util;

import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.world.phys.Vec3;

public class VecHelper {
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
	 *
	 * @param v1 第一个向量
	 * @param x  第二个向量的x值
	 * @param y  第二个向量的y值
	 * @param z  第二个向量的z值
	 * @return 相加结果
	 */
	public static Vec3 add(Vec3 v1, double x, double y, double z) {
		return new Vec3(v1.x + x, v1.y + y, v1.z + z);
	}

	/**
	 * 用来执行向量加减法。<br>
	 * 原版的Vec3已提供此方法，但零向量的加减会出现bug。<br>
	 *
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

	/**
	 * 获取向量1在向量2方向上的投影长度的平方
	 *
	 * @param v1 向量1
	 * @param v2 向量2
	 * @return 投影长度的平方
	 */
	public static double projectionLengthSqr(Vec3 v1, Vec3 v2) {
		double a = v1.length();
		double b = v2.length();
		double c = v1.distanceTo(v2); // v1和v2所夹形成三角形的第三边长度
		// 海伦-秦九韶公式
		double p = (a + b + c) / 2.0D;
		double sSqr = p * (p - a) * (p - b) * (p - c); // v1和v2所夹形成三角形的面积的平方
		double hSqr = 4 * sSqr / v2.lengthSqr(); // v2.lengthSqr()等价于b * b
		return v1.lengthSqr() - hSqr; // v1.lengthSqr()等价于a * a
	}

	/**
	 * 获取向量1在向量2方向上的投影长度
	 *
	 * @param v1 向量1
	 * @param v2 向量2
	 * @return 投影长度
	 */
	public static double projectionLength(Vec3 v1, Vec3 v2) {
		return Math.sqrt(projectionLengthSqr(v1, v2));
	}

	public static Vec3d calcResistance(Vec3 motion, Vec3 res) {
		return calcResistance(motion, res.x, res.y, res.z);
	}

	public static Vec3d calcResistance(Vec3 motion, double resX, double resY, double resZ) {
		if (motion.x >= 0 && motion.x - Math.abs(resX) < 0) resX = motion.x;
		if (motion.x < 0 && motion.x + Math.abs(resX) > 0) resX = motion.x;
		if (motion.y >= 0 && motion.y - Math.abs(resY) < 0) resY = motion.y;
		if (motion.y < 0 && motion.y + Math.abs(resY) > 0) resY = motion.y;
		if (motion.z >= 0 && motion.z - Math.abs(resZ) < 0) resZ = motion.z;
		if (motion.z < 0 && motion.z + Math.abs(resZ) > 0) resZ = motion.z;
		return new Vec3d(-resX, -resY, -resZ);
	}
}

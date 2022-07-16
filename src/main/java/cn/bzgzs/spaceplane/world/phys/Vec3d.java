package cn.bzgzs.spaceplane.world.phys;

import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Vec3d extends Vec3 {
	public Vec3d(double x, double y, double z) {
		super(x, y, z);
	}

	public Vec3d(Vector3f pFloatVector) {
		super(pFloatVector);
	}

	@Override
	public Vec3d add(Vec3 vec) {
		return this.add(vec.x, vec.y, vec.z);
	}

	public Vec3d add(double x, double y, double z) {
		return new Vec3d(this.x + x, this.y + y, this.z + z);
	}

	@Override
	public Vec3d xRot(float pitch) {
		float f = Mth.cos(pitch);
		float f1 = Mth.sin(pitch);
		double d1 = this.y * (double)f + this.z * (double)f1;
		double d2 = this.z * (double)f - this.y * (double)f1;
		return new Vec3d(this.x, d1, d2);
	}

	@Override
	public Vec3d yRot(float yaw) {
		float f = Mth.cos(yaw);
		float f1 = Mth.sin(yaw);
		double d0 = this.x * (double)f + this.z * (double)f1;
		double d2 = this.z * (double)f - this.x * (double)f1;
		return new Vec3d(d0, this.y, d2);
	}

	@Override
	public Vec3d zRot(float roll) {
		float f = Mth.cos(roll);
		float f1 = Mth.sin(roll);
		double d0 = this.x * (double)f + this.y * (double)f1;
		double d1 = this.y * (double)f - this.x * (double)f1;
		return new Vec3d(d0, d1, this.z);
	}

	public Vec3d xRot(double pitch) {
		double f = Math.cos(pitch);
		double f1 = Math.sin(pitch);
		double d1 = this.y * f + this.z * f1;
		double d2 = this.z * f - this.y * f1;
		return new Vec3d(this.x, d1, d2);
	}

	public Vec3d yRot(double yaw) {
		double f = Math.cos(yaw);
		double f1 = Math.sin(yaw);
		double d0 = this.x * f + this.z * f1;
		double d2 = this.z * f - this.x * f1;
		return new Vec3d(d0, this.y, d2);
	}

	public Vec3d zRot(double roll) {
		double f = Math.cos(roll);
		double f1 = Math.sin(roll);
		double d0 = this.x * f + this.y * f1;
		double d1 = this.y * f - this.x * f1;
		return new Vec3d(d0, d1, this.z);
	}
}

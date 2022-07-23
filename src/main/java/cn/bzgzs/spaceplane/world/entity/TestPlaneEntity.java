package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.network.client.*;
import cn.bzgzs.spaceplane.network.server.PlaneRotateSyncPacket;
import cn.bzgzs.spaceplane.sounds.SoundEventList;
import cn.bzgzs.spaceplane.util.VecHelper;
import cn.bzgzs.spaceplane.world.item.ItemList;
import cn.bzgzs.spaceplane.world.phys.Vec3d;
import net.minecraft.BlockUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestPlaneEntity extends BasePlaneEntity {
	public TestPlaneEntity(EntityType<?> type, Level world) {
		super(type, world);
		this.defineParts();
		this.noPhysics = true;
	}

	//	public TestPlaneEntity(Level world, double x, double y, double z) {
//		this(EntityTypeList.TEST.get(), world);
//		this.setPos(x, y, z);
//		this.xo = x;
//		this.yo = y;
//		this.zo = z;
//	}

	@Override
	protected double getMaxAccel() {
		return 1.0D;
	}

	@Override
	protected double getHorizontalRidingOffset() {
		return 2.0D;
	}

	@Override
	public double getPassengersRidingOffset() {
		return -0.81D;
	}

	@Override
	protected double getMaxDeltaRotate() {
		return 5.0D;
	}

	@Override
	protected double getGroundLiftFactor() {
		return 0.017777777777777778D;
	}

	@Override
	protected double getGravity() {
		return 0.04D;
	}

	@Override
	protected double getStandRes() {
		return 0.2D;
	}

	@Override
	protected double getLieRes() {
		return 1.0D;
	}

	@Override
	protected double getLieRotateRes() {
		return 0.6D;
	}

	@Override
	protected double getTakeOffSpeed() {
		return 1.5D;
	}

	@Override
	protected double getAirLiftFactor() {
		return 0.008888888888888888D;
	}

	@Override
	protected double getAirResFactor() {
		return 0.04D;
	}

	@Override
	protected double getAirRotateRes() {
		return 0.9D;
	}

	public void defineParts() {
		// 头
		this.parts.add(new PlanePart(this, 0, -13, 52, Part.HEAD, 8));
		this.parts.add(new PlanePart(this, -4, -17, 44, Part.HEAD, 8));
		this.parts.add(new PlanePart(this, -4, -9, 44, Part.HEAD, 8));
		this.parts.add(new PlanePart(this, 4, -17, 44, Part.HEAD, 8));
		this.parts.add(new PlanePart(this, 4, -9, 44, Part.HEAD, 8));
		// 身内右下
		this.parts.add(new PlanePart(this, -4, -17, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -17, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -17, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -17, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -17, -4, Part.BODY, 8));
		// 身内右上
		this.parts.add(new PlanePart(this, -4, -9, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -9, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -9, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -9, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -4, -9, -4, Part.BODY, 8));
		// 身外右下
		this.parts.add(new PlanePart(this, -12, -17, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -17, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -17, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -17, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -17, -4, Part.BODY, 8));
		// 身外右上
		this.parts.add(new PlanePart(this, -12, -9, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -9, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -9, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -9, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -12, -9, -4, Part.BODY, 8));
		// 身外外右下
		this.parts.add(new PlanePart(this, -20, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -20, -17, -4, Part.BODY, 8));
		// 身外外右上
		this.parts.add(new PlanePart(this, -20, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, -20, -9, -4, Part.BODY, 8));
		// 身内左下
		this.parts.add(new PlanePart(this, 4, -17, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -17, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -17, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -17, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -17, -4, Part.BODY, 8));
		// 身内左上
		this.parts.add(new PlanePart(this, 4, -9, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -9, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -9, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -9, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 4, -9, -4, Part.BODY, 8));
		// 身外左下
		this.parts.add(new PlanePart(this, 12, -17, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -17, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -17, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -17, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -17, -4, Part.BODY, 8));
		// 身外左上
		this.parts.add(new PlanePart(this, 12, -9, 36, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -9, 28, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -9, 20, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -9, 12, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 12, -9, -4, Part.BODY, 8));
		// 身外外左下
		this.parts.add(new PlanePart(this, 20, -17, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 20, -17, -4, Part.BODY, 8));
		// 身外外左上
		this.parts.add(new PlanePart(this, 20, -9, 4, Part.BODY, 8));
		this.parts.add(new PlanePart(this, 20, -9, -4, Part.BODY, 8));
		// 引内右下
		this.parts.add(new PlanePart(this, -4, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -17, -52, Part.ENGINE, 8));
		// 引内右上
		this.parts.add(new PlanePart(this, -4, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -4, -9, -52, Part.ENGINE, 8));
		// 引外右下
		this.parts.add(new PlanePart(this, -12, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -17, -52, Part.ENGINE, 8));
		// 引外右上
		this.parts.add(new PlanePart(this, -12, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -12, -9, -52, Part.ENGINE, 8));
		// 引外外右下
		this.parts.add(new PlanePart(this, -20, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -17, -52, Part.ENGINE, 8));
		// 引外外右上
		this.parts.add(new PlanePart(this, -20, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, -20, -9, -52, Part.ENGINE, 8));
		// 引内左下
		this.parts.add(new PlanePart(this, 4, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -17, -52, Part.ENGINE, 8));
		// 引内左上
		this.parts.add(new PlanePart(this, 4, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 4, -9, -52, Part.ENGINE, 8));
		// 引外左下
		this.parts.add(new PlanePart(this, 12, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -17, -52, Part.ENGINE, 8));
		// 引外左上
		this.parts.add(new PlanePart(this, 12, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 12, -9, -52, Part.ENGINE, 8));
		// 引外外左下
		this.parts.add(new PlanePart(this, 20, -17, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -17, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -17, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -17, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -17, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -17, -52, Part.ENGINE, 8));
		// 引外外左上
		this.parts.add(new PlanePart(this, 20, -9, -12, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -9, -20, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -9, -28, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -9, -36, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -9, -44, Part.ENGINE, 8));
		this.parts.add(new PlanePart(this, 20, -9, -52, Part.ENGINE, 8));

		this.parts.add(new PlanePart(this, -27, -8, -1, Part.LEFT_FRONT_WING, 8));
		this.parts.add(new PlanePart(this, -27, -8, 7, Part.LEFT_FRONT_WING, 8));
		this.parts.add(new PlanePart(this, -35, -8, -1, Part.LEFT_FRONT_WING, 8));

		this.parts.add(new PlanePart(this, 27, -8, -1, Part.RIGHT_FRONT_WING, 8));
		this.parts.add(new PlanePart(this, 27, -8, 7, Part.RIGHT_FRONT_WING, 8));
		this.parts.add(new PlanePart(this, 35, -8, -1, Part.RIGHT_FRONT_WING, 8));
		// 翼左内
		this.parts.add(new PlanePart(this, -27, -8, -25, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -27, -8, -33, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -27, -8, -41, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -27, -8, -49, Part.LEFT_WING, 8));
		// 翼左外
		this.parts.add(new PlanePart(this, -35, -8, -33, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -35, -8, -41, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -35, -8, -49, Part.LEFT_WING, 8));
		// 翼左外外
		this.parts.add(new PlanePart(this, -43, -8, -41, Part.LEFT_WING, 8));
		this.parts.add(new PlanePart(this, -43, -8, -49, Part.LEFT_WING, 8));
		// 翼左外外外
		this.parts.add(new PlanePart(this, -49, -8, -49, Part.LEFT_WING, 8));
		// 翼右内
		this.parts.add(new PlanePart(this, 27, -8, -25, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 27, -8, -33, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 27, -8, -41, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 27, -8, -49, Part.RIGHT_WING, 8));
		// 翼右外
		this.parts.add(new PlanePart(this, 35, -8, -33, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 35, -8, -41, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 35, -8, -49, Part.RIGHT_WING, 8));
		// 翼右外外
		this.parts.add(new PlanePart(this, 43, -8, -41, Part.RIGHT_WING, 8));
		this.parts.add(new PlanePart(this, 43, -8, -49, Part.RIGHT_WING, 8));
		// 翼右外外外
		this.parts.add(new PlanePart(this, 49, -8, -49, Part.RIGHT_WING, 8));
		// 舵左下下
		this.parts.add(new PlanePart(this, -18, -1, -37, Part.LEFT_RUDDER, 8));
		this.parts.add(new PlanePart(this, -18, -1, -45, Part.LEFT_RUDDER, 8));
		this.parts.add(new PlanePart(this, -18, -1, -53, Part.LEFT_RUDDER, 8));
		// 舵左下
		this.parts.add(new PlanePart(this, -18, 5, -37, Part.LEFT_RUDDER, 8));
		this.parts.add(new PlanePart(this, -18, 5, -45, Part.LEFT_RUDDER, 8));
		this.parts.add(new PlanePart(this, -18, 5, -53, Part.LEFT_RUDDER, 8));
		// 舵左上
		this.parts.add(new PlanePart(this, -18, 11, -45, Part.LEFT_RUDDER, 8));
		this.parts.add(new PlanePart(this, -18, 11, -53, Part.LEFT_RUDDER, 8));
		// 舵右下下
		this.parts.add(new PlanePart(this, 18, -1, -37, Part.RIGHT_RUDDER, 8));
		this.parts.add(new PlanePart(this, 18, -1, -45, Part.RIGHT_RUDDER, 8));
		this.parts.add(new PlanePart(this, 18, -1, -53, Part.RIGHT_RUDDER, 8));
		// 舵右下
		this.parts.add(new PlanePart(this, 18, 5, -37, Part.RIGHT_RUDDER, 8));
		this.parts.add(new PlanePart(this, 18, 5, -45, Part.RIGHT_RUDDER, 8));
		this.parts.add(new PlanePart(this, 18, 5, -53, Part.RIGHT_RUDDER, 8));
		// 舵右上
		this.parts.add(new PlanePart(this, 18, 11, -45, Part.RIGHT_RUDDER, 8));
		this.parts.add(new PlanePart(this, 18, 11, -53, Part.RIGHT_RUDDER, 8));
		// 风挡左下
		this.parts.add(new PlanePart(this, -4, -1, 14, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, -1, 22, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, -1, 30, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, -1, 38, Part.WINDSHIELD, 8));
		// 风挡左上
		this.parts.add(new PlanePart(this, -4, 1, 13, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, 1, 21, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, 1, 29, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, -4, 1, 37, Part.WINDSHIELD, 8));
		// 风挡右下
		this.parts.add(new PlanePart(this, 4, -1, 14, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, -1, 22, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, -1, 30, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, -1, 38, Part.WINDSHIELD, 8));
		// 风挡右上
		this.parts.add(new PlanePart(this, 4, 1, 13, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, 1, 21, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, 1, 29, Part.WINDSHIELD, 8));
		this.parts.add(new PlanePart(this, 4, 1, 37, Part.WINDSHIELD, 8));

		this.parts.add(new PlanePart(this, 0, -24, 19, Part.FRONT_LANDING_GEAR, 8));
		this.parts.add(new PlanePart(this, -10, -24, -24, Part.LEFT_LANDING_GEAR, 8));
		this.parts.add(new PlanePart(this, 10, -24, -24, Part.RIGHT_LANDING_GEAR, 8));
		this.parts.add(new PlanePart(this, 0, -31, 19, Part.FRONT_LANDING_GEAR_WHEEL, 6));
		this.parts.add(new PlanePart(this, -10, -31, -24, Part.LEFT_LANDING_GEAR_WHEEL, 6));
		this.parts.add(new PlanePart(this, 10, -31, -24, Part.RIGHT_LANDING_GEAR_WHEEL, 6));
	}
}

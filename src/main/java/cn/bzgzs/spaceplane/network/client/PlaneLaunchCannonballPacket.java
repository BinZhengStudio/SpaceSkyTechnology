package cn.bzgzs.spaceplane.network.client;

import cn.bzgzs.spaceplane.network.CustomPacket;
import cn.bzgzs.spaceplane.world.entity.BasePlaneEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaneLaunchCannonballPacket extends CustomPacket {
	private final boolean left;
	private final double speedX;
	private final double speedY;
	private final double speedZ;
	private final float pitch;
	private final float yaw;
	private final float roll;

	public PlaneLaunchCannonballPacket(FriendlyByteBuf buf) {
		this.left = buf.readBoolean();
		this.speedX = buf.readDouble();
		this.speedY = buf.readDouble();
		this.speedZ = buf.readDouble();
		this.pitch = buf.readFloat();
		this.yaw = buf.readFloat();
		this.roll = buf.readFloat();
	}

	public PlaneLaunchCannonballPacket(boolean left, BasePlaneEntity plane) {
		this.left = left;
		this.speedX = plane.getDeltaMovement().x;
		this.speedY = plane.getDeltaMovement().y;
		this.speedZ = plane.getDeltaMovement().z;
		this.pitch = plane.getXRot();
		this.yaw = plane.getYRot();
		this.roll = plane.getZRot();
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(left);
		buf.writeDouble(speedX);
		buf.writeDouble(speedY);
		buf.writeDouble(speedZ);
		buf.writeFloat(pitch);
		buf.writeFloat(yaw);
		buf.writeFloat(roll);
	}

	@Override
	public void consumer(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer sender = context.get().getSender();
			if (sender != null) {
				if (sender.getVehicle() instanceof BasePlaneEntity plane) {
					plane.launchCannonBall(this.left, new Vec3(this.speedX, this.speedY, this.speedZ), this.pitch, this.yaw, this.roll);
				}
			} else {
				throw new NullPointerException("Fuck! Sender is NULL!");
			}
		});
		context.get().setPacketHandled(true);
	}
}

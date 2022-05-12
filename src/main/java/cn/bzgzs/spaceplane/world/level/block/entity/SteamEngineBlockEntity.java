package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.energy.IMechanicalTransmission;
import cn.bzgzs.spaceplane.world.level.block.SteamEngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SteamEngineBlockEntity extends BlockEntity {
	private int speed, torque;
	private final LazyOptional<IMechanicalTransmission> transmission = LazyOptional.of(() -> new IMechanicalTransmission() {
		@Override
		public int getAngularVelocity() {
			return SteamEngineBlockEntity.this.speed;
		}

		@Override
		public float getTorque() {
			return SteamEngineBlockEntity.this.torque;
		}

		@Override
		public float getResistance() {
			return 0;
		}

		@Override
		public boolean isSource() {
			return true;
		}

		@Override
		public boolean isRod() {
			return false;
		}

		@Override
		public BlockEntity getSource() {
			return SteamEngineBlockEntity.this;
		}
	});


	public SteamEngineBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.STEAM_ENGINE.get(), pos, state);
	}

	public static void serverTick(Level world, BlockPos pos, BlockState state, SteamEngineBlockEntity entity) {

	}

	// TODO：物品、流体存储

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		boolean correctDirection = side == this.getBlockState().getValue(SteamEngineBlock.FACING);
		boolean correctCapability = Objects.equals(cap, CapabilityList.MECHANICAL_TRANSMISSION);
		return correctCapability && correctDirection ? this.transmission.cast() : super.getCapability(cap, side);
	}
}

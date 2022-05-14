package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.energy.IMechanicalTransmission;
import cn.bzgzs.spaceplane.world.level.block.TransmissionRodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TransmissionRodBlockEntity extends BlockEntity {
	private int speed,torque;
	private BlockEntity engine;
	private final LazyOptional<IMechanicalTransmission> transmission = LazyOptional.of(() -> new IMechanicalTransmission() {
		@Override
		public int getSpeed() {
			return TransmissionRodBlockEntity.this.speed;
		}

		@Override
		public float getTorque() {
			return TransmissionRodBlockEntity.this.torque;
		}

		@Override
		public float getResistance() {
			return 0;
		}

		@Override
		public boolean isSource() {
			return false;
		}

		@Override
		public boolean isRod() {
			return true;
		}

		@Override
		public BlockEntity getSource() {
			return TransmissionRodBlockEntity.this.engine;
		}
	});

	public TransmissionRodBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.TRANSMISSION_ROD.get(), pos, state);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		boolean correctDirection = side.getAxis() == this.getBlockState().getValue(TransmissionRodBlock.AXIS);
		boolean correctCapability = cap == CapabilityList.MECHANICAL_TRANSMISSION;
		return correctCapability && correctDirection ? this.transmission.cast() : super.getCapability(cap, side);
	}
}

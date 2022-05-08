package cn.bzgzs.spaceplane.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransmissionRodBlockEntity extends BlockEntity {
	private int speed;
	private BlockEntity engine;
	private final LazyOptional<IMechanicalTransmission> transmission = LazyOptional.of(() -> new IMechanicalTransmission() {
		@Override
		public int getSpeed() {
			return TransmissionRodBlockEntity.this.speed;
		}

		@Override
		public boolean isSource() {
			return false;
		}

		@Override
		public BlockEntity getSource() {
			return TransmissionRodBlockEntity.this.engine;
		}
	});

	public TransmissionRodBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypeList.TRANSMISSION_ROD.get(), pos, state);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		boolean correctDirection = side.getAxis() == this.getBlockState().getValue(BlockStateProperties.AXIS);
		boolean correctCapability = cap == CapabilityList.MECHANICAL_TRANSMISSION;
		return correctCapability && correctDirection ? this.transmission.cast() : super.getCapability(cap, side);
	}
}

package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.energy.IMechanicalTransmission;
import cn.bzgzs.spaceplane.world.inventory.SteamEngineMenu;
import cn.bzgzs.spaceplane.world.level.block.SteamEngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SteamEngineBlockEntity extends BaseContainerBlockEntity {
	private int speed, torque, burnTime, waterAmount;
	public static final int MAX_SPEED = 15, MAX_TORQUE = 10, MAX_WATER = 4000;
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	private final ContainerData data = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> speed;
				case 1 -> torque;
				case 2 -> burnTime;
				case 3 -> waterAmount;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> speed = value;
				case 1 -> torque = value;
				case 2 -> burnTime = value;
				case 3 -> waterAmount = value;
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	};
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

	public static void serverTick(Level world, BlockPos pos, BlockState state, SteamEngineBlockEntity blockEntity) {
		// TODO
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.steam_engine");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new SteamEngineMenu(id, inventory, this, this.data);
	}

	@Override
	public int getContainerSize() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return this.inventory.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		return ContainerHelper.removeItem(this.inventory, index, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.inventory, index);
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		this.inventory.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clearContent() {
		this.inventory.clear();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		boolean correctDirection = side == this.getBlockState().getValue(SteamEngineBlock.FACING);
		boolean correctCapability = Objects.equals(cap, CapabilityList.MECHANICAL_TRANSMISSION);
		return correctCapability && correctDirection ? this.transmission.cast() : super.getCapability(cap, side);
	}
}

package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.energy.IMechanicalTransmission;
import cn.bzgzs.spaceplane.world.inventory.SteamEngineMenu;
import cn.bzgzs.spaceplane.world.level.block.SteamEngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SteamEngineBlockEntity extends BaseContainerBlockEntity {
	// 转速、设定的转速、功率、燃烧时间、总时间、水量、tick数（使每12tick才减waterAmount一次）
	private int speed, setSpeed, power, burnTime, totalBurnTime, waterAmount, shrinkTick;
	public static final int MAX_SPEED = 15, MAX_POWER = 150, MAX_WATER = 4000; // 最大转速，最大功率，最大水量
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	private final ContainerData data = new ContainerData() { // 这个用于向客户端发送服务端的相关据数据
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> speed;
				case 1 -> setSpeed;
				case 2 -> power;
				case 3 -> burnTime;
				case 4 -> totalBurnTime;
				case 5 -> waterAmount;
				case 6 -> SteamEngineBlockEntity.this.getBlockPos().getX();
				case 7 -> SteamEngineBlockEntity.this.getBlockPos().getY();
				case 8 -> SteamEngineBlockEntity.this.getBlockPos().getZ();
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) { // 这个是让在服务端的Menu中也能更改BlockEntity的数据
			switch (index) {
				case 0:
					speed = value;
					break;
				case 1:
					setSpeed = value;
					break;
				case 2:
					power = value;
					break;
				case 3:
					burnTime = value;
					break;
				case 4:
					totalBurnTime = value;
					break;
				case 5:
					waterAmount = value;
					break;
				case 6: // BlockPos不能更改
				case 7:
				case 8:
					break;
			}
		}

		@Override
		public int getCount() {
			return 9;
		}
	};

	private final LazyOptional<IMechanicalTransmission> transmission = LazyOptional.of(() -> new IMechanicalTransmission() {
		@Override
		public int getSpeed() {
			return SteamEngineBlockEntity.this.speed;
		}

		@Override
		public void setSpeed(int speed) {
			SteamEngineBlockEntity.this.setSpeed = speed;
		}

		@Override
		public int getPower() {
			return SteamEngineBlockEntity.this.power;
		}

		@Override
		public void setPower(int power) {
			SteamEngineBlockEntity.this.power = power;
		}

		@Override
		public float getResistance() {
			return 1.0F;
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
		boolean flag = false; // 是否需要setChanged

		if (blockEntity.isLit()) {
			--blockEntity.burnTime;
			flag = true;
		}

		if (blockEntity.hasWater() && blockEntity.isLit()) {
			if (blockEntity.shrinkTick <= 0) {
				--blockEntity.waterAmount;
				blockEntity.shrinkTick = 12; // 每12tick减一次waterAmount，这样水不会少的太快
			} else {
				--blockEntity.shrinkTick;
			}
			flag = true;
		}

		if (!blockEntity.isLit() && blockEntity.hasWater()) { // 如果没有燃烧，并且有水，则消耗燃料并燃烧
			ItemStack stack = blockEntity.inventory.get(0);
			int time = ForgeHooks.getBurnTime(stack, null);
			if (time > 0) {
				stack.shrink(1);
				blockEntity.burnTime = time;
				blockEntity.totalBurnTime = time;
				flag = true;
			}
		}

		if (blockEntity.isLit() && blockEntity.hasWater()) { // 输出能量
			if (blockEntity.power < MAX_POWER) {
				++blockEntity.power;
				blockEntity.speedAndPowerChanged();
				flag = true;
			}
			if (blockEntity.setSpeed <= MAX_SPEED && blockEntity.setSpeed > 0) { // 检查设定的转速是否正确
				if (blockEntity.speed < blockEntity.setSpeed) {
					++blockEntity.speed;
					blockEntity.speedAndPowerChanged();
					flag = true;
				} else if (blockEntity.speed > blockEntity.setSpeed) {
					--blockEntity.speed;
					blockEntity.speedAndPowerChanged();
					flag = true;
				}
			} else if (blockEntity.speed < MAX_SPEED) {
				++blockEntity.speed;
				blockEntity.speedAndPowerChanged();
				flag = true;
			}
		} else {
			if (blockEntity.power > 0) {
				--blockEntity.power;
				if (blockEntity.speed < blockEntity.setSpeed) {
					++blockEntity.speed;
				} else if (blockEntity.speed > blockEntity.setSpeed) {
					--blockEntity.speed;
				}
				blockEntity.speedAndPowerChanged();
				flag = true;
			}
			if (blockEntity.speed > 0 && blockEntity.power <= 0) {
				--blockEntity.speed;
				blockEntity.speedAndPowerChanged();
				flag = true;
			}
		}

		if (blockEntity.isLit() != state.getValue(SteamEngineBlock.LIT)) { // 如果燃烧状态与state不符，则更新state
			world.setBlock(pos, state.setValue(SteamEngineBlock.LIT, blockEntity.isLit()), 3);
			flag = true;
		}

		if (flag) {
			blockEntity.setChanged(); // 必须标记，否则Minecraft不会保存数据
		}
	}

	public boolean isLit() { // 是否正在燃烧
		return this.burnTime > 0;
	}

	public boolean hasWater() { // 是否有水
		return this.waterAmount > 0;
	}

	public void speedAndPowerChanged() {
		BlockState state = this.getBlockState();
		BlockEntity neighbor = this.getLevel().getBlockEntity(this.worldPosition.offset(state.getValue(SteamEngineBlock.FACING).getNormal()));
		if (neighbor != null) {
			LazyOptional<IMechanicalTransmission> capability = neighbor.getCapability(CapabilityList.MECHANICAL_TRANSMISSION, state.getValue(SteamEngineBlock.FACING).getOpposite());
			capability.ifPresent(transmission -> {
				transmission.setSpeed(this.speed);
				transmission.setPower(this.power);
			});
		}
	}

	public void waterUseBucketIO(boolean isPourIn, int amount) {
		if (isPourIn) {
			if (this.waterAmount + amount <= MAX_WATER) {
				this.waterAmount += amount;
				this.inventory.set(1, new ItemStack(Items.BUCKET));
				this.setChanged();
			} else SpacePlane.LOGGER.error("Water overflow! Did you check the water amount?");
		} else {
			if (this.waterAmount - amount >= 0) {
				this.waterAmount -= amount;
				this.inventory.set(1, new ItemStack(Items.WATER_BUCKET));
				this.setChanged();
			} else SpacePlane.LOGGER.error("Too much water took back! Did you check the water amount?");
		}
	}

	public void setSpeedByScreenSlider(int speed) {
		if (speed <= 0 || speed > MAX_SPEED) {
			SpacePlane.LOGGER.error("FUCK YOU! Don't try to crash the TeaCon server by send incorrect speed value!");
		} else {
			this.setSpeed = speed;
			this.setChanged();
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		ContainerHelper.loadAllItems(tag, this.inventory);
		this.speed = tag.getInt("Speed");
		this.setSpeed = tag.getInt("SetSpeed");
		this.power = tag.getInt("Power");
		this.burnTime = tag.getInt("BurnTime");
		this.totalBurnTime = tag.getInt("TotalBurnTime");
		this.waterAmount = tag.getInt("WaterAmount");
		this.shrinkTick = tag.getInt("ShrinkTick");
		// TODO 读取其他数据
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.inventory);
		tag.putInt("Speed", this.speed);
		tag.putInt("SetSpeed", this.setSpeed);
		tag.putInt("Power", this.power);
		tag.putInt("BurnTime", this.burnTime);
		tag.putInt("TotalBurnTime", this.totalBurnTime);
		tag.putInt("WaterAmount", this.waterAmount);
		tag.putInt("ShrinkTick", this.shrinkTick);
		// TODO 存储其他数据
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container." + SpacePlane.MODID + ".steam_engine");
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

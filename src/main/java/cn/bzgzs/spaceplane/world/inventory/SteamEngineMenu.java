package cn.bzgzs.spaceplane.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SteamEngineMenu extends AbstractContainerMenu {
	private final Container container;
	private final ContainerData data;

	public SteamEngineMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(2), new SimpleContainerData(4));
	}

	public SteamEngineMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(MenuTypeList.STEAM_ENGINE.get(), id);
		this.container = container;
		this.data = data;

		this.addSlot(new Slot(container, 0, 70, 42)); // 燃料槽
		this.addSlot(new Slot(container, 1, 111, 34)); // 水槽

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	public ContainerData getData() {
		return this.data;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(index);
		if (slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < 2) {
				if (!this.moveItemStackTo(stack1, 2, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (stack1.getItem() == Items.WATER_BUCKET || stack1.getItem() == Items.BUCKET) { // TODO 不利于模组联动
				if (!this.moveItemStackTo(stack1, 1, 2, false)) return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(stack1, 0, 1, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return stack;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}
}

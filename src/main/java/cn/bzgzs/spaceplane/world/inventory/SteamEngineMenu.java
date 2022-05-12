package cn.bzgzs.spaceplane.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SteamEngineMenu extends AbstractContainerMenu {
	private final Container container;
	public SteamEngineMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(2));
	}

	public SteamEngineMenu(int id, Inventory inventory, Container container) {
		super(MenuTypeList.STEAM_ENGINE.get(), id);
		this.container = container;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}
}

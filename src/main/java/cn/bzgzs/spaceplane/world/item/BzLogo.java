package cn.bzgzs.spaceplane.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BzLogo extends Item {
	public BzLogo() {
		super(new Item.Properties().tab(CreativeModeTabList.TAB_SPACEPLANE));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag advanced) {
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.1").withStyle(ChatFormatting.RED));
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.2").withStyle(ChatFormatting.GOLD));
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.3").withStyle(ChatFormatting.YELLOW));
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.4").withStyle(ChatFormatting.GREEN));
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.5").withStyle(ChatFormatting.BLUE));
		components.add(new TranslatableComponent("itemTooltip.spaceplane.bz_logo.6").withStyle(ChatFormatting.LIGHT_PURPLE));
	}
}

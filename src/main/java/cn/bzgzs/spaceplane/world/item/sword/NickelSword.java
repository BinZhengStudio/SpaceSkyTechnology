package cn.bzgzs.spaceplane.world.item.sword;

import cn.bzgzs.spaceplane.world.item.CreativeModeTabList;
import cn.bzgzs.spaceplane.world.item.ModItemTiers;
import net.minecraft.world.item.SwordItem;

public class NickelSword extends SwordItem {
    public NickelSword(){
        super(ModItemTiers.NICKEL,3,-2.4F,new Properties().tab(CreativeModeTabList.TAB_SPACEPLANE));
    }
}

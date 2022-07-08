package cn.bzgzs.spaceplane.world.item.Tool;

import cn.bzgzs.spaceplane.world.item.CreativeModeTabList;
import cn.bzgzs.spaceplane.world.item.ModItemTiers;
import net.minecraft.world.item.PickaxeItem;

public class RheniumPickaxe extends PickaxeItem {
    public RheniumPickaxe(){
        super(ModItemTiers.RHENIUM,-3,-1.5F,new Properties().tab(CreativeModeTabList.TAB_SPACEPLANE));
    }
}

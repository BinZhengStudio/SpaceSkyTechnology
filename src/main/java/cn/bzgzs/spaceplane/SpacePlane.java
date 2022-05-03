package cn.bzgzs.spaceplane;

import cn.bzgzs.spaceplane.world.item.ItemList;
import cn.bzgzs.spaceplane.world.level.block.BlockList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpacePlane.MODID)
public class SpacePlane {
	public static final String MODID = "spaceplane"; // mod ID

	public SpacePlane() {
		BlockList.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册方块
		ItemList.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册物品

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) { // 暂时不用
	}
}

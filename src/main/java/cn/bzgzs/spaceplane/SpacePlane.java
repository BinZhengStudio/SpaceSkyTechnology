package cn.bzgzs.spaceplane;

import cn.bzgzs.spaceplane.world.item.ItemList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("spaceplane")
public class SpacePlane {
	public static final String MODID = "spaceplane"; // mod ID

	public SpacePlane() {
		ItemList.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus()); // register items

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) { // 暂时不用
	}
}

package cn.bzgzs.spaceplane;

import cn.bzgzs.spaceplane.client.gui.screens.MenuScreenManager;
import cn.bzgzs.spaceplane.client.player.KeyboardInputList;
import cn.bzgzs.spaceplane.network.NetworkHandler;
import cn.bzgzs.spaceplane.sounds.SoundEventList;
import cn.bzgzs.spaceplane.world.entity.EntityTypeList;
import cn.bzgzs.spaceplane.world.inventory.MenuTypeList;
import cn.bzgzs.spaceplane.world.item.ItemList;
import cn.bzgzs.spaceplane.world.level.block.BlockList;
import cn.bzgzs.spaceplane.world.level.block.entity.BlockEntityTypeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SpacePlane.MODID)
public class SpacePlane {
	public static final String MODID = "spaceplane";
	public static final Logger LOGGER = LogManager.getLogger();

	public SpacePlane() {
		BlockList.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册方块
		BlockEntityTypeList.BLOCK_ENTITY_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册方块实体
		EntityTypeList.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册实体
		ItemList.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册物品
		MenuTypeList.MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus()); // 注册Container
		SoundEventList.SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(NetworkHandler::register); // 注册网络发包
	}

	private void doClientStuff(final FMLClientSetupEvent event) { // 与客户端相关的代码
		event.enqueueWork(MenuScreenManager::register); // 绑定Container与Screen
		KeyboardInputList.register(); // 注册按键
	}
}

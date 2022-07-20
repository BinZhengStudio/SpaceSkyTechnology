package cn.bzgzs.spaceplane.client.event;

import cn.bzgzs.spaceplane.client.model.CannonballModel;
import cn.bzgzs.spaceplane.client.model.TestPlaneModel;
import cn.bzgzs.spaceplane.client.renderer.entity.CannonballRenderer;
import cn.bzgzs.spaceplane.client.renderer.entity.TestPlaneRenderer;
import cn.bzgzs.spaceplane.world.entity.EntityTypeList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEventHandler {
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EntityTypeList.TEST.get(), TestPlaneRenderer::new);
		event.registerEntityRenderer(EntityTypeList.CANNONBALL.get(), CannonballRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TestPlaneModel.MAIN, TestPlaneModel::createBodyLayer);
		event.registerLayerDefinition(CannonballModel.MAIN, CannonballModel::createBodyLayer);
	}
}

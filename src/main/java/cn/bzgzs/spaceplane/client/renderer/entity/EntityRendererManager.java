package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.world.entity.EntityTypeList;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class EntityRendererManager {
	public static void register() {
		EntityRenderers.register(EntityTypeList.BZ_20_PLANE.get(), BZ20PlaneRenderer::new);
		EntityRenderers.register(EntityTypeList.TEST.get(), TestPlaneRenderer::new);
		EntityRenderers.register(EntityTypeList.TEST2.get(), TestPlane2Renderer::new);
	}
}

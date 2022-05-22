package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.world.entity.EntityTypeList;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class EntityRendererManager {
	public static void register() {
		EntityRenderers.register(EntityTypeList.J20_PLANE.get(), J20PlaneRenderer::new);
	}
}

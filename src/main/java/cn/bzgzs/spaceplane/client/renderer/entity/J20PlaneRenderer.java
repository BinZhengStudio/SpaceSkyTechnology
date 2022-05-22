package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.entity.J20PlaneEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class J20PlaneRenderer extends EntityRenderer<J20PlaneEntity> {
	public J20PlaneRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(J20PlaneEntity pEntity) {
		return new ResourceLocation(SpacePlane.MODID, "textures/entity/j20_plane.png");
	}
}

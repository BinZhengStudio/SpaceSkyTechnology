package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TestPlaneRenderer extends EntityRenderer<TestPlaneEntity> {
	public TestPlaneRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(TestPlaneEntity pEntity) {
		return new ResourceLocation(SpacePlane.MODID, "textures/entity/bz_20_plane.png");
	}
}

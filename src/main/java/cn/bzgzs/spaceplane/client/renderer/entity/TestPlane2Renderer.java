package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.entity.TestPlane2;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TestPlane2Renderer extends EntityRenderer<TestPlane2> {
	public TestPlane2Renderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(TestPlane2 pEntity) {
		return new ResourceLocation(SpacePlane.MODID, "textures/entity/bz_20_plane.png");
	}
}

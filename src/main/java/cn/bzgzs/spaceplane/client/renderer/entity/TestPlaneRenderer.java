package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.client.model.TestPlaneModel;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TestPlaneRenderer extends EntityRenderer<TestPlaneEntity> implements RenderLayerParent<TestPlaneEntity, TestPlaneModel> {
	private final EntityRendererProvider.Context context;

	public TestPlaneRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public TestPlaneModel getModel() {
		return new TestPlaneModel(this.context.bakeLayer(TestPlaneModel.MAIN));
	}

	@Override
	public ResourceLocation getTextureLocation(TestPlaneEntity entity) {
		return new ResourceLocation(SpacePlane.MODID, "textures/entity/test_plane.png");
	}
}

package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.client.model.TestPlaneModel;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TestPlaneRenderer extends EntityRenderer<TestPlaneEntity> implements RenderLayerParent<TestPlaneEntity, TestPlaneModel> {
	private final EntityRendererProvider.Context context;
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpacePlane.MODID, "textures/entity/test_plane.png");

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
		return TEXTURE;
	}

	@Override
	public void render(TestPlaneEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.pushPose();
		this.getModel().setupAnim(entity, 45.0F,0.0F,-0.1F,45.0F, 0.0F);
		// TODO ????
//		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
//		this.getModel().renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F,1.0F,1.0F);
		matrixStack.popPose();
	}
}

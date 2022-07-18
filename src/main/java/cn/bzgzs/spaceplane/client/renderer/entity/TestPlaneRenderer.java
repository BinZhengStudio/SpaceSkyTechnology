package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.client.model.TestPlaneModel;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

		VertexConsumer cutoutNoCull = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		VertexConsumer translucentCull = buffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
		matrixStack.translate(0.0D, 3.5D, 0.0D);
		if ((entity.yRotO < -90 && entity.getYRot() > 90) || (entity.yRotO > 90 && entity.getYRot() < -90)) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-entity.getYRot()));
		} else {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, -entity.yRotO, -entity.getYRot())));
		}
		if ((entity.xRotO < -90 && entity.getXRot() > 90) || (entity.xRotO > 90 && entity.getXRot() < -90)) {
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		} else {
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		}
		if ((entity.zRotO < -90 && entity.getZRot() > 90) || (entity.zRotO > 90 && entity.getZRot() < -90)) {
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(entity.getZRot()));
		} else {
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.zRotO, entity.getZRot())));
		}
		this.getModel().commonPartRender(matrixStack, cutoutNoCull, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		this.getModel().cullPartRender(matrixStack, translucentCull, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		this.getModel().engineDecorateRender(matrixStack, cutoutNoCull, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (entity.getLandingGear())
			this.getModel().landingGearRenderer(matrixStack, cutoutNoCull, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
	}
}

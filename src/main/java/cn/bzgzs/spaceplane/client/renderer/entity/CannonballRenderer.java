package cn.bzgzs.spaceplane.client.renderer.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.client.model.CannonballModel;
import cn.bzgzs.spaceplane.world.entity.CannonballEntity;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CannonballRenderer extends EntityRenderer<CannonballEntity> implements RenderLayerParent<CannonballEntity, CannonballModel> {
	private final EntityRendererProvider.Context context;
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpacePlane.MODID, "textures/entity/cannonball.png");

	public CannonballRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public CannonballModel getModel() {
		return new CannonballModel(this.context.bakeLayer(CannonballModel.MAIN));
	}

	@Override
	public ResourceLocation getTextureLocation(CannonballEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(CannonballEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.pushPose();
		matrixStack.translate(0.0D, 0.5D, 0.0D);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-entity.getYRot()));
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));
		VertexConsumer cutoutNoCull = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		this.getModel().renderToBuffer(matrixStack, cutoutNoCull, 250, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
	}
}

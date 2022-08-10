package cn.bzgzs.spaceplane.client.model;

import cn.bzgzs.spaceplane.world.entity.CannonballEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CannonballModel extends EntityModel<CannonballEntity> {
	public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation("modid", "custom_model"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart tail;

	public CannonballModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.tail = root.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -22.0F, -5.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 21.0F, -3.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -21.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-2.5F, -23.5F, -2.5F, 5.0F, 5.0F, 2.0F, new CubeDeformation(-0.5F))
				.texOffs(14, 20).addBox(-1.5F, -22.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 16).addBox(-1.5F, -22.5F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 9.0F));

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 6).addBox(-5.0F, -27.0F, -2.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(12, 0).addBox(1.0F, -27.0F, -2.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 27.0F, -8.0F));

		PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -21.0F, 1.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 6).addBox(1.0F, 0.0F, -3.0F, 4.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -27.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(CannonballEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}

package cn.bzgzs.spaceplane.client.model;

import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class TestPlaneModel extends EntityModel<TestPlaneEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "custom_model"), "main");
	private final ModelPart bone;

	public TestPlaneModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, (float) -Math.toRadians(90.0D), 0.0F));

		PartDefinition wing = main.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(8.0F, -36.0F, -27.0F));

		PartDefinition rudder = wing.addOrReplaceChild("rudder", CubeListBuilder.create().texOffs(0, 0).addBox(28.0F, -8.5F, 38.0F, 20.0F, 20.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(28.0F, -8.5F, 3.0F, 20.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-84.0F, -14.5F, 6.0F));

		PartDefinition leftWing = wing.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 0).addBox(32.0F, 0.0F, -9.5F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(43, 0).addBox(-16.0F, 0.0F, -9.5F, 32.0F, 0.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offset(-40.0F, 0.0F, 58.5F));

		PartDefinition rightWing = wing.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(39, 0).addBox(-56.0F, 0.0F, -33.0F, 32.0F, 0.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 0.0F, -13.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition sit = main.addOrReplaceChild("sit", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -5.75F, -6.0F, 2.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.5F, 1.25F, -6.0F, 9.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(20.5F, -37.25F, -1.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(19.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(16.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(12.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(8.0F, -7.0F, -7.0F, 4.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -8.0F, -8.0F, 6.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(40.0F, -31.0F, -1.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(32.0F, -3.0F, 0.0F, 26.0F, 11.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(32.0F, -8.0F, 0.0F, 26.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(32.0F, -8.0F, 19.0F, 26.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 2).addBox(-32.0F, -8.0F, 26.0F, 64.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(1, 0).addBox(-32.0F, -8.0F, 0.0F, 64.0F, 16.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-32.0F, -8.0F, -10.0F, 64.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, -31.0F, -14.0F));

		PartDefinition landingGear = main.addOrReplaceChild("landingGear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frontGear = landingGear.addOrReplaceChild("frontGear", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, 0.0F, -2.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, 17.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(37.0F, -23.0F, 0.0F));

		PartDefinition leftGear = landingGear.addOrReplaceChild("leftGear", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -2.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 17.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.0F, -23.0F, 10.0F));

		PartDefinition rightGear = landingGear.addOrReplaceChild("rightGear", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -2.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 17.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.0F, -23.0F, -10.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(TestPlaneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
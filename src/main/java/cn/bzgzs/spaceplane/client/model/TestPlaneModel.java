package cn.bzgzs.spaceplane.client.model;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.entity.TestPlaneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TestPlaneModel extends EntityModel<TestPlaneEntity> {
	public static final ModelLayerLocation MAIN = new ModelLayerLocation(new ResourceLocation(SpacePlane.MODID, "test_plane"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart windshield;
	private final ModelPart wing;
	private final ModelPart sit;
	private final ModelPart head;
	private final ModelPart landingGear;
	private final ModelPart frontGear;
	private final ModelPart leftGear;
	private final ModelPart rightGear;
	private final ModelPart engine1;
	private final ModelPart engine2;
	private final ModelPart engineRod;
	private final ModelPart engineRod2;
	private final ModelPart engineDecorate;
	private final ModelPart engineDecorate2;

	public TestPlaneModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.windshield = root.getChild("windshield");
		this.wing = root.getChild("wing");
		this.sit = root.getChild("sit");
		this.head = root.getChild("head");
		this.landingGear = root.getChild("landingGear");
		this.frontGear = landingGear.getChild("frontGear");
		this.leftGear = landingGear.getChild("leftGear");
		this.rightGear = landingGear.getChild("rightGear");
		this.engine1 = root.getChild("engine1");
		this.engine2 = root.getChild("engine2");
		this.engineRod = root.getChild("engineRod");
		this.engineRod2 = root.getChild("engineRod2");
		this.engineDecorate = root.getChild("engineDecorate");
		this.engineDecorate2 = root.getChild("engineDecorate2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(204, 170).addBox(-53.0F, -6.0F, -4.0F, 41.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(204, 190).addBox(-53.0F, -6.0F, -23.0F, 41.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(222, 136).addBox(-53.0F, -6.0F, 16.0F, 41.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-53.0F, 6.0F, -23.0F, 41.0F, 2.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-53.0F, -8.0F, -23.0F, 41.0F, 2.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(0, 96).addBox(-12.0F, -8.0F, -23.0F, 23.0F, 16.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(222, 155).addBox(11.0F, -8.0F, 6.0F, 26.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 222).addBox(11.0F, -8.0F, -13.0F, 26.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(192, 0).addBox(11.0F, -3.0F, -13.0F, 26.0F, 11.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition windshield = partdefinition.addOrReplaceChild("windshield", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition layer1 = windshield.addOrReplaceChild("layer1", CubeListBuilder.create(), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition cube_r2 = layer1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(483, 495).addBox(7.0F, -8.0F, 10.0F, -14.0F, -5.0F, -1.0F, new CubeDeformation(0.0F))
				.texOffs(498, 495).addBox(7.0F, -8.0F, 43.0F, -14.0F, -5.0F, -1.0F, new CubeDeformation(0.0F))
				.texOffs(499, 502).addBox(7.0F, -12.0F, 12.0F, -14.0F, -1.0F, -2.0F, new CubeDeformation(0.0F))
				.texOffs(500, 499).addBox(7.0F, -13.0F, 42.0F, -14.0F, -1.0F, -3.0F, new CubeDeformation(0.0F))
				.texOffs(467, 499).addBox(-7.0F, -8.0F, 42.0F, -1.0F, -5.0F, -32.0F, new CubeDeformation(0.0F))
				.texOffs(500, 499).addBox(8.0F, -8.0F, 42.0F, -1.0F, -5.0F, -32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition layer2 = windshield.addOrReplaceChild("layer2", CubeListBuilder.create(), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition cube_r3 = layer2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(172, 426).addBox(6.0F, -13.0F, 11.0F, -12.0F, -4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(94, 479).addBox(6.0F, -13.0F, 40.0F, -12.0F, -4.0F, -1.0F, new CubeDeformation(0.0F))
				.texOffs(94, 478).addBox(6.0F, -17.0F, 39.0F, -12.0F, -1.0F, -27.0F, new CubeDeformation(0.0F))
				.texOffs(125, 454).addBox(7.0F, -13.0F, 39.0F, -1.0F, -4.0F, -27.0F, new CubeDeformation(0.0F))
				.texOffs(97, 454).addBox(-6.0F, -13.0F, 39.0F, -1.0F, -4.0F, -27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition wing = partdefinition.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition rudder = wing.addOrReplaceChild("rudder", CubeListBuilder.create(), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition cube_r4 = rudder.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 96).addBox(-53.0F, -28.0F, -17.0F, 20.0F, 20.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(92, 112).addBox(-53.0F, -28.0F, 18.0F, 20.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition leftWing = wing.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition cube_r5 = leftWing.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(129, 141).addBox(-53.0F, -5.0F, 23.0F, 32.0F, 0.0F, 29.0F, new CubeDeformation(0.0F))
				.texOffs(76, 96).addBox(-5.0F, -5.0F, 23.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition rightWing = wing.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(8.0F, -68.0F, -27.0F));

		PartDefinition cube_r6 = rightWing.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(112, 0).addBox(-5.0F, -5.0F, -39.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(127, 96).addBox(-53.0F, -5.0F, -52.0F, 32.0F, 0.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -2.0F, 27.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition sit = partdefinition.addOrReplaceChild("sit", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition cube_r7 = sit.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 32).addBox(11.0F, -5.0F, -6.0F, 9.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(128, 16).addBox(11.0F, -12.0F, -6.0F, 2.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -70.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(37.0F, -8.0F, -8.0F, 6.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(43.0F, -7.0F, -7.0F, 4.0F, 14.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 116).addBox(47.0F, -6.0F, -6.0F, 4.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 76).addBox(51.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(54.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -70.0F, 0.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition landingGear = partdefinition.addOrReplaceChild("landingGear", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition frontGear = landingGear.addOrReplaceChild("frontGear", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 20.0F));

		PartDefinition cube_r9 = frontGear.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 116).addBox(16.0F, 15.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 116).addBox(18.0F, 8.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -62.0F, -20.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition leftGear = landingGear.addOrReplaceChild("leftGear", CubeListBuilder.create(), PartPose.offset(10.0F, -8.0F, -23.0F));

		PartDefinition cube_r10 = leftGear.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(22, 76).addBox(-27.0F, 15.0F, 7.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 60).addBox(-25.0F, 8.0F, 9.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -62.0F, 23.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition rightGear = landingGear.addOrReplaceChild("rightGear", CubeListBuilder.create(), PartPose.offset(-10.0F, -8.0F, -23.0F));

		PartDefinition cube_r11 = rightGear.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(22, 48).addBox(-27.0F, 15.0F, -13.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-25.0F, 8.0F, -11.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, -62.0F, 23.0F, 0.0F, -1.5708F, -3.1416F));

		PartDefinition engine1 = partdefinition.addOrReplaceChild("engine1", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition cube_r12 = engine1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(158, 170).addBox(-5.0F, -5.0F, -56.0F, 1.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(92, 186).addBox(-16.0F, -5.0F, -56.0F, 1.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(128, 48).addBox(-15.0F, -6.0F, -56.0F, 10.0F, 1.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(94, 141).addBox(-15.0F, 5.0F, -56.0F, 10.0F, 1.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -70.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition engine2 = partdefinition.addOrReplaceChild("engine2", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition cube_r13 = engine2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 158).addBox(15.0F, -5.0F, -56.0F, 1.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(46, 168).addBox(4.0F, -5.0F, -56.0F, 1.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(92, 96).addBox(5.0F, -6.0F, -56.0F, 10.0F, 1.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(128, 0).addBox(5.0F, 5.0F, -56.0F, 10.0F, 1.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -70.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition engineRod = partdefinition.addOrReplaceChild("engineRod", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition mainRod = engineRod.addOrReplaceChild("mainRod", CubeListBuilder.create().texOffs(192, 45).addBox(-4.0F, -39.0F, -106.0F, 4.0F, 4.0F, 39.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -33.0F, 55.0F));

		PartDefinition rodDecorate = engineRod.addOrReplaceChild("rodDecorate", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r14 = rodDecorate.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 32).addBox(-8.7674F, 9.3948F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 60).addBox(-4.7674F, 5.3948F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 66).addBox(-11.7674F, 5.3948F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 32).addBox(-8.7674F, 2.3948F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-8.7674F, 9.3948F, -44.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 72).addBox(-4.7674F, 5.3948F, -44.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 78).addBox(-11.7674F, 5.3948F, -44.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 37).addBox(-8.7674F, 2.3948F, -44.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -70.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition engineRod2 = partdefinition.addOrReplaceChild("engineRod2", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition mainRod2 = engineRod2.addOrReplaceChild("mainRod2", CubeListBuilder.create().texOffs(181, 93).addBox(8.0F, -9.0F, -51.0F, 4.0F, 4.0F, 39.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition rodDecorate2 = engineRod2.addOrReplaceChild("rodDecorate2", CubeListBuilder.create(), PartPose.offset(0.0F, -63.0F, 0.0F));

		PartDefinition cube_r15 = rodDecorate2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 8).addBox(5.8284F, -4.6863F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(36, 69).addBox(9.8284F, -8.6863F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(124, 104).addBox(2.8284F, -8.6863F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 0).addBox(5.8284F, -11.6863F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 5).addBox(5.8284F, -4.6863F, -44.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 16).addBox(9.8284F, -8.6863F, -44.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 22).addBox(2.8284F, -8.6863F, -44.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 10).addBox(5.8284F, -11.6863F, -44.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition engineDecorate = partdefinition.addOrReplaceChild("engineDecorate", CubeListBuilder.create().texOffs(128, 56).addBox(-11.5F, -66.0F, -49.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 52).addBox(-11.5F, -75.0F, -17.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 48).addBox(-11.5F, -75.0F, -35.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 39).addBox(-11.5F, -75.0F, -49.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(135, 63).addBox(-15.0F, -71.5F, -17.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(37, 135).addBox(-15.0F, -71.5F, -35.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(124, 132).addBox(-6.0F, -71.5F, -17.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(116, 132).addBox(-6.0F, -71.5F, -49.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(108, 132).addBox(-6.0F, -71.5F, -35.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(100, 132).addBox(-15.0F, -71.5F, -49.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 35).addBox(-11.5F, -66.0F, -17.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 128).addBox(-11.5F, -66.0F, -35.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition engineDecorate2 = partdefinition.addOrReplaceChild("engineDecorate2", CubeListBuilder.create().texOffs(124, 100).addBox(8.5F, -66.0F, -49.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(124, 96).addBox(8.5F, -75.0F, -17.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(34, 88).addBox(8.5F, -75.0F, -35.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(22, 88).addBox(8.5F, -75.0F, -49.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(92, 132).addBox(5.0F, -71.5F, -17.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 132).addBox(5.0F, -71.5F, -35.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(128, 84).addBox(14.0F, -71.5F, -17.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 113).addBox(14.0F, -71.5F, -49.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 76).addBox(14.0F, -71.5F, -35.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(37, 2).addBox(5.0F, -71.5F, -49.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(14, 76).addBox(8.5F, -66.0F, -17.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 57).addBox(8.5F, -66.0F, -35.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 56.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(TestPlaneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.xRot = headPitch;
		this.body.yRot = netHeadYaw;
		this.body.zRot = limbSwing;

		this.wing.xRot = headPitch;
		this.wing.yRot = netHeadYaw;
		this.wing.zRot = limbSwing;

		this.engine1.xRot = headPitch;
		this.engine1.yRot = netHeadYaw;
		this.engine1.zRot = limbSwing;

		this.engine2.xRot = headPitch;
		this.engine2.yRot = netHeadYaw;
		this.engine2.zRot = limbSwing;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		windshield.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sit.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		landingGear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engine1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engine2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineRod.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineRod2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineDecorate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineDecorate2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void commonPartRender(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		sit.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		landingGear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engine1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engine2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void cullPartRender(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		windshield.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void engineDecorateRender(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		engineRod.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineRod2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineDecorate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engineDecorate2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart getRoot() {
		return root;
	}

	public ModelPart getFrontGear() {
		return frontGear;
	}

	public ModelPart getLeftGear() {
		return leftGear;
	}

	public ModelPart getRightGear() {
		return rightGear;
	}
}
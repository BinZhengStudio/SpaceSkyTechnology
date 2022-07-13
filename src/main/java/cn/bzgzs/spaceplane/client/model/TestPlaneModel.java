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
	private final ModelPart body;
	private final ModelPart wing;
	private final ModelPart engine;
	private final ModelPart landing_gear1;
	private final ModelPart landing_gear2;
	private final ModelPart landing_gear3;

	public TestPlaneModel(ModelPart root) {
		this.body = root.getChild("body");
		this.wing = root.getChild("wing");
		this.engine = root.getChild("engine");
		this.landing_gear1 = root.getChild("landing_gear1");
		this.landing_gear2 = root.getChild("landing_gear2");
		this.landing_gear3 = root.getChild("landing_gear3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition windshield = body.addOrReplaceChild("windshield", CubeListBuilder.create(), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition layer1 = windshield.addOrReplaceChild("layer1", CubeListBuilder.create().texOffs(0, 0).addBox(9.0F, -50.0F, 12.0F, 1.0F, 6.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.0F, -50.0F, 12.0F, 1.0F, 6.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -51.0F, 13.0F, 2.0F, 1.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(7.0F, -51.0F, 13.0F, 2.0F, 1.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -52.0F, 50.0F, 14.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -52.0F, 12.0F, 14.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -51.0F, 57.0F, 18.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -51.0F, 12.0F, 18.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition layer2 = windshield.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -56.0F, 14.0F, 1.0F, 5.0F, 36.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -56.0F, 14.0F, 1.0F, 5.0F, 36.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -57.0F, 45.0F, 12.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -57.0F, 14.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -56.0F, 49.0F, 12.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -56.0F, 14.0F, 12.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition layer3 = windshield.addOrReplaceChild("layer3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-6.0F, -60.0F, 16.0F, 1.0F, 4.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(5.0F, -60.0F, 16.0F, 1.0F, 4.0F, 29.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -61.0F, 17.0F, 10.0F, 1.0F, 27.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -60.0F, 44.0F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -60.0F, 16.0F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition sit = body.addOrReplaceChild("sit", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -43.0F, 26.0F, 13.0F, 16.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -50.0F, 26.0F, 13.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -59.0F, 24.0F, 9.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition leftBody = body.addOrReplaceChild("leftBody", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -44.0F, -42.0F, 15.0F, 18.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-24.0F, -28.0F, 13.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -29.0F, 13.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -30.0F, 13.0F, 12.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -31.0F, 13.0F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -32.0F, 13.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -33.0F, 13.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -33.0F, 13.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -34.0F, 13.0F, 12.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -35.0F, 13.0F, 12.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -36.0F, 13.0F, 12.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -37.0F, 13.0F, 12.0F, 1.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -43.0F, 13.0F, 12.0F, 6.0F, 37.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -42.0F, 50.0F, 12.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(-24.0F, -44.0F, -44.0F, 12.0F, 17.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-24.0F, -44.0F, -46.0F, 12.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-24.0F, -44.0F, -48.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-24.0F, -44.0F, -50.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-24.0F, -32.0F, -51.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -33.0F, -52.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -34.0F, -53.0F, 12.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -35.0F, -55.0F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -36.0F, -58.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -37.0F, -62.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -38.0F, -66.0F, 12.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-24.0F, -42.0F, -70.0F, 12.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition rightBody = body.addOrReplaceChild("rightBody", CubeListBuilder.create().texOffs(0, 0).addBox(9.0F, -44.0F, -42.0F, 15.0F, 18.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(12.0F, -44.0F, -44.0F, 12.0F, 17.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(12.0F, -44.0F, -46.0F, 12.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(12.0F, -44.0F, -48.0F, 12.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(12.0F, -44.0F, -50.0F, 12.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(12.0F, -32.0F, -51.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -33.0F, -52.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -34.0F, -53.0F, 12.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -35.0F, -55.0F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -36.0F, -58.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -37.0F, -62.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -38.0F, -66.0F, 12.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -42.0F, -70.0F, 12.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -28.0F, 13.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -29.0F, 13.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -30.0F, 13.0F, 12.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -31.0F, 13.0F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -32.0F, 13.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -33.0F, 13.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -34.0F, 13.0F, 12.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -35.0F, 13.0F, 12.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -36.0F, 13.0F, 12.0F, 1.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -37.0F, 13.0F, 12.0F, 1.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -43.0F, 13.0F, 12.0F, 6.0F, 37.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -42.0F, 50.0F, 12.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition mainBody = body.addOrReplaceChild("mainBody", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -48.0F, 58.0F, 18.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -44.0F, -70.0F, 3.0F, 18.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -26.0F, -70.0F, 18.0F, 3.0F, 82.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -26.0F, 12.0F, 18.0F, 3.0F, 45.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.0F, -47.0F, -70.0F, 20.0F, 3.0F, 82.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -44.0F, -21.0F, 18.0F, 18.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.0F, -44.0F, 13.0F, 3.0F, 17.0F, 50.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -44.0F, 13.0F, 3.0F, 17.0F, 50.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.0F, -27.0F, 13.0F, 24.0F, 1.0F, 50.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.0F, -44.0F, -70.0F, 3.0F, 18.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -44.0F, 63.0F, 18.0F, 18.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -38.0F, 77.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -40.0F, 72.0F, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -42.0F, 67.0F, 14.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition wing = partdefinition.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition rudder = wing.addOrReplaceChild("rudder", CubeListBuilder.create(), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition rightRudder = rudder.addOrReplaceChild("rightRudder", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.3842F, -45.6247F, -58.0F, 3.0F, 3.0F, 51.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-16.3842F, -60.6247F, -48.0F, 3.0F, 3.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-16.3842F, -57.6247F, -50.0F, 3.0F, 3.0F, 27.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-16.3842F, -54.6247F, -52.0F, 3.0F, 3.0F, 33.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-16.3842F, -51.6247F, -54.0F, 3.0F, 3.0F, 39.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-16.3842F, -48.6247F, -56.0F, 3.0F, 3.0F, 45.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		PartDefinition leftRudder = rudder.addOrReplaceChild("leftRudder", CubeListBuilder.create().texOffs(0, 0).addBox(13.3842F, -45.6247F, -58.0F, 3.0F, 3.0F, 51.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(13.3842F, -60.6247F, -48.0F, 3.0F, 3.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(13.3842F, -57.6247F, -50.0F, 3.0F, 3.0F, 27.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(13.3842F, -54.6247F, -52.0F, 3.0F, 3.0F, 33.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(13.3842F, -51.6247F, -54.0F, 3.0F, 3.0F, 39.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(13.3842F, -48.6247F, -56.0F, 3.0F, 3.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		PartDefinition rightWing = wing.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition rightFront = rightWing.addOrReplaceChild("rightFront", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 32.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 28.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 13.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 17.0F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 20.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -41.0F, 24.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 32.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 28.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 13.0F, 1.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 15.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 17.0F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 20.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, 24.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(25.0F, -41.0F, 36.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(24.0F, -41.0F, 36.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(26.0F, -41.0F, 28.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(26.0F, -41.0F, 13.0F, 1.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(26.0F, -41.0F, 24.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(26.0F, -41.0F, 32.0F, 1.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(24.0F, -41.0F, 40.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(38.0F, -41.0F, 13.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(35.0F, -41.0F, 13.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(32.0F, -41.0F, 13.0F, 3.0F, 3.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(27.0F, -41.0F, 13.0F, 2.0F, 3.0F, 32.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -42.0F, 13.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -42.0F, 20.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -42.0F, 27.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(29.0F, -41.0F, 13.0F, 3.0F, 3.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(50.0F, -41.0F, -43.0F, 3.0F, 3.0F, 43.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(47.0F, -41.0F, -43.0F, 3.0F, 3.0F, 46.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(56.0F, -41.0F, -41.0F, 3.0F, 3.0F, 35.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(53.0F, -41.0F, -41.0F, 3.0F, 3.0F, 38.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(44.0F, -41.0F, -43.0F, 3.0F, 3.0F, 49.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(41.0F, -41.0F, -43.0F, 3.0F, 3.0F, 52.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(30.0F, -41.0F, -46.0F, 11.0F, 3.0F, 59.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(29.0F, -42.0F, -46.0F, 1.0F, 4.0F, 59.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(28.0F, -42.0F, -46.0F, 1.0F, 5.0F, 59.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(27.0F, -42.0F, -48.0F, 1.0F, 6.0F, 61.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(26.0F, -43.0F, -50.0F, 1.0F, 8.0F, 63.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(25.0F, -43.0F, -50.0F, 1.0F, 11.0F, 63.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -43.0F, 13.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -39.0F, -52.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -44.0F, -50.0F, 1.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -44.0F, -48.0F, 1.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -44.0F, -46.0F, 1.0F, 15.0F, 59.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightBack = rightWing.addOrReplaceChild("rightBack", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(15.0F, -41.0F, -88.0F, 3.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(12.0F, -41.0F, -87.0F, 3.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(18.0F, -41.0F, -89.0F, 6.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(27.0F, -41.0F, -87.0F, 3.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(24.0F, -41.0F, -89.0F, 3.0F, 2.0F, 39.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(30.0F, -41.0F, -87.0F, 3.0F, 2.0F, 33.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(33.0F, -41.0F, -87.0F, 3.0F, 2.0F, 31.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(36.0F, -41.0F, -87.0F, 3.0F, 2.0F, 29.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(39.0F, -41.0F, -85.0F, 3.0F, 2.0F, 25.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(42.0F, -41.0F, -85.0F, 3.0F, 2.0F, 23.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(45.0F, -41.0F, -85.0F, 3.0F, 2.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftWing = wing.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition leftFront = leftWing.addOrReplaceChild("leftFront", CubeListBuilder.create().texOffs(0, 0).addBox(-27.0F, -43.0F, 13.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).mirror().addBox(-25.0F, -39.0F, -52.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-25.0F, -44.0F, -48.0F, 1.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-25.0F, -44.0F, -50.0F, 1.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(-26.0F, -43.0F, -50.0F, 1.0F, 11.0F, 63.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -43.0F, -50.0F, 1.0F, 8.0F, 63.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-28.0F, -42.0F, -48.0F, 1.0F, 6.0F, 61.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-29.0F, -42.0F, -45.0F, 1.0F, 5.0F, 58.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-30.0F, -42.0F, -45.0F, 1.0F, 4.0F, 58.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-41.0F, -41.0F, -45.0F, 11.0F, 3.0F, 58.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-44.0F, -41.0F, -43.0F, 3.0F, 3.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-47.0F, -41.0F, -43.0F, 3.0F, 3.0F, 49.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-56.0F, -41.0F, -41.0F, 3.0F, 3.0F, 38.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-52.0F, -41.0F, -41.0F, 3.0F, 3.0F, 38.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-59.0F, -41.0F, -41.0F, 3.0F, 3.0F, 35.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-50.0F, -41.0F, -43.0F, 3.0F, 3.0F, 46.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-53.0F, -41.0F, -43.0F, 3.0F, 3.0F, 43.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 40.0F, 2.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-32.0F, -41.0F, 13.0F, 3.0F, 3.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -42.0F, 27.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-28.0F, -42.0F, 20.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-30.0F, -42.0F, 13.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-29.0F, -41.0F, 13.0F, 2.0F, 3.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-35.0F, -41.0F, 13.0F, 3.0F, 3.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-38.0F, -41.0F, 13.0F, 3.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-41.0F, -41.0F, 13.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -44.0F, -46.0F, 1.0F, 15.0F, 59.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -41.0F, 32.0F, 1.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -41.0F, 28.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -41.0F, 24.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -41.0F, 13.0F, 1.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-28.0F, -41.0F, 12.0F, 1.0F, 4.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 36.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 32.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 28.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 13.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 17.0F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 20.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.0F, -41.0F, 24.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 36.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 32.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 28.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 13.0F, 1.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 15.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 17.0F, 1.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 20.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-25.0F, -41.0F, 24.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftBack = leftWing.addOrReplaceChild("leftBack", CubeListBuilder.create().texOffs(0, 0).addBox(-18.0F, -41.0F, -88.0F, 3.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.0F, -41.0F, -86.0F, 3.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-24.0F, -41.0F, -89.0F, 6.0F, 2.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-27.0F, -41.0F, -89.0F, 3.0F, 2.0F, 39.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-30.0F, -41.0F, -87.0F, 3.0F, 2.0F, 35.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-33.0F, -41.0F, -87.0F, 3.0F, 2.0F, 33.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-36.0F, -41.0F, -87.0F, 3.0F, 2.0F, 31.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-39.0F, -41.0F, -87.0F, 3.0F, 2.0F, 29.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-42.0F, -41.0F, -85.0F, 3.0F, 2.0F, 25.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-45.0F, -41.0F, -85.0F, 3.0F, 2.0F, 23.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-48.0F, -41.0F, -85.0F, 3.0F, 2.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition engine = partdefinition.addOrReplaceChild("engine", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition engineRod = engine.addOrReplaceChild("engineRod", CubeListBuilder.create(), PartPose.offset(0.0F, 65.0F, 0.0F));

		PartDefinition mainRod = engineRod.addOrReplaceChild("mainRod", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -68.0F, -73.0F, 6.0F, 6.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.0F, -67.0F, -73.0F, 1.0F, 4.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -69.0F, -73.0F, 4.0F, 1.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -67.0F, -73.0F, 1.0F, 4.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -62.0F, -73.0F, 4.0F, 1.0F, 52.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rodDecorate = engineRod.addOrReplaceChild("rodDecorate", CubeListBuilder.create(), PartPose.offset(-0.9645F, -16.8934F, -94.5F));

		PartDefinition cube_r1 = rodDecorate.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-26.9203F, -20.8492F, -48.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-21.9203F, -25.8492F, -48.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-31.9203F, -25.8492F, -48.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-26.9203F, -30.8492F, -48.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.9203F, -20.8492F, -66.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-21.9203F, -25.8492F, -66.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-31.9203F, -25.8492F, -66.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-26.9203F, -30.8492F, -66.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-26.9203F, -20.8492F, -31.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-26.9203F, -30.8492F, -31.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-21.9203F, -25.8492F, -31.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(70, 62).addBox(-31.9203F, -25.8492F, -31.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9645F, -13.1066F, 94.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition engineBody = engine.addOrReplaceChild("engineBody", CubeListBuilder.create(), PartPose.offset(0.0F, 65.0F, 0.0F));

		PartDefinition engineDecorate = engineBody.addOrReplaceChild("engineDecorate", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -60.0F, -71.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -72.0F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -72.0F, -39.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -72.0F, -57.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -72.0F, -71.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -66.5F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -66.5F, -39.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -66.5F, -57.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -66.5F, -26.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -66.5F, -39.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -66.5F, -71.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -66.5F, -57.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -66.5F, -71.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -60.0F, -39.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -60.0F, -57.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -60.0F, -26.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition engineBodyMain = engineBody.addOrReplaceChild("engineBodyMain", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -58.0F, -78.0F, 14.0F, 2.0F, 57.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -74.0F, -78.0F, 14.0F, 2.0F, 57.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.0F, -72.0F, -78.0F, 2.0F, 14.0F, 57.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(7.0F, -72.0F, -78.0F, 2.0F, 14.0F, 57.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition landing_gear1 = partdefinition.addOrReplaceChild("landing_gear1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition cube_r2 = landing_gear1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -11.1824F, 43.1475F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -15.1824F, 43.1475F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.5F, -19.1824F, 43.1475F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(1.5F, -19.1824F, 43.1475F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.5F, -15.1824F, 43.1475F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -13.1824F, 43.1475F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.5F, -10.1824F, 43.1475F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(1.5F, -11.1824F, 44.1475F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.5F, -11.1824F, 43.1475F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(2.5F, -9.9493F, 43.4866F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(1.5F, -6.1824F, 44.1475F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.5F, -6.1824F, 44.1475F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 35.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition wheel = landing_gear1.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -10.8581F, 40.4587F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -6.8581F, 42.4587F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -7.8581F, 41.4587F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -11.8581F, 41.4587F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -12.8581F, 42.4587F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition landing_gear2 = partdefinition.addOrReplaceChild("landing_gear2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition cube_r3 = landing_gear2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-10.9133F, -11.5939F, -42.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -23.5939F, -45.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -20.5939F, -44.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -17.5939F, -43.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.9133F, -23.5939F, -45.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.9133F, -20.5939F, -44.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.9133F, -17.5939F, -43.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.9133F, -14.5939F, -42.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -14.5939F, -42.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-13.9133F, -13.5939F, -42.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.9133F, -10.5939F, -42.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -11.5939F, -41.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.9133F, -11.5939F, -42.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-9.9133F, -10.5939F, -42.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.9133F, -6.5939F, -41.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.9133F, -6.5939F, -41.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 35.0F, 0.0F, 0.0F, 0.0F, 0.2146F));

		PartDefinition bone = landing_gear2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-12.25F, -5.9F, -42.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.25F, -9.9F, -44.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.25F, -6.9F, -43.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.25F, -11.9F, -42.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.25F, -10.9F, -43.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition landing_gear3 = partdefinition.addOrReplaceChild("landing_gear3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.0F, 0.0F, 0.0F, 0.0F, (float) -Math.PI));

		PartDefinition cube_r4 = landing_gear3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(9.7855F, -11.6077F, -42.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(9.7855F, -23.6077F, -45.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(9.7855F, -20.6077F, -44.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(9.7855F, -17.6077F, -43.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -23.6077F, -45.0F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -20.6077F, -44.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -17.6077F, -43.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -14.6077F, -42.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(9.7855F, -14.6077F, -42.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(10.7855F, -13.6077F, -42.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(14.7855F, -10.6077F, -42.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(9.7855F, -11.6077F, -41.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -11.6077F, -42.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.7855F, -10.6077F, -42.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.7855F, -6.6077F, -41.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(13.7855F, -6.6077F, -41.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 35.0F, 0.0F, 0.0F, 0.0F, -0.2146F));

		PartDefinition bone2 = landing_gear3.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(8.25F, -5.9F, -42.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.25F, -9.9F, -44.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.25F, -6.9F, -43.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.25F, -11.9F, -42.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(8.25F, -10.9F, -43.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 35.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(TestPlaneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.xRot = headPitch;
		this.body.yRot = netHeadYaw;
		this.body.zRot = limbSwing;

		this.wing.xRot = headPitch;
		this.wing.yRot = netHeadYaw;
		this.wing.zRot = limbSwing;

		this.engine.xRot = headPitch;
		this.engine.yRot = netHeadYaw;
		this.engine.zRot = limbSwing;

		this.landing_gear1.xRot = headPitch;
		this.landing_gear1.yRot = netHeadYaw;
		this.landing_gear1.zRot = limbSwing;

		this.landing_gear2.xRot = headPitch;
		this.landing_gear2.yRot = netHeadYaw;
		this.landing_gear2.zRot = limbSwing;

		this.landing_gear3.xRot = headPitch;
		this.landing_gear3.yRot = netHeadYaw;
		this.landing_gear3.zRot = limbSwing;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		engine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		landing_gear1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		landing_gear2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		landing_gear3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
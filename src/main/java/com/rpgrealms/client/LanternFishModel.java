package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LanternFishEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Modelo del Pez linterna rúnico. GENERADO por fishgen.py junto con su textura. */
public class LanternFishModel extends EntityModel<LanternFishEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "lantern_fish"), "main");

    private final ModelPart root;
    private final ModelPart body, head, jaw, rear, tail, dorsal, ventral, pectL, pectR, stalk, bulb;

    public LanternFishModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("fish").getChild("body");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.rear = this.body.getChild("rear");
        this.tail = this.rear.getChild("tail");
        this.dorsal = this.body.getChild("dorsal");
        this.ventral = this.body.getChild("ventral");
        this.pectL = this.body.getChild("pect_l");
        this.pectR = this.body.getChild("pect_r");
        this.stalk = this.head.getChild("stalk");
        this.bulb = this.stalk.getChild("bulb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition fishRoot = mesh.getRoot().addOrReplaceChild("fish", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, -6.0F));
        PartDefinition root = fishRoot;
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3F, -3.5F, 0F, 6F, 7F, 8F), PartPose.offset(0F, 0F, 0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0).addBox(-2.5F, -3F, -5F, 5F, 6F, 5F), PartPose.offset(0F, 0F, 0F));
        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(46, 25).addBox(-1.5F, -1F, -3F, 3F, 2F, 3F), PartPose.offset(0F, 0.8F, -5F));
        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 33).addBox(-1.5F, 0F, -3F, 3F, 1F, 3F), PartPose.offset(0F, 1.4F, -4.9F));
        PartDefinition rear = body.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(0, 15).addBox(-2.5F, -3F, 0F, 5F, 6F, 4F), PartPose.offset(0F, 0F, 8F));
        PartDefinition tail = rear.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 15).addBox(-1.5F, -2F, 0F, 3F, 4F, 4F), PartPose.offset(0F, 0F, 4F));
        PartDefinition caudal_m = tail.addOrReplaceChild("caudal_m", CubeListBuilder.create().texOffs(12, 25).addBox(-0.5F, -2F, 0F, 1F, 4F, 3F), PartPose.offset(0F, 0F, 4F));
        PartDefinition caudal_u = tail.addOrReplaceChild("caudal_u", CubeListBuilder.create().texOffs(46, 15).addBox(-0.5F, -5F, 0.5F, 1F, 3F, 5F), PartPose.offset(0F, 0F, 4F));
        PartDefinition caudal_d = tail.addOrReplaceChild("caudal_d", CubeListBuilder.create().texOffs(0, 25).addBox(-0.5F, 2F, 0.5F, 1F, 3F, 5F), PartPose.offset(0F, 0F, 4F));
        PartDefinition dorsal = body.addOrReplaceChild("dorsal", CubeListBuilder.create().texOffs(18, 15).addBox(-0.5F, -4F, 0F, 1F, 4F, 6F), PartPose.offset(0F, -3.5F, 1F));
        PartDefinition ventral = body.addOrReplaceChild("ventral", CubeListBuilder.create().texOffs(20, 25).addBox(-0.5F, 0F, 0F, 1F, 2.5F, 4F), PartPose.offset(0F, 3.5F, 2F));
        PartDefinition pect_l = body.addOrReplaceChild("pect_l", CubeListBuilder.create().texOffs(12, 33).addBox(0F, -0.5F, -1F, 4F, 1F, 3F), PartPose.offsetAndRotation(3F, 1.5F, 1.5F, 0F, 0F, 0.5F));
        PartDefinition pect_r = body.addOrReplaceChild("pect_r", CubeListBuilder.create().texOffs(26, 33).addBox(-4F, -0.5F, -1F, 4F, 1F, 3F), PartPose.offsetAndRotation(-3F, 1.5F, 1.5F, 0F, 0F, -0.5F));
        PartDefinition eye_l = head.addOrReplaceChild("eye_l", CubeListBuilder.create().texOffs(40, 33).addBox(0F, -1F, -1F, 1F, 2F, 2F), PartPose.offset(2.5F, -0.6F, -2.5F));
        PartDefinition eye_r = head.addOrReplaceChild("eye_r", CubeListBuilder.create().texOffs(46, 33).addBox(-1F, -1F, -1F, 1F, 2F, 2F), PartPose.offset(-2.5F, -0.6F, -2.5F));
        PartDefinition stalk = head.addOrReplaceChild("stalk", CubeListBuilder.create().texOffs(30, 25).addBox(-0.5F, -5F, -0.5F, 1F, 5F, 1F), PartPose.offsetAndRotation(0F, -3F, -2F, 0.9F, 0F, 0F));
        PartDefinition bulb = stalk.addOrReplaceChild("bulb", CubeListBuilder.create().texOffs(34, 25).addBox(-1.5F, -3F, -1.5F, 3F, 3F, 3F), PartPose.offset(0F, -5F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(LanternFishEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float move = Mth.clamp(limbSwingAmount * 3.0F, 0.0F, 1.0F);
        boolean wet = entity.isInWater();
        float speed = wet ? 0.22F + move * 0.35F : 0.9F;
        float amp = wet ? 0.5F + move * 0.9F : 1.6F;
        float ph = ageInTicks * speed;
        this.body.yRot = Mth.sin(ph) * 0.07F * amp;
        this.rear.yRot = Mth.sin(ph - 0.8F) * 0.22F * amp;
        this.tail.yRot = Mth.sin(ph - 1.6F) * 0.32F * amp;
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F) * 0.4F - Mth.sin(ph) * 0.05F * amp;
        this.dorsal.yRot = Mth.sin(ph - 0.4F) * 0.10F;
        this.ventral.yRot = Mth.sin(ph - 0.4F) * 0.10F;
        float flap = Mth.sin(ageInTicks * 0.22F + 1.0F) * 0.25F;
        this.pectL.zRot = 0.5F + flap;
        this.pectR.zRot = -0.5F - flap;
        this.jaw.xRot = 0.08F + (Mth.sin(ageInTicks * 0.13F) * 0.5F + 0.5F) * 0.14F;
        // el señuelo se balancea y late
        this.stalk.xRot = 0.9F + Mth.sin(ageInTicks * 0.10F) * 0.16F - move * 0.25F;
        this.stalk.yRot = Mth.sin(ageInTicks * 0.07F) * 0.12F - this.body.yRot;
        this.bulb.xRot = Mth.sin(ageInTicks * 0.10F - 0.6F) * 0.2F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay,
                               float r, float g, float b, float a) {
        this.root.render(poseStack, buffer, light, overlay, r, g, b, a);
    }
}

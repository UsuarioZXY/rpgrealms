package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.SeaFoxEntity;
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


/** Modelo del Zorro marino. GENERADO por mobgen.py junto con su textura. */
public class SeaFoxModel extends EntityModel<SeaFoxEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "sea_fox"), "main");

    private final ModelPart root;
    private final ModelPart body, head, earL, earR, legFL, legFR, legHL, legHR, tail, tail2, flM, flU, flD;

    public SeaFoxModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.earL = this.head.getChild("ear_l");
        this.earR = this.head.getChild("ear_r");
        this.legFL = root.getChild("leg_fl");
        this.legFR = root.getChild("leg_fr");
        this.legHL = root.getChild("leg_hl");
        this.legHR = root.getChild("leg_hr");
        this.tail = this.body.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
        this.flM = this.tail2.getChild("fl_m");
        this.flU = this.tail2.getChild("fl_u");
        this.flD = this.tail2.getChild("fl_d");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3F, -3F, -5F, 6F, 6F, 10F), PartPose.offset(0F, 15F, 0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-3F, -3F, -5F, 6F, 6F, 5F), PartPose.offset(0F, -1F, -5F));
        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(40, 16).addBox(-2F, 0F, -3F, 4F, 3F, 3F), PartPose.offset(0F, 0F, -5F));
        PartDefinition ear_l = head.addOrReplaceChild("ear_l", CubeListBuilder.create().texOffs(36, 25).addBox(-1F, -3F, -0.5F, 2F, 3F, 1F), PartPose.offsetAndRotation(2F, -3F, -2F, 0F, 0F, 0.1F));
        PartDefinition ear_r = head.addOrReplaceChild("ear_r", CubeListBuilder.create().texOffs(42, 25).addBox(-1F, -3F, -0.5F, 2F, 3F, 1F), PartPose.offsetAndRotation(-2F, -3F, -2F, 0F, 0F, -0.1F));
        PartDefinition leg_fl = root.addOrReplaceChild("leg_fl", CubeListBuilder.create().texOffs(54, 0).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(2F, 17F, -3.5F));
        PartDefinition leg_fr = root.addOrReplaceChild("leg_fr", CubeListBuilder.create().texOffs(0, 16).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(-2F, 17F, -3.5F));
        PartDefinition leg_hl = root.addOrReplaceChild("leg_hl", CubeListBuilder.create().texOffs(8, 16).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(2F, 17F, 3.5F));
        PartDefinition leg_hr = root.addOrReplaceChild("leg_hr", CubeListBuilder.create().texOffs(16, 16).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(-2F, 17F, 3.5F));
        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 16).addBox(-1.5F, -1.5F, 0F, 3F, 3F, 5F), PartPose.offsetAndRotation(0F, -1F, 5F, -0.3F, 0F, 0F));
        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 25).addBox(-1F, -1F, 0F, 2F, 2F, 4F), PartPose.offset(0F, 0F, 5F));
        PartDefinition fl_m = tail2.addOrReplaceChild("fl_m", CubeListBuilder.create().texOffs(12, 25).addBox(-0.5F, -1F, 0F, 1F, 3F, 3F), PartPose.offset(0F, 0F, 4F));
        PartDefinition fl_u = tail2.addOrReplaceChild("fl_u", CubeListBuilder.create().texOffs(20, 25).addBox(-0.5F, -4F, 0.5F, 1F, 3F, 3F), PartPose.offset(0F, 0F, 4F));
        PartDefinition fl_d = tail2.addOrReplaceChild("fl_d", CubeListBuilder.create().texOffs(28, 25).addBox(-0.5F, 2F, 0.5F, 1F, 3F, 3F), PartPose.offset(0F, 0F, 4F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SeaFoxEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float deg = (float) Math.PI / 180F;
        boolean wet = entity.isInWater();
        boolean sit = entity.isInSittingPose();
        float sw = wet ? limbSwing * 1.4F : limbSwing * 0.6662F;
        float amp = wet ? 0.9F : 1.3F;
        float f = limbSwingAmount * amp;
        float bob = Mth.sin(ageInTicks * 0.06F);
        this.body.y = 15.0F;
        this.body.xRot = 0.0F;
        this.head.xRot = headPitch * deg + bob * 0.03F;
        this.head.yRot = netHeadYaw * deg;
        this.legFL.xRot = Mth.cos(sw) * f;
        this.legFR.xRot = Mth.cos(sw + (float) Math.PI) * f;
        this.legHL.xRot = Mth.cos(sw + (float) Math.PI) * f;
        this.legHR.xRot = Mth.cos(sw) * f;
        float wag = 0.12F + limbSwingAmount * 0.25F + (wet ? 0.1F : 0.0F);
        float w = ageInTicks * wag;
        this.tail.yRot = Mth.sin(w) * 0.30F;
        this.tail2.yRot = Mth.sin(w - 0.9F) * 0.40F;
        this.flM.yRot = Mth.sin(w - 1.8F) * 0.30F;
        this.flU.yRot = Mth.sin(w - 1.8F) * 0.30F;
        this.flD.yRot = Mth.sin(w - 1.8F) * 0.30F;
        this.earL.zRot = 0.1F + Mth.sin(ageInTicks * 0.05F) * 0.06F;
        this.earR.zRot = -0.1F - Mth.sin(ageInTicks * 0.05F + 1.0F) * 0.06F;
        if (sit) {
            this.body.y = 18.0F;
            this.body.xRot = -0.55F;
            this.head.xRot += 0.55F;
            this.legFL.xRot = -0.1F;
            this.legFR.xRot = -0.1F;
            this.legHL.xRot = -1.4F;
            this.legHR.xRot = -1.4F;
            this.tail.xRot = -0.9F;
        } else {
            this.tail.xRot = -0.3F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay,
                               float r, float g, float b, float a) {
        this.root.render(poseStack, buffer, light, overlay, r, g, b, a);
    }
}

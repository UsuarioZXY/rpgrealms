package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.FaroleroEntity;
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


/** Modelo del Farolero (medusa flotante). GENERADO por mobgen.py junto con su textura. */
public class FaroleroModel extends EntityModel<FaroleroEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "farolero"), "main");

    private final ModelPart root;
    private final ModelPart main, bell;
    private final ModelPart[] tent = new ModelPart[8];
    private final ModelPart[] tentB = new ModelPart[8];
    private final ModelPart[] arm = new ModelPart[4];
    private final ModelPart[] armB = new ModelPart[4];

    public FaroleroModel(ModelPart root) {
        this.root = root;
        this.main = root.getChild("main");
        this.bell = this.main.getChild("bell");
        for (int i = 0; i < 8; i++) {
            this.tent[i] = this.bell.getChild("t" + i);
            this.tentB[i] = this.tent[i].getChild("t" + i + "b");
        }
        for (int i = 0; i < 4; i++) {
            this.arm[i] = this.bell.getChild("a" + i);
            this.armB[i] = this.arm[i].getChild("a" + i + "b");
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));
        PartDefinition bell = root.addOrReplaceChild("bell", CubeListBuilder.create().texOffs(0, 14).addBox(-5F, 2F, -5F, 10F, 3F, 10F), PartPose.offset(0F, 0F, 0F));
        PartDefinition cap = bell.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(40, 27).addBox(-3F, 0F, -3F, 6F, 2F, 6F), PartPose.offset(0F, 0F, 0F));
        PartDefinition skirt = bell.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(0, 0).addBox(-6F, 5F, -6F, 12F, 2F, 12F), PartPose.offset(0F, 0F, 0F));
        PartDefinition core = bell.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 38).addBox(-2F, 2F, -2F, 4F, 3F, 4F), PartPose.offset(0F, 0F, 0F));
        PartDefinition t0 = bell.addOrReplaceChild("t0", CubeListBuilder.create().texOffs(16, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(4.6F, 7F, 0.0F));
        PartDefinition t0b = t0.addOrReplaceChild("t0b", CubeListBuilder.create().texOffs(20, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t1 = bell.addOrReplaceChild("t1", CubeListBuilder.create().texOffs(24, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(3.3F, 7F, 3.3F));
        PartDefinition t1b = t1.addOrReplaceChild("t1b", CubeListBuilder.create().texOffs(28, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t2 = bell.addOrReplaceChild("t2", CubeListBuilder.create().texOffs(32, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0.0F, 7F, 4.6F));
        PartDefinition t2b = t2.addOrReplaceChild("t2b", CubeListBuilder.create().texOffs(36, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t3 = bell.addOrReplaceChild("t3", CubeListBuilder.create().texOffs(40, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(-3.3F, 7F, 3.3F));
        PartDefinition t3b = t3.addOrReplaceChild("t3b", CubeListBuilder.create().texOffs(44, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t4 = bell.addOrReplaceChild("t4", CubeListBuilder.create().texOffs(48, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(-4.6F, 7F, 0.0F));
        PartDefinition t4b = t4.addOrReplaceChild("t4b", CubeListBuilder.create().texOffs(52, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t5 = bell.addOrReplaceChild("t5", CubeListBuilder.create().texOffs(56, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(-3.3F, 7F, -3.3F));
        PartDefinition t5b = t5.addOrReplaceChild("t5b", CubeListBuilder.create().texOffs(60, 38).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t6 = bell.addOrReplaceChild("t6", CubeListBuilder.create().texOffs(0, 45).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(-0.0F, 7F, -4.6F));
        PartDefinition t6b = t6.addOrReplaceChild("t6b", CubeListBuilder.create().texOffs(4, 45).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition t7 = bell.addOrReplaceChild("t7", CubeListBuilder.create().texOffs(8, 45).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(3.3F, 7F, -3.3F));
        PartDefinition t7b = t7.addOrReplaceChild("t7b", CubeListBuilder.create().texOffs(12, 45).addBox(-0.5F, 0F, -0.5F, 1F, 6F, 1F), PartPose.offset(0F, 6F, 0F));
        PartDefinition a0 = bell.addOrReplaceChild("a0", CubeListBuilder.create().texOffs(40, 14).addBox(-1F, 0F, -1F, 2F, 9F, 2F), PartPose.offset(1.5F, 5F, 1.5F));
        PartDefinition a0b = a0.addOrReplaceChild("a0b", CubeListBuilder.create().texOffs(8, 27).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(0F, 9F, 0F));
        PartDefinition a1 = bell.addOrReplaceChild("a1", CubeListBuilder.create().texOffs(48, 14).addBox(-1F, 0F, -1F, 2F, 9F, 2F), PartPose.offset(-1.5F, 5F, 1.5F));
        PartDefinition a1b = a1.addOrReplaceChild("a1b", CubeListBuilder.create().texOffs(16, 27).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(0F, 9F, 0F));
        PartDefinition a2 = bell.addOrReplaceChild("a2", CubeListBuilder.create().texOffs(56, 14).addBox(-1F, 0F, -1F, 2F, 9F, 2F), PartPose.offset(1.5F, 5F, -1.5F));
        PartDefinition a2b = a2.addOrReplaceChild("a2b", CubeListBuilder.create().texOffs(24, 27).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(0F, 9F, 0F));
        PartDefinition a3 = bell.addOrReplaceChild("a3", CubeListBuilder.create().texOffs(0, 27).addBox(-1F, 0F, -1F, 2F, 9F, 2F), PartPose.offset(-1.5F, 5F, -1.5F));
        PartDefinition a3b = a3.addOrReplaceChild("a3b", CubeListBuilder.create().texOffs(32, 27).addBox(-1F, 0F, -1F, 2F, 7F, 2F), PartPose.offset(0F, 9F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(FaroleroEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float t = ageInTicks + entity.getId() * 7.0F;
        float pulse = Mth.sin(t * 0.09F);
        // la campana late: se ensancha y se aplana, luego se estira
        this.bell.xScale = 1.0F + pulse * 0.07F;
        this.bell.zScale = 1.0F + pulse * 0.07F;
        this.bell.yScale = 1.0F - pulse * 0.10F;
        this.main.y = 3.0F + Mth.sin(t * 0.045F) * 1.0F;
        this.main.xRot = Mth.sin(t * 0.03F) * 0.05F;
        this.main.zRot = Mth.cos(t * 0.027F) * 0.05F;
        float drift = Mth.clamp(limbSwingAmount * 2.0F, 0.0F, 1.0F);
        for (int i = 0; i < 8; i++) {
            float o = i * 0.8F;
            this.tent[i].xRot = Mth.sin(t * 0.10F - o) * 0.18F - pulse * 0.10F;
            this.tent[i].zRot = Mth.cos(t * 0.085F + o) * 0.18F;
            this.tentB[i].xRot = Mth.sin(t * 0.10F - o - 1.0F) * 0.30F - pulse * 0.10F;
            this.tentB[i].zRot = Mth.cos(t * 0.085F + o - 1.0F) * 0.30F;
        }
        for (int i = 0; i < 4; i++) {
            float o = i * 1.6F;
            this.arm[i].xRot = Mth.sin(t * 0.07F - o) * 0.12F - pulse * 0.07F - drift * 0.1F;
            this.arm[i].zRot = Mth.cos(t * 0.06F + o) * 0.12F;
            this.armB[i].xRot = Mth.sin(t * 0.07F - o - 1.2F) * 0.22F;
            this.armB[i].zRot = Mth.cos(t * 0.06F + o - 1.2F) * 0.22F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay,
                               float r, float g, float b, float a) {
        this.root.render(poseStack, buffer, light, overlay, r, g, b, a);
    }
}

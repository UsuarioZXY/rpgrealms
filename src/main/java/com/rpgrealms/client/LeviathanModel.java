package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEntity;
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

/**
 * Modelo del Leviatán: cabeza grande, cuerpo largo, dos aletas laterales,
 * aleta dorsal y cola con aleta horizontal (como en el dibujo de referencia).
 * El frente del modelo apunta hacia -Z.
 */
public class LeviathanModel extends EntityModel<LeviathanEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "leviathan"), "main");

    private final ModelPart body;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart fluke;
    private final ModelPart leftFin;
    private final ModelPart rightFin;

    public LeviathanModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tail1 = this.body.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.fluke = this.tail2.getChild("fluke");
        this.leftFin = this.body.getChild("left_fin");
        this.rightFin = this.body.getChild("right_fin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8F, -8F, -16F, 16F, 16F, 32F),
                PartPose.offset(0F, 16F, 0F));

        // Cabeza y mandíbula
        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 48).addBox(-7F, -6F, -14F, 14F, 9F, 14F),
                PartPose.offset(0F, -1F, -16F));
        head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(0, 72).addBox(-6F, 0F, -13F, 12F, 4F, 13F),
                PartPose.offset(0F, 3F, 0F));
        // Ojos
        head.addOrReplaceChild("left_eye",
                CubeListBuilder.create().texOffs(0, 120).addBox(0F, -1F, -1F, 1F, 2F, 2F),
                PartPose.offset(7F, -1F, -9F));
        head.addOrReplaceChild("right_eye",
                CubeListBuilder.create().texOffs(0, 120).addBox(-1F, -1F, -1F, 1F, 2F, 2F),
                PartPose.offset(-7F, -1F, -9F));

        // Aleta dorsal
        body.addOrReplaceChild("dorsal_fin",
                CubeListBuilder.create().texOffs(56, 48).addBox(-1F, -6F, 0F, 2F, 6F, 10F),
                PartPose.offsetAndRotation(0F, -8F, 2F, 0.35F, 0F, 0F));

        // Aletas laterales
        body.addOrReplaceChild("left_fin",
                CubeListBuilder.create().texOffs(56, 66).addBox(0F, -1F, -3F, 14F, 2F, 8F),
                PartPose.offsetAndRotation(8F, 5F, -8F, 0F, 0F, 0.45F));
        body.addOrReplaceChild("right_fin",
                CubeListBuilder.create().texOffs(56, 66).mirror().addBox(-14F, -1F, -3F, 14F, 2F, 8F),
                PartPose.offsetAndRotation(-8F, 5F, -8F, 0F, 0F, -0.45F));

        // Cola
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 88).addBox(-6F, -6F, 0F, 12F, 12F, 16F),
                PartPose.offset(0F, 0F, 16F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(56, 88).addBox(-4F, -4F, 0F, 8F, 8F, 14F),
                PartPose.offset(0F, 0F, 16F));
        tail2.addOrReplaceChild("fluke",
                CubeListBuilder.create().texOffs(0, 116).addBox(-14F, -1F, 0F, 28F, 2F, 8F),
                PartPose.offset(0F, 0F, 14F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(LeviathanEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.xRot = headPitch * ((float) Math.PI / 180F);

        float t = ageInTicks * 0.08F;
        this.tail1.xRot = Mth.sin(t) * 0.08F;
        this.tail2.xRot = Mth.sin(t - 0.7F) * 0.12F;
        this.fluke.xRot = Mth.sin(t - 1.4F) * 0.18F;

        this.leftFin.zRot = 0.45F + Mth.sin(t * 1.3F) * 0.12F;
        this.rightFin.zRot = -0.45F - Mth.sin(t * 1.3F) * 0.12F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

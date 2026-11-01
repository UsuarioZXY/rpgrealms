package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEggEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/** Huevo ovalado hecho de nueve capas apiladas (ancho abajo, estrecho arriba). GENERADO junto con sus texturas. */
public class LeviathanEggModel extends EntityModel<LeviathanEggEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(RpgRealms.MODID, "leviathan_egg"), "main");

    private final ModelPart root;

    public LeviathanEggModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition r = mesh.getRoot();
        PartDefinition egg = r.addOrReplaceChild("egg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        egg.addOrReplaceChild("l0", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -4F, -10.0F, 20F, 4F, 20F), PartPose.ZERO);
        egg.addOrReplaceChild("l1", CubeListBuilder.create().texOffs(0, 24).addBox(-13.0F, -8F, -13.0F, 26F, 4F, 26F), PartPose.ZERO);
        egg.addOrReplaceChild("l2", CubeListBuilder.create().texOffs(0, 54).addBox(-15.0F, -14F, -15.0F, 30F, 6F, 30F), PartPose.ZERO);
        egg.addOrReplaceChild("l3", CubeListBuilder.create().texOffs(0, 90).addBox(-16.0F, -22F, -16.0F, 32F, 8F, 32F), PartPose.ZERO);
        egg.addOrReplaceChild("l4", CubeListBuilder.create().texOffs(0, 130).addBox(-15.0F, -30F, -15.0F, 30F, 8F, 30F), PartPose.ZERO);
        egg.addOrReplaceChild("l5", CubeListBuilder.create().texOffs(0, 168).addBox(-13.0F, -36F, -13.0F, 26F, 6F, 26F), PartPose.ZERO);
        egg.addOrReplaceChild("l6", CubeListBuilder.create().texOffs(0, 200).addBox(-10.0F, -40F, -10.0F, 20F, 4F, 20F), PartPose.ZERO);
        egg.addOrReplaceChild("l7", CubeListBuilder.create().texOffs(0, 224).addBox(-7.0F, -43F, -7.0F, 14F, 3F, 14F), PartPose.ZERO);
        egg.addOrReplaceChild("l8", CubeListBuilder.create().texOffs(0, 241).addBox(-4.0F, -45F, -4.0F, 8F, 2F, 8F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 256);
    }

    @Override
    public void setupAnim(LeviathanEggEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

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

/** Huevo hecho de cuatro bloques apilados (base ancha, vientre, hombro, punta). */
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
        egg.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -10.0F, -11.0F, 22.0F, 10.0F, 22.0F), PartPose.ZERO);
        egg.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 32).addBox(-13.0F, -24.0F, -13.0F, 26.0F, 14.0F, 26.0F), PartPose.ZERO);
        egg.addOrReplaceChild("shoulder", CubeListBuilder.create().texOffs(0, 72).addBox(-11.0F, -34.0F, -11.0F, 22.0F, 10.0F, 22.0F), PartPose.ZERO);
        egg.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(0, 104).addBox(-7.0F, -40.0F, -7.0F, 14.0F, 6.0F, 14.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
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

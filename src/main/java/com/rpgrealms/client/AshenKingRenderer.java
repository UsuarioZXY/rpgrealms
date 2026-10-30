package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.entity.AshenKingEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AshenKingRenderer extends HumanoidMobRenderer<AshenKingEntity, HumanoidModel<AshenKingEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/zombie/husk.png");

    public AshenKingRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.9F);
    }

    @Override
    protected void scale(AshenKingEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.8F, 1.8F, 1.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(AshenKingEntity entity) {
        return TEXTURE;
    }
}

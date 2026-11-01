package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanTeenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LeviathanTeenRenderer extends MobRenderer<LeviathanTeenEntity, LeviathanTeenModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_teen.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_teen_glow.png");

    public LeviathanTeenRenderer(EntityRendererProvider.Context context) {
        super(context, new LeviathanTeenModel(context.bakeLayer(LeviathanTeenModel.LAYER)), 5.0F);
        this.addLayer(new LeviathanGlowLayer<>(this, GLOW, 1.1F));
    }

    @Override
    protected void scale(LeviathanTeenEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(4.4F, 4.4F, 4.4F);
        poseStack.translate(0.0F, 0.0F, -1.25F);
    }

    @Override
    protected float getFlipDegrees(LeviathanTeenEntity entity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanTeenEntity entity) {
        return TEXTURE;
    }
}

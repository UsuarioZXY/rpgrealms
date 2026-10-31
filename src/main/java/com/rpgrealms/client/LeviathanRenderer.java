package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LeviathanRenderer extends MobRenderer<LeviathanEntity, LeviathanModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_glow.png");

    public LeviathanRenderer(EntityRendererProvider.Context context) {
        super(context, new LeviathanModel(context.bakeLayer(LeviathanModel.LAYER)), 6.0F);
        this.addLayer(new LeviathanGlowLayer<>(this, GLOW, 1.15F));
    }

    @Override
    protected void scale(LeviathanEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(4.5F, 4.5F, 4.5F);
        poseStack.translate(0.0F, 0.0F, -1.25F);
    }

    @Override
    protected float getFlipDegrees(LeviathanEntity entity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEntity entity) {
        return TEXTURE;
    }
}

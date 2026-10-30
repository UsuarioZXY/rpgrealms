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

    public LeviathanRenderer(EntityRendererProvider.Context context) {
        super(context, new LeviathanModel(context.bakeLayer(LeviathanModel.LAYER)), 3.0F);
        this.addLayer(new LeviathanGlowLayer(this));
    }

    @Override
    protected void scale(LeviathanEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(2.4F, 2.4F, 2.4F);
        // El modelo mide ~160 px de largo con el origen en el pecho: lo centramos
        // sobre la posición de la entidad para que gire sobre su centro.
        poseStack.translate(0.0F, 0.0F, -1.25F);
    }

    @Override
    protected float getFlipDegrees(LeviathanEntity entity) {
        // Al morir queda flotando panza arriba.
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEntity entity) {
        return TEXTURE;
    }
}

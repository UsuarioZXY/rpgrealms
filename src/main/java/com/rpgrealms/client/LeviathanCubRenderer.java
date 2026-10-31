package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanCubEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LeviathanCubRenderer extends MobRenderer<LeviathanCubEntity, LeviathanCubModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_cub.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_cub_glow.png");

    public LeviathanCubRenderer(EntityRendererProvider.Context context) {
        super(context, new LeviathanCubModel(context.bakeLayer(LeviathanCubModel.LAYER)), 2.0F);
        this.addLayer(new LeviathanGlowLayer<>(this, GLOW, 1.0F));
    }

    @Override
    protected void scale(LeviathanCubEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.8F, 1.8F, 1.8F);
        // Centra el modelo (~160 px de largo, origen en el pecho) sobre la posición de la entidad.
        poseStack.translate(0.0F, 0.0F, -1.25F);
    }

    @Override
    protected float getFlipDegrees(LeviathanCubEntity entity) {
        return 180.0F; // al morir queda flotando panza arriba
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanCubEntity entity) {
        return TEXTURE;
    }
}

package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.SeaFoxEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SeaFoxRenderer extends MobRenderer<SeaFoxEntity, SeaFoxModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RpgRealms.MODID, "textures/entity/sea_fox.png");
    private static final ResourceLocation GLOW = new ResourceLocation(RpgRealms.MODID, "textures/entity/sea_fox_glow.png");

    public SeaFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaFoxModel(context.bakeLayer(SeaFoxModel.LAYER)), 0.4F);
        this.addLayer(new SimpleGlowLayer<>(this, GLOW, 0.75F, 1.0F, 1.0F, 0.1F, 0.35F));
    }

    @Override
    protected void scale(SeaFoxEntity entity, PoseStack poseStack, float partialTick) {
        float s = entity.isBaby() ? 0.6F : 0.9F;
        poseStack.scale(s, s, s);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaFoxEntity entity) {
        return TEXTURE;
    }
}

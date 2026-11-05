package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.FaroleroEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FaroleroRenderer extends MobRenderer<FaroleroEntity, FaroleroModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RpgRealms.MODID, "textures/entity/farolero.png");
    private static final ResourceLocation GLOW = new ResourceLocation(RpgRealms.MODID, "textures/entity/farolero_glow.png");

    public FaroleroRenderer(EntityRendererProvider.Context context) {
        super(context, new FaroleroModel(context.bakeLayer(FaroleroModel.LAYER)), 0.3F);
        this.addLayer(new SimpleGlowLayer<>(this, GLOW, 1.0F, 0.85F, 0.55F, 0.09F, 0.45F));
    }

    @Override
    protected void scale(FaroleroEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.1F, 1.1F, 1.1F);
    }

    /** Campana y tentáculos translúcidos. */
    @Override
    protected RenderType getRenderType(FaroleroEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return bodyVisible || translucent ? RenderType.entityTranslucent(TEXTURE) : super.getRenderType(entity, bodyVisible, translucent, glowing);
    }

    @Override
    public ResourceLocation getTextureLocation(FaroleroEntity entity) {
        return TEXTURE;
    }
}

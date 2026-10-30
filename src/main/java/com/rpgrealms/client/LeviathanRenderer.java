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
    }

    @Override
    protected void scale(LeviathanEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(3.2F, 3.2F, 3.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEntity entity) {
        return TEXTURE;
    }
}

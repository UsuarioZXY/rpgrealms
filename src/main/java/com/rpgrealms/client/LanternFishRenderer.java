package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LanternFishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LanternFishRenderer extends MobRenderer<LanternFishEntity, LanternFishModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/lantern_fish.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/lantern_fish_glow.png");

    public LanternFishRenderer(EntityRendererProvider.Context context) {
        super(context, new LanternFishModel(context.bakeLayer(LanternFishModel.LAYER)), 0.2F);
        this.addLayer(new GlowLayer(this));
    }

    @Override
    protected void scale(LanternFishEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.55F, 0.55F, 0.55F);
    }

    @Override
    protected void setupRotations(LanternFishEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        if (!entity.isInWater()) {
            poseStack.translate(0.1F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(LanternFishEntity entity) {
        return TEXTURE;
    }

    /** Runa, aletas y señuelo brillan siempre; laten suavemente y destellan al estar alarmado. */
    static class GlowLayer extends RenderLayer<LanternFishEntity, LanternFishModel> {
        GlowLayer(RenderLayerParent<LanternFishEntity, LanternFishModel> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LanternFishEntity entity,
                           float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                           float netHeadYaw, float headPitch) {
            if (entity.isInvisible()) {
                return;
            }
            float pulse = 0.78F + 0.22F * Mth.sin(ageInTicks * 0.12F + entity.getId());
            float r = 1.0F, g = 1.0F, b = 1.0F;
            if (entity.getAlarm() > 0) {
                float f = 0.6F + 0.4F * Mth.sin(ageInTicks * 1.2F);
                r = 1.0F; g = 0.85F; b = 0.55F;
                pulse = 0.8F + f * 0.4F;
            }
            VertexConsumer vc = buffer.getBuffer(RenderType.eyes(GLOW));
            this.getParentModel().renderToBuffer(poseStack, vc, 15728640, OverlayTexture.NO_OVERLAY,
                    r * pulse, g * pulse, b * pulse, 1.0F);
        }
    }
}

package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEggEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Huevo que se mece cada vez más fuerte según se acerca la eclosión, con motas luminosas. */
public class LeviathanEggRenderer extends EntityRenderer<LeviathanEggEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_egg.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_egg_glow.png");

    private final LeviathanEggModel model;

    public LeviathanEggRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LeviathanEggModel(context.bakeLayer(LeviathanEggModel.LAYER));
        this.shadowRadius = 1.0F;
    }

    @Override
    public void render(LeviathanEggEntity egg, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float t = egg.tickCount + partialTick;
        float progress = 1.0F - Mth.clamp(egg.getTicksLeft() / (float) LeviathanEggEntity.HATCH_TICKS, 0.0F, 1.0F);
        float amp = progress > 0.6F ? (progress - 0.6F) / 0.4F * 9.0F : 0.0F;   // grados

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(t * 0.9F) * amp));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(t * 0.7F) * amp * 0.6F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        VertexConsumer body = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, body, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        float pulse = 0.55F + 0.35F * Mth.sin(t * (0.08F + progress * 0.25F)) + progress * 0.2F;
        VertexConsumer glow = buffer.getBuffer(RenderType.eyes(GLOW));
        this.model.renderToBuffer(poseStack, glow, 15728640, OverlayTexture.NO_OVERLAY,
                0.3F * pulse, 0.9F * pulse, 1.0F * pulse, 1.0F);
        poseStack.popPose();

        super.render(egg, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEggEntity egg) {
        return TEXTURE;
    }
}

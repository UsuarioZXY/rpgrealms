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

/**
 * Huevo vivo: late como un corazón (más rápido al acercarse la eclosión), dos redes de luz (anillos y meridianos)
 * que pulsan en contrafase como una onda, se estremece a ratos y se va agrietando.
 */
public class LeviathanEggRenderer extends EntityRenderer<LeviathanEggEntity> {
    private static final ResourceLocation TEXTURE = tex("leviathan_egg");
    private static final ResourceLocation RINGS = tex("leviathan_egg_glow");
    private static final ResourceLocation MERIDIANS = tex("leviathan_egg_glow2");
    private static final ResourceLocation CRACKS = tex("leviathan_egg_cracks");

    private final LeviathanEggModel model;

    private static ResourceLocation tex(String name) {
        return new ResourceLocation(RpgRealms.MODID, "textures/entity/" + name + ".png");
    }

    public LeviathanEggRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LeviathanEggModel(context.bakeLayer(LeviathanEggModel.LAYER));
        this.shadowRadius = 1.3F;
    }

    @Override
    public void render(LeviathanEggEntity egg, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float t = egg.tickCount + partialTick;
        float progress = 1.0F - Mth.clamp(egg.getTicksLeft() / (float) LeviathanEggEntity.HATCH_TICKS, 0.0F, 1.0F);

        // Estremecimientos a ratos, cada vez más frecuentes y fuertes
        float gate = Mth.clamp(Mth.sin(t * 0.07F + egg.getId() * 1.7F) * 2.5F - 0.9F + progress * 1.5F, 0.0F, 1.0F);
        float amp = (progress > 0.25F ? (progress - 0.25F) / 0.75F : 0.0F) * (3.0F + 9.0F * progress) * gate
                + progress * 0.6F * Mth.sin(t * 2.3F);
        // Latido: dos golpes seguidos (lub-dub) cuya frecuencia sube
        float beatRate = 0.16F + progress * 0.18F;
        float ph = (t * beatRate) % 6.2832F;
        float beat = Math.max(0.0F, Mth.sin(ph * 2.0F)) * (ph < 3.1416F ? 1.0F : 0.0F)
                + 0.6F * Math.max(0.0F, Mth.sin((ph - 1.4F) * 2.0F)) * (ph > 1.4F && ph < 4.5F ? 1.0F : 0.0F);
        float pulseScale = 1.0F + beat * (0.012F + 0.02F * progress);

        poseStack.pushPose();
        poseStack.translate(0.0F, Math.max(0.0F, Mth.sin(t * 1.1F)) * 0.06F * gate * progress, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(t * 0.9F) * amp));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(t * 0.7F) * amp * 0.6F));
        poseStack.scale(-pulseScale, -pulseScale, pulseScale);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        VertexConsumer body = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, body, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Dos redes de luz en contrafase: la energía "sube y baja" por el huevo
        float speed = 0.07F + progress * 0.22F;
        float a = 0.45F + 0.4F * Mth.sin(t * speed) + beat * 0.5F * (0.4F + progress);
        float b = 0.45F + 0.4F * Mth.sin(t * speed + 2.4F) + beat * 0.5F * (0.4F + progress);
        VertexConsumer g1 = buffer.getBuffer(RenderType.eyes(RINGS));
        this.model.renderToBuffer(poseStack, g1, 15728640, OverlayTexture.NO_OVERLAY,
                0.3F * a, 0.9F * a, 1.0F * a, 1.0F);
        VertexConsumer g2 = buffer.getBuffer(RenderType.eyes(MERIDIANS));
        this.model.renderToBuffer(poseStack, g2, 15728640, OverlayTexture.NO_OVERLAY,
                0.5F * b, 0.7F * b, 1.0F * b, 1.0F);

        // Grietas que aparecen poco a poco y brillan al final
        if (progress > 0.35F) {
            float ca = Mth.clamp((progress - 0.35F) / 0.55F, 0.0F, 1.0F);
            poseStack.scale(1.012F, 1.004F, 1.012F);
            VertexConsumer cr = buffer.getBuffer(RenderType.entityTranslucent(CRACKS));
            this.model.renderToBuffer(poseStack, cr, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, ca);
        }
        poseStack.popPose();

        super.render(egg, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEggEntity egg) {
        return TEXTURE;
    }
}

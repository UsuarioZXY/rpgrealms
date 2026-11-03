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
        // Golpe: se inclina alejándose del golpe, rebota y se aplasta un poco
        float hitK = Mth.clamp((egg.getHitTicks() - partialTick) / 10.0F, 0.0F, 1.0F);
        float squash = 1.0F;
        if (hitK > 0.0F) {
            float dirRad = egg.getHitDir() * 0.017453292F;
            float ang = Mth.sin(hitK * 9.0F) * hitK * 16.0F;
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(dirRad) * ang));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-Mth.cos(dirRad) * ang));
            squash = 1.0F - 0.07F * hitK * Math.abs(Mth.sin(hitK * 9.0F));
        }
        // Últimos 5 s: sacudida fuerte y destello
        float last = Mth.clamp(1.0F - egg.getTicksLeft() / 100.0F, 0.0F, 1.0F);
        if (last > 0.0F && egg.getTicksLeft() > 0) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(t * 3.1F) * 4.0F * last));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(t * 2.7F) * 3.0F * last));
        }
        poseStack.scale(-pulseScale, -pulseScale * squash, pulseScale);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        VertexConsumer body = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, body, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Dos redes de luz en contrafase: la energía "sube y baja" por el huevo
        float speed = 0.07F + progress * 0.22F;
        float flash = last * 0.9F + hitK * 0.8F;
        float a = 0.45F + 0.4F * Mth.sin(t * speed) + beat * 0.5F * (0.4F + progress) + flash;
        float b = 0.45F + 0.4F * Mth.sin(t * speed + 2.4F) + beat * 0.5F * (0.4F + progress) + flash;
        VertexConsumer g1 = buffer.getBuffer(RenderType.eyes(RINGS));
        this.model.renderToBuffer(poseStack, g1, 15728640, OverlayTexture.NO_OVERLAY,
                0.3F * a, 0.9F * a, 1.0F * a, 1.0F);
        VertexConsumer g2 = buffer.getBuffer(RenderType.eyes(MERIDIANS));
        this.model.renderToBuffer(poseStack, g2, 15728640, OverlayTexture.NO_OVERLAY,
                0.5F * b, 0.7F * b, 1.0F * b, 1.0F);

        // Grietas que aparecen poco a poco y brillan al final
        float damage = Mth.clamp((1.0F - egg.getHealthFraction()) * 1.3F, 0.0F, 1.0F);
        float ca = Math.max(damage, progress > 0.35F ? Mth.clamp((progress - 0.35F) / 0.55F, 0.0F, 1.0F) : 0.0F);
        if (ca > 0.0F) {
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

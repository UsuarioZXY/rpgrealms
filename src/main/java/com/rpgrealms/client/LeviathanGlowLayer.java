package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.entity.AbstractLeviathan;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Bioluminiscencia: puntos, venas y ojos que brillan en la oscuridad con un pulso suave.
 * La textura es blanca y se tiñe: cian en calma, rojo cuando se enfada,
 * y blanco incandescente mientras el adulto carga o dispara el rayo.
 */
public class LeviathanGlowLayer<T extends AbstractLeviathan, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;
    private final float boost;

    public LeviathanGlowLayer(RenderLayerParent<T, M> parent, ResourceLocation texture, float boost) {
        super(parent);
        this.texture = texture;
        this.boost = boost;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        boolean angry = entity.isAggressive();
        int phase = entity.getPhase();

        float speed = angry ? 0.25F : 0.07F;
        float pulse = 0.65F + 0.35F * Mth.sin(ageInTicks * speed);
        float r = angry ? 1.0F : 0.30F;
        float g = angry ? 0.18F : 0.85F;
        float b = angry ? 0.12F : 1.00F;

        if (phase == 2) {          // cargando el rayo: se enciende en blanco azulado, latido cada vez más rápido
            float c = Mth.clamp(entity.getPhaseTimer() / 50.0F, 0.0F, 1.0F);
            pulse = 0.6F + 0.4F * Mth.sin(ageInTicks * (0.3F + c * 1.4F));
            pulse *= 0.7F + 0.5F * c;
            r = 0.75F; g = 0.95F; b = 1.0F;
        } else if (phase == 3) {   // disparando: incandescente, con vibración
            pulse = 0.9F + 0.1F * Mth.sin(ageInTicks * 3.0F);
            r = 1.0F; g = 1.0F; b = 1.0F;
        } else if (phase == 1) {   // ráfaga de orbes: destello en cada disparo
            pulse = 0.8F + 0.2F * (entity.getRecoil() / 6.0F);
            r = 0.55F; g = 1.0F; b = 1.0F;
        }

        float k = Math.min(1.0F, pulse * this.boost);
        VertexConsumer vc = buffer.getBuffer(RenderType.eyes(this.texture));
        this.getParentModel().renderToBuffer(poseStack, vc, 15728640, OverlayTexture.NO_OVERLAY,
                r * k, g * k, b * k, 1.0F);
    }
}

package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Bioluminiscencia: puntos y ojos que brillan en la oscuridad con un pulso suave.
 * La textura es blanca y se tiñe: cian en calma, rojo cuando el Leviatán se enfada.
 */
public class LeviathanGlowLayer extends RenderLayer<LeviathanEntity, LeviathanModel> {
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_glow.png");

    public LeviathanGlowLayer(RenderLayerParent<LeviathanEntity, LeviathanModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       LeviathanEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        boolean angry = entity.isAggressive();
        float speed = angry ? 0.25F : 0.07F;
        float pulse = 0.65F + 0.35F * Mth.sin(ageInTicks * speed);

        float r = angry ? 1.0F : 0.30F;
        float g = angry ? 0.18F : 0.85F;
        float b = angry ? 0.12F : 1.00F;

        VertexConsumer vc = buffer.getBuffer(RenderType.eyes(GLOW));
        this.getParentModel().renderToBuffer(poseStack, vc, 15728640, OverlayTexture.NO_OVERLAY,
                r * pulse, g * pulse, b * pulse, 1.0F);
    }
}

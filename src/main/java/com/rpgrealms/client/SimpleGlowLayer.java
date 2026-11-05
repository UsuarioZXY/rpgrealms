package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/** Capa de brillo genérica: textura iluminada y teñida, con un pulso suave. */
public class SimpleGlowLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;
    private final float r, g, b, speed, depth;

    public SimpleGlowLayer(RenderLayerParent<T, M> parent, ResourceLocation texture,
                           float r, float g, float b, float speed, float depth) {
        super(parent);
        this.texture = texture;
        this.r = r;
        this.g = g;
        this.b = b;
        this.speed = speed;
        this.depth = depth;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        float pulse = 1.0F - this.depth + this.depth * (0.5F + 0.5F * Mth.sin(ageInTicks * this.speed + entity.getId() * 1.7F));
        VertexConsumer vc = buffer.getBuffer(RenderType.eyes(this.texture));
        this.getParentModel().renderToBuffer(poseStack, vc, 15728640, OverlayTexture.NO_OVERLAY,
                this.r * pulse, this.g * pulse, this.b * pulse, 1.0F);
    }
}

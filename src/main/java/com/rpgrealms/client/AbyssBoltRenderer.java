package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.entity.AbyssBoltEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.util.Mth;

/** Orbe abisal: sprite brillante que late y se dibuja dos veces (halo grande + núcleo). */
public class AbyssBoltRenderer extends ThrownItemRenderer<AbyssBoltEntity> {

    public AbyssBoltRenderer(EntityRendererProvider.Context context) {
        super(context, 3.0F, true);
    }

    @Override
    public void render(AbyssBoltEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float t = entity.tickCount + partialTick;
        float pulse = 1.0F + 0.22F * Mth.sin(t * 0.7F);

        // Halo exterior
        poseStack.pushPose();
        poseStack.scale(pulse * 1.9F, pulse * 1.9F, pulse * 1.9F);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, 15728880);
        poseStack.popPose();

        // Núcleo
        poseStack.pushPose();
        poseStack.scale(pulse, pulse, pulse);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, 15728880);
        poseStack.popPose();
    }
}

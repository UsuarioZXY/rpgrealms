package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.entity.AbyssBoltEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Orbe abisal: núcleo en forma de estrella que late, halo más grande y tres satélites luminosos que orbitan.
 */
public class AbyssBoltRenderer extends EntityRenderer<AbyssBoltEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/beacon_beam.png");

    public AbyssBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(AbyssBoltEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float t = entity.tickCount + partialTick;
        float pulse = 1.0F + 0.2F * Mth.sin(t * 0.8F);

        BeamRenderHelper.renderFlare(poseStack, buffer, Vec3.ZERO, 0.75F * pulse, t);          // núcleo
        BeamRenderHelper.renderFlare(poseStack, buffer, Vec3.ZERO, 1.25F * pulse, t * 0.5F);    // halo
        for (int i = 0; i < 3; i++) {
            double a = t * 0.35D + i * Math.PI * 2.0D / 3.0D;
            Vec3 p = new Vec3(Math.cos(a) * 1.1D, Math.sin(a * 1.3D) * 0.5D, Math.sin(a) * 1.1D);
            BeamRenderHelper.renderFlare(poseStack, buffer, p, 0.22F, t + i * 3.0F);            // satélites
        }
    }

    @Override
    public ResourceLocation getTextureLocation(AbyssBoltEntity entity) {
        return TEXTURE;
    }
}

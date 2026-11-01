package com.bluelock.mod.client;

import com.bluelock.mod.BlueLockMod;
import com.bluelock.mod.entity.SoccerBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/** Dibuja el balon como una esfera 3D que gira segun rueda. */
public class SoccerBallRenderer extends EntityRenderer<SoccerBallEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BlueLockMod.MODID, "textures/entity/soccer_ball.png");
    private static final float RADIUS = 0.25F;
    private static final int SEGMENTS = 20;
    private static final int RINGS = 14;

    public SoccerBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.25F;
    }

    @Override
    public void render(SoccerBallEntity ball, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, RADIUS, 0.0);
        poseStack.mulPose(ball.rollRot);

        VertexConsumer vc = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        PoseStack.Pose last = poseStack.last();
        Matrix4f pose = last.pose();
        Matrix3f normal = last.normal();

        for (int i = 0; i < RINGS; i++) {
            float phi0 = (float) (Math.PI / 2 - Math.PI * i / RINGS);
            float phi1 = (float) (Math.PI / 2 - Math.PI * (i + 1) / RINGS);
            float v0 = (float) i / RINGS;
            float v1 = (float) (i + 1) / RINGS;
            for (int j = 0; j < SEGMENTS; j++) {
                float l0 = (float) (2 * Math.PI * j / SEGMENTS);
                float l1 = (float) (2 * Math.PI * (j + 1) / SEGMENTS);
                float u0 = (float) j / SEGMENTS;
                float u1 = (float) (j + 1) / SEGMENTS;
                vertex(vc, pose, normal, phi0, l0, u0, v0, light);
                vertex(vc, pose, normal, phi0, l1, u1, v0, light);
                vertex(vc, pose, normal, phi1, l1, u1, v1, light);
                vertex(vc, pose, normal, phi1, l0, u0, v1, light);
            }
        }

        poseStack.popPose();
        super.render(ball, yaw, partialTick, poseStack, buffers, light);
    }

    private static void vertex(VertexConsumer vc, Matrix4f pose, Matrix3f normal,
                               float phi, float lon, float u, float v, int light) {
        float cp = (float) Math.cos(phi);
        float nx = cp * (float) Math.cos(lon);
        float ny = (float) Math.sin(phi);
        float nz = cp * (float) Math.sin(lon);
        vc.vertex(pose, nx * RADIUS, ny * RADIUS, nz * RADIUS)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(SoccerBallEntity entity) {
        return TEXTURE;
    }
}

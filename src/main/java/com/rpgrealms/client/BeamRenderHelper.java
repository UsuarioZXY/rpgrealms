package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Dibuja haces luminosos (prismas translúcidos) con la textura del haz de la baliza.
 * Se usa para el rayo del Leviatán y para el destello de carga.
 */
public final class BeamRenderHelper {
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

    private BeamRenderHelper() {
    }

    /**
     * Rayo completo entre dos puntos (relativos al origen del PoseStack): tres capas que giran y laten.
     */
    public static void renderBeam(PoseStack ps, MultiBufferSource buffer, Vec3 from, Vec3 to, float time) {
        Vec3 d = to.subtract(from);
        double len = d.length();
        if (len < 0.5D) {
            return;
        }
        Vec3 dir = d.scale(1.0D / len);

        ps.pushPose();
        ps.translate(from.x, from.y, from.z);
        // Alinea el eje +Y del prisma con la dirección del rayo.
        ps.mulPose(new Quaternionf().rotationTo(0.0F, 1.0F, 0.0F, (float) dir.x, (float) dir.y, (float) dir.z));

        float pulse = 1.0F + 0.15F * Mth.sin(time * 1.9F);
        float scroll = time * 0.12F;
        VertexConsumer vc = buffer.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, true));

        // Capa exterior: halo cian grande
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(time * 14.0F));
        prism(ps, vc, 0.2F, 0.75F, 1.0F, 0.28F, (float) len, 1.9F * pulse, scroll);
        ps.popPose();

        // Capa media: azul brillante que gira al revés
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(-time * 22.0F + 45.0F));
        prism(ps, vc, 0.45F, 0.9F, 1.0F, 0.5F, (float) len, 1.1F * pulse, -scroll * 1.5F);
        ps.popPose();

        // Núcleo blanco
        ps.pushPose();
        ps.mulPose(Axis.YP.rotationDegrees(time * 30.0F));
        prism(ps, vc, 1.0F, 1.0F, 1.0F, 0.95F, (float) len, 0.5F * pulse, scroll * 2.0F);
        ps.popPose();

        ps.popPose();
    }

    /** Destello en forma de estrella (tres haces cruzados) que gira: carga de la boca / impacto. */
    public static void renderFlare(PoseStack ps, MultiBufferSource buffer, Vec3 center, float size, float time) {
        if (size <= 0.05F) {
            return;
        }
        VertexConsumer vc = buffer.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, true));
        ps.pushPose();
        ps.translate(center.x, center.y, center.z);
        ps.mulPose(Axis.YP.rotationDegrees(time * 25.0F));
        ps.mulPose(Axis.XP.rotationDegrees(time * 17.0F));
        float pulse = 1.0F + 0.2F * Mth.sin(time * 2.3F);
        float s = size * pulse;

        for (int axis = 0; axis < 3; axis++) {
            ps.pushPose();
            if (axis == 1) {
                ps.mulPose(Axis.ZP.rotationDegrees(90.0F));
            } else if (axis == 2) {
                ps.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            ps.translate(0.0D, -s, 0.0D);
            prism(ps, vc, 0.3F, 0.85F, 1.0F, 0.55F, s * 2.0F, s * 0.30F, time * 0.1F);
            prism(ps, vc, 1.0F, 1.0F, 1.0F, 0.9F, s * 2.0F, s * 0.12F, time * 0.2F);
            ps.popPose();
        }
        ps.popPose();
    }

    // ---------------------------------------------------------------- geometría

    /** Prisma cuadrado que va de y=0 a y=length, con el radio dado, visible por dentro y por fuera. */
    private static void prism(PoseStack ps, VertexConsumer vc, float r, float g, float b, float a,
                              float length, float radius, float scroll) {
        PoseStack.Pose pose = ps.last();
        Matrix4f m4 = pose.pose();
        Matrix3f m3 = pose.normal();
        float v0 = -scroll;
        float v1 = length * 0.5F - scroll;
        float[][] c = {{-radius, -radius}, {radius, -radius}, {radius, radius}, {-radius, radius}};
        for (int i = 0; i < 4; i++) {
            float[] p1 = c[i];
            float[] p2 = c[(i + 1) % 4];
            quad(m4, m3, vc, r, g, b, a, 0.0F, length, p1[0], p1[1], p2[0], p2[1], 0.0F, 1.0F, v0, v1);
            quad(m4, m3, vc, r, g, b, a, 0.0F, length, p2[0], p2[1], p1[0], p1[1], 0.0F, 1.0F, v0, v1);
        }
    }

    private static void quad(Matrix4f m4, Matrix3f m3, VertexConsumer vc, float r, float g, float b, float a,
                             float yMin, float yMax, float x1, float z1, float x2, float z2,
                             float u1, float u2, float v1, float v2) {
        vertex(m4, m3, vc, r, g, b, a, yMax, x1, z1, u2, v1);
        vertex(m4, m3, vc, r, g, b, a, yMin, x1, z1, u2, v2);
        vertex(m4, m3, vc, r, g, b, a, yMin, x2, z2, u1, v2);
        vertex(m4, m3, vc, r, g, b, a, yMax, x2, z2, u1, v1);
    }

    private static void vertex(Matrix4f m4, Matrix3f m3, VertexConsumer vc, float r, float g, float b, float a,
                               float y, float x, float z, float u, float v) {
        vc.vertex(m4, x, y, z).color(r, g, b, a).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(m3, 0.0F, 1.0F, 0.0F).endVertex();
    }
}

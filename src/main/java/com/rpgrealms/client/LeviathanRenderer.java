package com.rpgrealms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.LeviathanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LeviathanRenderer extends MobRenderer<LeviathanEntity, LeviathanModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan.png");
    private static final ResourceLocation GLOW =
            new ResourceLocation(RpgRealms.MODID, "textures/entity/leviathan_glow.png");

    public LeviathanRenderer(EntityRendererProvider.Context context) {
        super(context, new LeviathanModel(context.bakeLayer(LeviathanModel.LAYER)), 6.0F);
        this.addLayer(new LeviathanGlowLayer<>(this, GLOW, 1.15F));
    }

    @Override
    public void render(LeviathanEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        int phase = entity.getPhase();
        if (phase != LeviathanEntity.PHASE_BEAM_CHARGE && phase != LeviathanEntity.PHASE_BEAM_FIRE) {
            return;
        }
        float time = entity.tickCount + partialTick;
        Vec3 mouth = entity.getMouthOffset(partialTick);

        if (phase == LeviathanEntity.PHASE_BEAM_CHARGE) {
            // Núcleo de energía que crece en la boca mientras carga.
            float charge = Mth.clamp((entity.getPhaseTimer() + partialTick) / LeviathanEntity.CHARGE_TICKS, 0.0F, 1.0F);
            BeamRenderHelper.renderFlare(poseStack, buffer, mouth, 0.4F + charge * 2.6F, time);
        } else {
            Vec3 end = entity.getBeamEndOffset();
            BeamRenderHelper.renderBeam(poseStack, buffer, mouth, end, time);
            BeamRenderHelper.renderFlare(poseStack, buffer, mouth, 2.2F, time);   // foco en la boca
            BeamRenderHelper.renderFlare(poseStack, buffer, end, 1.6F, time + 7.0F); // impacto
        }
    }

    @Override
    protected void scale(LeviathanEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(4.5F, 4.5F, 4.5F);
        poseStack.translate(0.0F, 0.0F, -1.25F);
    }

    @Override
    protected float getFlipDegrees(LeviathanEntity entity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanEntity entity) {
        return TEXTURE;
    }
}

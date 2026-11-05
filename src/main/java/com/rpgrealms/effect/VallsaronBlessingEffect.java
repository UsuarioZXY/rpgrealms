package com.rpgrealms.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

/**
 * Bendición del Vallsaron: respirar bajo el agua, nadar más rápido, ver en la oscuridad del mar,
 * corazones de absorción y un aura de runas. Dura 10 minutos desde la última vez que un Vallsaron en paz te bendijo.
 */
public class VallsaronBlessingEffect extends MobEffect {
    public VallsaronBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x4DE8FF);
    }

    private static final java.util.UUID SWIM_ID = java.util.UUID.fromString("5f2b8f0e-3a53-4a9c-9a64-7d0c11a6b001");

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
        super.addAttributeModifiers(entity, map, amp);
        // La velocidad de nado se aplica aquí (y no en el constructor) para no depender del orden de registro de atributos.
        net.minecraft.world.entity.ai.attributes.AttributeInstance swim = map.getInstance(ForgeMod.SWIM_SPEED.get());
        if (swim != null && swim.getModifier(SWIM_ID) == null) {
            swim.addTransientModifier(new AttributeModifier(SWIM_ID, "Vallsaron blessing", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), 6.0F));
        if (!entity.level().isClientSide && entity.level() instanceof ServerLevel sl) {
            // al recibirla: estallido de luz que sube en espiral y campanada
            for (int i = 0; i < 48; i++) {
                double ang = i * 0.5D;
                double h = i / 48.0D * 2.4D;
                sl.sendParticles(ParticleTypes.END_ROD, entity.getX() + Math.cos(ang) * 1.2D, entity.getY() + h, entity.getZ() + Math.sin(ang) * 1.2D, 0, 0.0D, 0.1D, 0.0D, 1.0D);
                sl.sendParticles(ParticleTypes.GLOW, entity.getX() - Math.cos(ang) * 1.2D, entity.getY() + h, entity.getZ() - Math.sin(ang) * 1.2D, 0, 0.0D, 0.1D, 0.0D, 1.0D);
            }
            sl.sendParticles(ParticleTypes.ENCHANT, entity.getX(), entity.getY() + 1.0D, entity.getZ(), 60, 0.8D, 0.8D, 0.8D, 1.0D);
            sl.playSound(null, entity.getX(), entity.getY(), entity.getZ(), net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE,
                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.6F);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
        entity.setAbsorptionAmount(Math.max(0.0F, entity.getAbsorptionAmount() - 6.0F));
        if (!entity.level().isClientSide && entity.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.END_ROD, entity.getX(), entity.getY() + 1.0D, entity.getZ(), 30, 0.6D, 0.9D, 0.6D, 0.08D);
            sl.playSound(null, entity.getX(), entity.getY(), entity.getZ(), net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.4F);
        }
        net.minecraft.world.entity.ai.attributes.AttributeInstance swim = map.getInstance(ForgeMod.SWIM_SPEED.get());
        if (swim != null) {
            swim.removeModifier(SWIM_ID);
        }
        super.removeAttributeModifiers(entity, map, amp);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amp) {
        return duration % 5 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amp) {
        if (entity.level().isClientSide) {
            return;
        }
        if (entity.isEyeInFluid(net.minecraft.tags.FluidTags.WATER)) {
            entity.setAirSupply(entity.getMaxAirSupply());
            if (entity.tickCount % 20 < 5) {
                entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, true, false, false));
            }
        }
        if (entity.level() instanceof ServerLevel sl) {
            double a = entity.tickCount * 0.25D;
            double r = entity.getBbWidth() * 0.9D;
            sl.sendParticles(ParticleTypes.END_ROD, entity.getX() + Math.cos(a) * r, entity.getY() + 0.2D + (entity.tickCount % 40) / 40.0D * 1.6D,
                    entity.getZ() + Math.sin(a) * r, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            sl.sendParticles(ParticleTypes.ENCHANT, entity.getX(), entity.getY() + 1.8D, entity.getZ(), 2, 0.4D, 0.1D, 0.4D, 0.5D);
            // segundo hilo de luz en sentido contrario y estela al nadar
            sl.sendParticles(ParticleTypes.GLOW, entity.getX() - Math.cos(a) * r, entity.getY() + 1.8D - (entity.tickCount % 40) / 40.0D * 1.6D,
                    entity.getZ() - Math.sin(a) * r, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            if (entity.isSwimming() || (entity.isInWater() && entity.getDeltaMovement().horizontalDistanceSqr() > 0.01D)) {
                sl.sendParticles(ParticleTypes.GLOW, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 6, 0.4D, 0.3D, 0.4D, 0.02D);
                sl.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 4, 0.4D, 0.3D, 0.4D, 0.1D);
            }
        }
    }
}

package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;

/**
 * Cría de Leviatán: ágil, pacífica, con poca vida y poco daño.
 * Solo se defiende si la hieren.
 */
public class LeviathanCubEntity extends AbstractLeviathan {

    /** Ticks de vida hasta convertirse en adulto (24000 = 1 día de Minecraft = 20 min). */
    public static final int GROW_TICKS = 24000;

    private int growAge;

    public LeviathanCubEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 5.5F);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 160.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.7D)      // mucho más rápida que el adulto
                .add(Attributes.ATTACK_DAMAGE, 14.0D)       // poco daño
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            this.growAge++;
            if (this.growAge >= GROW_TICKS && this.tickCount % 20 == 0) {
                this.growUp();
            }
        }
    }

    /** Se convierte en Leviatán adulto si hay sitio para su cuerpo; si no, lo reintenta en unos segundos. */
    private void growUp() {
        if (!(this.level() instanceof ServerLevel sl)) {
            return;
        }
        LeviathanEntity adult = ModEntities.LEVIATHAN.get().create(sl);
        if (adult == null) {
            return;
        }
        boolean fits = false;
        for (double lift : new double[]{0.0D, 1.5D, 3.0D}) {
            adult.moveTo(this.getX(), this.getY() + lift, this.getZ(), this.getYRot(), 0.0F);
            if (sl.noCollision(adult)) {
                fits = true;
                break;
            }
        }
        if (!fits) {
            this.growAge = GROW_TICKS - 100;   // reintento en 5 s
            return;
        }
        if (this.hasCustomName()) {
            adult.setCustomName(this.getCustomName());
        }
        adult.setPersistenceRequired();
        sl.addFreshEntity(adult);
        sl.sendParticles(ParticleTypes.END_ROD, adult.getX(), adult.getY() + 3.0D, adult.getZ(), 80, 4.0D, 2.0D, 4.0D, 0.05D);
        sl.sendParticles(ParticleTypes.GLOW, adult.getX(), adult.getY() + 3.0D, adult.getZ(), 60, 4.0D, 2.0D, 4.0D, 0.05D);
        sl.playSound(null, adult.getX(), adult.getY(), adult.getZ(), SoundEvents.ENDER_DRAGON_GROWL,
                SoundSource.NEUTRAL, 3.0F, 0.7F);
        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GrowAge", this.growAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.growAge = tag.getInt("GrowAge");
    }

    @Override
    protected void registerGoals() {
        // Defensa: solo ataca a quien la haya herido.
        this.goalSelector.addGoal(0, new DefendGoal(this));
        // Pacífica: nada tranquila por el océano.
        this.goalSelector.addGoal(1, new RoamGoal(this, 30.0D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    protected double bodyScale() {
        return 2.6D;
    }

    @Override
    protected double cruiseSpeed() {
        return 0.50D;   // ~10 bloques/s: mucho más rápida que el adulto
    }

    /** Embiste y muerde al que la hirió, sin pathfinding (su hitbox es demasiado grande para él). */
    static class DefendGoal extends Goal {
        private final LeviathanCubEntity mob;
        private int biteCooldown;

        DefendGoal(LeviathanCubEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity t = this.mob.getTarget();
            return t != null && t.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }
            this.biteCooldown--;
            Vec3 center = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
            double dist = this.mob.getMouthPos().distanceTo(center);
            if (dist < 3.5D + target.getBbWidth()) {
                if (this.biteCooldown <= 0) {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                    this.biteCooldown = 20;
                }
            }
            if (this.mob.isWaterPoint(center.x, center.y, center.z)) {
                this.mob.getMoveControl().setWantedPosition(center.x, center.y, center.z, 1.6D);
            }
        }
    }

    @Override
    protected double mouthForward() {
        return 6.1D;
    }

    @Override
    protected double mouthUp() {
        return 1.2D;
    }

    @Override
    protected double trailBack() {
        return 15.9D;
    }

    @Override
    protected double trailDensity() {
        return 0.45D;
    }

    @Override
    protected double cullingInflate() {
        return 16.0D;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DOLPHIN_AMBIENT_WATER;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DOLPHIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }
}

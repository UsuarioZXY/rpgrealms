package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
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

    /** Ticks de vida hasta convertirse en adolescente (12000 = 10 min). */
    public static final int GROW_TICKS = 12000;

    private int growAge;

    public LeviathanCubEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 5.5F);
        this.xpReward = 50;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.7D)      // mucho más rápida que el adulto
                .add(Attributes.ATTACK_DAMAGE, 15.0D)       // poco daño
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    private int leapCooldown = 80;
    private boolean leaping;
    private boolean leftWater;
    private int leapTicks;

    /** Salta fuera del agua como un mini delfín cuando nada rápido cerca de la superficie. */
    private void dolphinLeap() {
        if (this.leaping) {
            this.leapTicks++;
            if (!this.isInWater()) {
                this.leftWater = true;
            }
            if ((this.leftWater && this.isInWater()) || this.leapTicks > 100) {
                this.leaping = false;
                if (this.level() instanceof ServerLevel sl) {
                    sl.sendParticles(ParticleTypes.SPLASH, this.getX(), this.getY() + 1.0D, this.getZ(), 40, 1.5D, 0.3D, 1.5D, 0.2D);
                    sl.sendParticles(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 20, 1.5D, 0.5D, 1.5D, 0.05D);
                }
                this.playSound(SoundEvents.GENERIC_SPLASH, 1.5F, 0.8F);
            } else {
                Vec3 v = this.getDeltaMovement();
                double hv = Math.sqrt(v.x * v.x + v.z * v.z);
                this.setXRot((float) (-Math.atan2(v.y, hv) * (180.0D / Math.PI)));   // arco de delfín
            }
            return;
        }
        if (--this.leapCooldown > 0 || !this.isInWater() || this.getTarget() != null) {
            return;
        }
        Vec3 v = this.getDeltaMovement();
        if (v.horizontalDistance() < 0.15D) {
            return;
        }
        int top = Mth.floor(this.getY() + this.getBbHeight());
        int gap = 0;
        while (gap < 5 && this.isWaterPoint(this.getX(), top + gap, this.getZ())) {
            gap++;
        }
        if (gap >= 5) {
            this.leapCooldown = 30;   // demasiado profundo: espera a subir
            return;
        }
        Vec3 fwd = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        this.setDeltaMovement(fwd.x * 0.3D, 1.0D + 0.12D * gap, fwd.z * 0.3D);
        this.setXRot(-50.0F);
        this.leaping = true;
        this.leftWater = false;
        this.leapTicks = 0;
        this.leapCooldown = 120 + this.random.nextInt(160);
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SPLASH, this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 30, 1.2D, 0.3D, 1.2D, 0.2D);
        }
        this.playSound(SoundEvents.DOLPHIN_JUMP, 1.5F, 0.9F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            this.dolphinLeap();
            this.waterSquirt();
            this.growAge++;
            if (this.growAge >= GROW_TICKS && this.tickCount % 20 == 0) {
                this.growUp();
            }
        }
    }

    private int squirtCooldown = 60;

    /** Chorro de agua: empuja a quien la acosa a distancia y le hace un poco de daño. */
    private void waterSquirt() {
        net.minecraft.world.entity.LivingEntity t = this.getTarget();
        if (--this.squirtCooldown > 0 || t == null || !t.isAlive()) {
            return;
        }
        Vec3 from = this.getMouthPos();
        Vec3 to = t.position().add(0.0D, t.getBbHeight() * 0.5D, 0.0D);
        double dist = from.distanceTo(to);
        if (dist < 2.5D || dist > 14.0D || !this.getSensing().hasLineOfSight(t)) {
            return;
        }
        this.squirtCooldown = 100;
        Vec3 dir = to.subtract(from).normalize();
        t.setDeltaMovement(t.getDeltaMovement().add(dir.x * 1.1D, 0.35D, dir.z * 1.1D));
        t.hurtMarked = true;
        t.hurt(this.damageSources().mobAttack(this), 2.0F);
        this.playSound(SoundEvents.DOLPHIN_SPLASH, 2.0F, 1.2F);
        if (this.level() instanceof ServerLevel sl) {
            for (double s = 0.0D; s <= dist; s += 0.7D) {
                Vec3 p = from.add(dir.scale(s));
                sl.sendParticles(ParticleTypes.SPLASH, p.x, p.y, p.z, 2, 0.1D, 0.1D, 0.1D, 0.05D);
                sl.sendParticles(ParticleTypes.BUBBLE, p.x, p.y, p.z, 1, 0.05D, 0.05D, 0.05D, 0.02D);
                sl.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.05D, 0.05D, 0.05D, 0.01D);
                sl.sendParticles(ParticleTypes.FALLING_WATER, p.x, p.y, p.z, 2, 0.15D, 0.15D, 0.15D, 0.0D);
            }
        }
    }

    /** Se convierte en adolescente si hay sitio para su cuerpo; si no, lo reintenta en unos segundos. */
    private void growUp() {
        if (!this.growInto(ModEntities.LEVIATHAN_TEEN.get())) {
            this.growAge = GROW_TICKS - 100;   // reintento en 5 s
        }
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
        this.goalSelector.addGoal(0, new BiteGoal(this, 1.6D));
        // Pacífica: nada tranquila por el océano.
        this.goalSelector.addGoal(1, new RoamGoal(this, 30.0D, 0.6D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    public double bodyScale() {
        return 2.6D;
    }

    @Override
    protected double cruiseSpeed() {
        return 0.50D;   // ~10 bloques/s: mucho más rápida que el adulto
    }

    @Override
    protected double mouthForward() {
        return 9.0D;
    }

    @Override
    protected double mouthUp() {
        return 1.2D;
    }

    @Override
    protected double trailBack() {
        return 12.0D;
    }

    @Override
    protected double trailDensity() {
        return 0.45D;
    }

    @Override
    protected double cullingInflate() {
        return 24.0D;
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

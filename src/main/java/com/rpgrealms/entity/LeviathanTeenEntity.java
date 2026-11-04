package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;

/**
 * Leviatán adolescente: más grande y fuerte que la cría, todavía pacífico (solo se defiende) y sin ataques a distancia.
 * Con el tiempo se convierte en Leviatán adulto.
 */
public class LeviathanTeenEntity extends AbstractLeviathan {
    /** Ticks de vida hasta convertirse en adulto (24000 = 20 min). */
    public static final int GROW_TICKS = 24000;

    private int growAge;

    public LeviathanTeenEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 4.2F);
        this.xpReward = 500;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 450.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 26.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RamGoal(this));
        this.goalSelector.addGoal(1, new BiteGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new RoamGoal(this, 40.0D, 0.3D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    private int pulseCooldown = 120;

    /** Pulso sónico: onda corta que daña y ralentiza a quien esté cerca mientras lo ataca. */
    private void sonicPulse() {
        net.minecraft.world.entity.LivingEntity t = this.getTarget();
        if (--this.pulseCooldown > 0 || t == null || !t.isAlive() || t.distanceToSqr(this) > 16.0D * 16.0D) {
            return;
        }
        this.pulseCooldown = 200;
        this.playSound(net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, 2.5F, 1.2F);
        this.shockwave(14.0D, 8.0F, 0.6D, 0.25D, 80, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            this.sonicPulse();
            this.growAge++;
            if (this.growAge >= GROW_TICKS && this.tickCount % 20 == 0) {
                if (!this.growInto(ModEntities.LEVIATHAN.get())) {
                    this.growAge = GROW_TICKS - 100;
                }
            }
        }
    }

    /** Embestida: se prepara (sonido y burbujas), carga en línea recta contra el objetivo y lo embiste con daño ×1.5. */
    static class RamGoal extends net.minecraft.world.entity.ai.goal.Goal {
        private final LeviathanTeenEntity mob;
        private int state;
        private int timer;
        private int nextUse = 100;
        private net.minecraft.world.phys.Vec3 dest = net.minecraft.world.phys.Vec3.ZERO;
        private boolean hit;

        RamGoal(LeviathanTeenEntity mob) {
            this.mob = mob;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            net.minecraft.world.entity.LivingEntity t = this.mob.getTarget();
            if (t == null || !t.isAlive() || this.mob.tickCount < this.nextUse) {
                return false;
            }
            double d = Math.sqrt(this.mob.distanceToSqr(t));
            net.minecraft.world.phys.Vec3 c = t.position().add(0.0D, t.getBbHeight() * 0.5D, 0.0D);
            return d > 8.0D && d < 40.0D && this.mob.isWaterPoint(c.x, c.y, c.z);
        }

        @Override
        public boolean canContinueToUse() {
            return this.state != 0 && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.state = 1;
            this.timer = 0;
            this.hit = false;
            this.mob.setAggressive(true);
            this.mob.playSound(net.minecraft.sounds.SoundEvents.GUARDIAN_ATTACK, 3.0F, 0.5F);
        }

        @Override
        public void stop() {
            this.state = 0;
            this.mob.setAggressive(false);
            this.nextUse = this.mob.tickCount + 160;
        }

        @Override
        public void tick() {
            net.minecraft.world.entity.LivingEntity t = this.mob.getTarget();
            if (t == null) {
                return;
            }
            net.minecraft.world.phys.Vec3 c = t.position().add(0.0D, t.getBbHeight() * 0.5D, 0.0D);
            this.mob.getLookControl().setLookAt(c.x, c.y, c.z, 30.0F, 30.0F);
            this.timer++;
            net.minecraft.world.phys.Vec3 mouth = this.mob.getMouthPos();
            if (this.state == 1) {
                this.mob.getMoveControl().setWantedPosition(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0.0D);
                if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel sl && this.timer % 3 == 0) {
                    sl.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE, mouth.x, mouth.y, mouth.z, 6, 0.8D, 0.5D, 0.8D, 0.05D);
                    sl.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD, mouth.x, mouth.y, mouth.z, 3, 0.6D, 0.4D, 0.6D, 0.04D);
                }
                if (this.timer >= 20) {
                    net.minecraft.world.phys.Vec3 dir = c.subtract(this.mob.position()).normalize();
                    this.dest = c.add(dir.scale(8.0D));
                    this.state = 2;
                    this.timer = 0;
                    this.mob.playSound(net.minecraft.sounds.SoundEvents.DOLPHIN_JUMP, 3.0F, 0.5F);
                }
                return;
            }
            // carga
            this.mob.getMoveControl().setWantedPosition(this.dest.x, this.dest.y, this.dest.z, 1.9D);
            if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel sl) {
                sl.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE_POP, this.mob.getX(), this.mob.getY() + this.mob.getBbHeight() * 0.5D, this.mob.getZ(), 8, 1.5D, 1.0D, 1.5D, 0.05D);
                sl.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD, mouth.x, mouth.y, mouth.z, 2, 0.4D, 0.3D, 0.4D, 0.03D);
            }
            if (!this.hit) {
                for (net.minecraft.world.entity.LivingEntity e : this.mob.level().getEntitiesOfClass(
                        net.minecraft.world.entity.LivingEntity.class,
                        new net.minecraft.world.phys.AABB(mouth, mouth).inflate(3.5D),
                        x -> x != this.mob && !(x instanceof AbstractLeviathan))) {
                    this.mob.doHurtTarget(e);
                    e.hurt(this.mob.damageSources().mobAttack(this.mob), (float) (this.mob.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) * 0.5D));
                    net.minecraft.world.phys.Vec3 away = e.position().subtract(this.mob.position()).multiply(1.0D, 0.0D, 1.0D).normalize();
                    e.setDeltaMovement(e.getDeltaMovement().add(away.x * 1.4D, 0.5D, away.z * 1.4D));
                    e.hurtMarked = true;
                    this.hit = true;
                }
                if (this.hit) {
                    this.mob.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE, 2.0F, 1.0F);
                    this.state = 3;   // termina
                }
            }
            if (this.timer >= 40 || this.mob.position().distanceTo(this.dest) < 4.0D) {
                this.state = 0;
            }
            if (this.state == 3) {
                this.state = 0;
            }
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
    protected double cruiseSpeed() {
        return 0.40D;
    }

    @Override
    public double bodyScale() {
        return 4.4D;
    }

    @Override
    protected double mouthForward() {
        return 14.0D;
    }

    @Override
    protected double mouthUp() {
        return 2.0D;
    }

    @Override
    protected double trailBack() {
        return 30.0D;
    }

    @Override
    protected double trailDensity() {
        return 0.7D;
    }

    @Override
    protected double cullingInflate() {
        return 55.0D;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GUARDIAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GUARDIAN_DEATH;
    }
}

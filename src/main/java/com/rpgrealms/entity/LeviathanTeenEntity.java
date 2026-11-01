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
        this.xpReward = 150;
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
        this.goalSelector.addGoal(0, new BiteGoal(this, 1.5D));
        this.goalSelector.addGoal(1, new RoamGoal(this, 40.0D, 0.3D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            this.growAge++;
            if (this.growAge >= GROW_TICKS && this.tickCount % 20 == 0) {
                if (!this.growInto(ModEntities.LEVIATHAN.get())) {
                    this.growAge = GROW_TICKS - 100;
                }
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
        return 33.0D;
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

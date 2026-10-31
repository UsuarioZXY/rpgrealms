package com.rpgrealms.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;

/**
 * Cría de Leviatán: ágil, pacífica, con poca vida y poco daño.
 * Solo se defiende si la hieren.
 */
public class LeviathanCubEntity extends AbstractLeviathan {

    public LeviathanCubEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 20);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.7D)      // mucho más rápida que el adulto
                .add(Attributes.ATTACK_DAMAGE, 7.0D)       // poco daño
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        // Defensa: solo ataca a quien la haya herido.
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.5D, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity target) {
                double reach = 5.0D + target.getBbWidth() * 0.5D;
                return reach * reach;
            }
        });
        // Pacífica: nada tranquila por el océano.
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0D, 50));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    protected double mouthForward() {
        return 4.2D;
    }

    @Override
    protected double mouthUp() {
        return 0.8D;
    }

    @Override
    protected double cullingInflate() {
        return 10.0D;
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

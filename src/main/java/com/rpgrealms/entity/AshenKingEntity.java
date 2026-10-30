package com.rpgrealms.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;

/**
 * Rey Cenizo: boss de fuego con barra de vida.
 * Fase 2 (menos del 50% de vida): se enfurece y invoca wither skeletons.
 */
public class AshenKingEntity extends Monster {
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    private boolean enraged = false;

    public AshenKingEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 100;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            living.setSecondsOnFire(5);
        }
        return hit;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        if (!this.enraged && this.getHealth() < this.getMaxHealth() / 2.0F) {
            this.enraged = true;
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1));
            this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 2.0F, 0.8F);
        }

        if (this.enraged && this.tickCount % 200 == 0 && this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 2; i++) {
                WitherSkeleton minion = EntityType.WITHER_SKELETON.create(serverLevel);
                if (minion != null) {
                    minion.moveTo(this.getX() + this.random.nextInt(5) - 2, this.getY(),
                            this.getZ() + this.random.nextInt(5) - 2, this.random.nextFloat() * 360.0F, 0.0F);
                    minion.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()),
                            MobSpawnType.MOB_SUMMONED, null, null);
                    minion.setTarget(this.getTarget());
                    serverLevel.addFreshEntity(minion);
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        this.enraged = this.getHealth() < this.getMaxHealth() / 2.0F;
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}

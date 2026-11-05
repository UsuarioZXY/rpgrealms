package com.rpgrealms.entity;

import com.rpgrealms.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.EnumSet;

/**
 * Farolero: medusa luminosa que flota como un globo sobre el agua. Pacífica; si la golpean
 * suelta una descarga que daña y ralentiza a quien la atacó.
 */
public class FaroleroEntity extends PathfinderMob {
    private int shockCooldown = 0;

    public FaroleroEntity(EntityType<? extends FaroleroEntity> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.12D)
                .add(Attributes.FLYING_SPEED, 0.12D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DriftGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean r = super.hurt(source, amount);
        if (r && !this.level().isClientSide && this.shockCooldown <= 0
                && source.getEntity() instanceof LivingEntity a && a != this) {
            this.shockCooldown = 30;
            a.hurt(this.damageSources().magic(), 4.0F);
            a.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TRIDENT_THUNDER,
                    SoundSource.NEUTRAL, 0.6F, 2.0F);
            if (this.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 0.7D, this.getZ(), 40, 0.6D, 0.6D, 0.6D, 0.3D);
                sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, a.getX(), a.getY() + a.getBbHeight() * 0.5D, a.getZ(), 25, 0.4D, 0.4D, 0.4D, 0.2D);
                sl.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + 0.7D, this.getZ(), 20, 0.7D, 0.5D, 0.7D, 0.05D);
            }
        }
        return r;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.shockCooldown > 0) {
            this.shockCooldown--;
        }
        if (!this.level().isClientSide) {
            // se mantiene a poca altura sobre la superficie del agua
            double surface = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.getBlockX(), this.getBlockZ());
            double want = surface + 2.5D;
            double dy = want - this.getY();
            if (Math.abs(dy) > 1.5D && this.getNavigation().isDone()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, Math.signum(dy) * 0.01D, 0.0D));
            }
        } else {
            double x = this.getX(), y = this.getY() + 0.9D, z = this.getZ();
            // luz ámbar que gotea de la campana
            if (this.random.nextInt(6) == 0) {
                this.level().addParticle(ParticleTypes.GLOW, x + (this.random.nextDouble() - 0.5D) * 0.8D, y,
                        z + (this.random.nextDouble() - 0.5D) * 0.8D, 0.0D, -0.02D, 0.0D);
            }
            if (this.random.nextInt(25) == 0) {
                this.level().addParticle(ParticleTypes.END_ROD, x, y + 0.3D, z, 0.0D, 0.02D, 0.0D);
            }
            if (this.random.nextInt(18) == 0) {
                this.level().addParticle(ParticleTypes.FALLING_WATER, x + (this.random.nextDouble() - 0.5D), y - 0.3D,
                        z + (this.random.nextDouble() - 0.5D), 0.0D, -0.1D, 0.0D);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return true;
    }

    public static boolean checkSpawn(EntityType<? extends FaroleroEntity> type, ServerLevelAccessor level,
                                     MobSpawnType reason, BlockPos pos, RandomSource random) {
        for (int i = 1; i <= 6; i++) {
            if (level.getFluidState(pos.below(i)).is(FluidTags.WATER)) {
                return level.getBlockState(pos).isAir() && random.nextInt(2) == 0;
            }
            if (!level.getBlockState(pos.below(i)).isAir()) {
                return false;
            }
        }
        return false;
    }

    /** Deriva despacio hacia puntos al azar sobre el agua, a poca altura. */
    static class DriftGoal extends Goal {
        private final FaroleroEntity mob;

        DriftGoal(FaroleroEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.mob.getNavigation().isDone() && this.mob.getRandom().nextInt(40) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.mob.getNavigation().isInProgress();
        }

        @Override
        public void start() {
            RandomSource r = this.mob.getRandom();
            double x = this.mob.getX() + (r.nextDouble() - 0.5D) * 24.0D;
            double z = this.mob.getZ() + (r.nextDouble() - 0.5D) * 24.0D;
            double surface = this.mob.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) x, (int) z);
            double y = surface + 2.0D + r.nextDouble() * 3.0D;
            this.mob.getNavigation().moveTo(x, y, z, 1.0D);
        }
    }
}

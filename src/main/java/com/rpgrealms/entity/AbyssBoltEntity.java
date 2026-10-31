package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import com.rpgrealms.registry.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Orbe abisal: proyectil luminoso que dispara el Leviatán por la boca.
 * Vuela en línea recta (también bajo el agua) y explota en chispas al impactar.
 */
public class AbyssBoltEntity extends AbstractHurtingProjectile implements ItemSupplier {

    public AbyssBoltEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    public AbyssBoltEntity(Level level, LivingEntity owner, double dx, double dy, double dz) {
        super(ModEntities.ABYSS_BOLT.get(), owner, dx, dy, dz, level);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.ABYSS_ORB.get());
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.GLOW;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 0.97F;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            target.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 12.0F);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        this.getX(), this.getY(), this.getZ(), 30, 0.6D, 0.6D, 0.6D, 0.25D);
                serverLevel.sendParticles(ParticleTypes.GLOW,
                        this.getX(), this.getY(), this.getZ(), 12, 0.5D, 0.5D, 0.5D, 0.1D);
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.HOSTILE, 2.0F, 0.6F);
            this.discard();
        }
    }
}

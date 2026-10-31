package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import com.rpgrealms.registry.ModItems;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
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
 * Vuela en línea recta (también bajo el agua), deja una estela de chispas y estalla al impactar
 * dañando en área.
 */
public class AbyssBoltEntity extends AbstractHurtingProjectile implements ItemSupplier {
    private Entity directTarget;

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
    public void tick() {
        // Guiado suave hacia el objetivo del Leviatán durante el primer par de segundos.
        if (!this.level().isClientSide && this.tickCount < 50 && this.getOwner() instanceof net.minecraft.world.entity.Mob mob
                && mob.getTarget() != null && mob.getTarget().isAlive()) {
            Vec3 want = mob.getTarget().getBoundingBox().getCenter().subtract(this.position());
            if (want.lengthSqr() > 1.0D) {
                Vec3 v = this.getDeltaMovement();
                Vec3 steered = v.lerp(want.normalize().scale(Math.max(v.length(), 0.8D)), 0.07D);
                this.setDeltaMovement(steered);
                Vec3 n = steered.normalize().scale(0.05D);
                this.xPower = n.x;
                this.yPower = n.y;
                this.zPower = n.z;
            }
        }
        super.tick();
        if (this.level().isClientSide) {
            // Estela mística: motas cian→violeta, destellos y chispas
            DustColorTransitionOptions mote = new DustColorTransitionOptions(
                    new Vector3f(0.4F, 0.95F, 1.0F), new Vector3f(0.5F, 0.2F, 0.95F), 1.4F);
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(mote,
                        this.getX() + (this.random.nextDouble() - 0.5D) * 0.7D,
                        this.getY() + (this.random.nextDouble() - 0.5D) * 0.7D,
                        this.getZ() + (this.random.nextDouble() - 0.5D) * 0.7D, 0.0D, 0.0D, 0.0D);
            }
            this.level().addParticle(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            if (this.tickCount % 2 == 0) {
                this.level().addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(),
                        (this.random.nextDouble() - 0.5D) * 0.05D, (this.random.nextDouble() - 0.5D) * 0.05D,
                        (this.random.nextDouble() - 0.5D) * 0.05D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            this.directTarget = result.getEntity();
            this.directTarget.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 12.0F);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) {
            return;
        }
        // Daño en área alrededor del impacto (menor que el golpe directo).
        Entity owner = this.getOwner();
        for (LivingEntity e : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D),
                x -> x != owner && x != this.directTarget && !(x instanceof AbstractLeviathan))) {
            e.hurt(this.damageSources().indirectMagic(this, owner), 6.0F);
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY(), this.getZ(), 40, 0.8D, 0.8D, 0.8D, 0.3D);
            serverLevel.sendParticles(ParticleTypes.GLOW,
                    this.getX(), this.getY(), this.getZ(), 20, 0.7D, 0.7D, 0.7D, 0.15D);
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                    this.getX(), this.getY(), this.getZ(), 14, 0.2D, 0.2D, 0.2D, 0.25D);
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            // Ondas de choque: tres anillos que se expanden en planos distintos
            for (int i = 0; i < 28; i++) {
                double a = i * Math.PI * 2.0D / 28.0D;
                double c = Math.cos(a);
                double sn = Math.sin(a);
                serverLevel.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0, c, 0.0D, sn, 0.4D);
                serverLevel.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0, c, sn, 0.0D, 0.3D);
                serverLevel.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0, 0.0D, c, sn, 0.3D);
            }
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.HOSTILE, 2.5F, 0.6F);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.2F, 1.4F);
        this.discard();
    }
}

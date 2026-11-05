package com.rpgrealms.entity;

import com.rpgrealms.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * Pez linterna rúnico: pez pequeño de cardumen con una runa que brilla en el costado y un señuelo luminoso.
 * Pacífico. Si hieren a uno, su señuelo destella y todo el cardumen cercano huye más rápido.
 */
public class LanternFishEntity extends AbstractSchoolingFish {
    private static final EntityDataAccessor<Integer> DATA_ALARM =
            SynchedEntityData.defineId(LanternFishEntity.class, EntityDataSerializers.INT);

    public LanternFishEntity(EntityType<? extends LanternFishEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSchoolingFish.createAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 1.4D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ALARM, 0);
    }

    /** Ticks de alarma restantes (el señuelo destella en blanco-naranja). */
    public int getAlarm() {
        return this.entityData.get(DATA_ALARM);
    }

    public void setAlarm(int ticks) {
        this.entityData.set(DATA_ALARM, ticks);
    }

    @Override
    public int getMaxSchoolSize() {
        return 9;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.LANTERN_FISH_BUCKET.get());
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.COD_HURT;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean r = super.hurt(source, amount);
        if (r && !this.level().isClientSide) {
            this.setAlarm(80);
            for (LanternFishEntity o : this.level().getEntitiesOfClass(LanternFishEntity.class,
                    this.getBoundingBox().inflate(12.0D), e -> e != this)) {
                o.setAlarm(80);
                o.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
            }
            if (this.level() instanceof ServerLevel sl) {
                Vec3 p = this.lampPos();
                sl.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 14, 0.25D, 0.25D, 0.25D, 0.04D);
            }
        }
        return r;
    }

    /** Posición aproximada del señuelo luminoso. */
    public Vec3 lampPos() {
        Vec3 fwd = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        return this.position().add(fwd.scale(0.22D)).add(0.0D, this.getBbHeight() * 0.95D, 0.0D);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.getAlarm() > 0) {
                this.setAlarm(this.getAlarm() - 1);
            }
        } else if (this.isInWater()) {
            Vec3 p = this.lampPos();
            // chispitas de luz que se desprenden del señuelo y alguna burbuja
            if (this.random.nextInt(this.getAlarm() > 0 ? 2 : 14) == 0) {
                this.level().addParticle(ParticleTypes.END_ROD, p.x, p.y, p.z,
                        (this.random.nextDouble() - 0.5D) * 0.01D, 0.012D, (this.random.nextDouble() - 0.5D) * 0.01D);
            }
            if (this.random.nextInt(45) == 0) {
                this.level().addParticle(ParticleTypes.GLOW, p.x, p.y, p.z, 0.0D, 0.0D, 0.0D);
            }
            if (this.random.nextInt(60) == 0) {
                this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getEyeY(), this.getZ(), 0.0D, 0.04D, 0.0D);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public static boolean checkSpawn(EntityType<? extends LanternFishEntity> type, ServerLevelAccessor level,
                                     MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos.above()).is(FluidTags.WATER)
                && pos.getY() <= level.getSeaLevel() - 4;
    }
}

package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import com.rpgrealms.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

/** Huevo de Leviatán: tras un tiempo se rompe y nace una Cría. Se recoge golpeándolo. */
public class LeviathanEggEntity extends Entity {
    /** Ticks hasta que eclosiona (3600 = 3 minutos). */
    public static final int HATCH_TICKS = 3600;

    private static final EntityDataAccessor<Integer> DATA_LEFT =
            SynchedEntityData.defineId(LeviathanEggEntity.class, EntityDataSerializers.INT);

    private int left = HATCH_TICKS;

    public LeviathanEggEntity(EntityType<? extends LeviathanEggEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LEFT, HATCH_TICKS);
    }

    /** Ticks que faltan (sincronizado cada segundo, para la animación del cliente). */
    public int getTicksLeft() {
        return this.entityData.get(DATA_LEFT);
    }

    @Override
    public void tick() {
        super.tick();

        // Física sencilla: cae y flota despacio en el agua.
        Vec3 d = this.getDeltaMovement();
        double gravity = this.isInWater() ? -0.01D : -0.04D;
        this.setDeltaMovement(d.x * 0.6D, d.y * (this.isInWater() ? 0.8D : 0.98D) + gravity, d.z * 0.6D);
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
        }

        if (this.level().isClientSide) {
            double prog = 1.0D - Math.max(0, Math.min(1.0D, this.getTicksLeft() / (double) HATCH_TICKS));
            if (this.tickCount % Math.max(2, (int) (8 - prog * 6)) == 0) {
                // motas que giran alrededor del huevo y suben
                double a = this.tickCount * 0.12D + this.random.nextDouble();
                double r = 1.5D + this.random.nextDouble() * 0.3D;
                double y = this.getY() + ((this.tickCount * 0.02D) % 2.6D);
                this.level().addParticle(ParticleTypes.END_ROD, this.getX() + Math.cos(a) * r, y,
                        this.getZ() + Math.sin(a) * r, -Math.sin(a) * 0.03D, 0.01D, Math.cos(a) * 0.03D);
            }
            if (prog > 0.8D && this.random.nextInt(4) == 0) {
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
                        this.getY() + this.random.nextDouble() * 2.8D, this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
                        0.0D, 0.0D, 0.0D);
            }
            return;
        }

        this.left--;
        if (this.left % 20 == 0) {
            this.entityData.set(DATA_LEFT, this.left);
        }
        // Los últimos 3 segundos crujen.
        // Golpecitos desde dentro, cada vez más seguidos
        if (this.left < HATCH_TICKS * 0.7D && this.left > 60 && this.left % (this.left > HATCH_TICKS * 0.35D ? 120 : 50) == 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 0.8F, 1.3F);
        }
        if (this.left > 0 && this.left <= 60 && this.left % 20 == 0 && this.level() instanceof ServerLevel sl) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 2.0F, 0.6F);
            sl.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.2D, this.getZ(), 6, 0.5D, 0.6D, 0.5D, 0.01D);
        }
        if (this.left <= 0) {
            this.hatch();
        }
    }

    private void hatch() {
        if (!(this.level() instanceof ServerLevel sl)) {
            return;
        }
        LeviathanCubEntity cub = ModEntities.LEVIATHAN_CUB.get().create(sl);
        if (cub != null) {
            cub.moveTo(this.getX(), this.getY() + 0.6D, this.getZ(), this.random.nextFloat() * 360.0F, 0.0F);
            cub.setPersistenceRequired();
            sl.addFreshEntity(cub);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.TURTLE_EGG_BREAK, SoundSource.NEUTRAL, 3.0F, 0.5F);
        sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.2D, this.getZ(), 40, 0.8D, 0.9D, 0.8D, 0.08D);
        sl.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.2D, this.getZ(), 20, 0.8D, 0.9D, 0.8D, 0.05D);
        this.discard();
    }

    /** Un golpe de un jugador lo recoge (en creativo no devuelve el objeto). */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide || this.isRemoved()) {
            return true;
        }
        if (source.getEntity() instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                this.spawnAtLocation(ModItems.LEVIATHAN_EGG.get());
            }
            this.discard();
        }
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.left = tag.contains("Left") ? tag.getInt("Left") : HATCH_TICKS;
        this.entityData.set(DATA_LEFT, this.left);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Left", this.left);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

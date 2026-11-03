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

/** Huevo de Vallsaron: se empuja, se rompe a golpes y tras 15 minutos nace una Cría. Agachado + clic derecho lo recoge. */
public class LeviathanEggEntity extends Entity {
    /** Ticks hasta que eclosiona (18000 = 15 minutos). */
    public static final int HATCH_TICKS = 18000;
    /** Golpes que aguanta antes de romperse (vida del huevo). */
    public static final float MAX_HEALTH = 20.0F;

    private static final EntityDataAccessor<Integer> DATA_LEFT =
            SynchedEntityData.defineId(LeviathanEggEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_HIT =
            SynchedEntityData.defineId(LeviathanEggEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_HIT_DIR =
            SynchedEntityData.defineId(LeviathanEggEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HP =
            SynchedEntityData.defineId(LeviathanEggEntity.class, EntityDataSerializers.FLOAT);

    private int left = HATCH_TICKS;

    public LeviathanEggEntity(EntityType<? extends LeviathanEggEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LEFT, HATCH_TICKS);
        this.entityData.define(DATA_HIT, 0);
        this.entityData.define(DATA_HIT_DIR, 0.0F);
        this.entityData.define(DATA_HP, MAX_HEALTH);
    }

    /** Ticks restantes de la animación de golpe (0 = quieto). */
    public int getHitTicks() {
        return this.entityData.get(DATA_HIT);
    }

    /** Dirección (grados) desde la que llegó el último golpe. */
    public float getHitDir() {
        return this.entityData.get(DATA_HIT_DIR);
    }

    /** Fracción de vida (1 = intacto) para mostrar las grietas de daño. */
    public float getHealthFraction() {
        return Math.max(0.0F, this.entityData.get(DATA_HP) / MAX_HEALTH);
    }

    /** Vive hasta ser golpeado: no lo empuja el agua, pero sí los jugadores y los leviatanes. */
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    /** Ticks que faltan (sincronizado cada segundo, para la animación del cliente). */
    public int getTicksLeft() {
        return this.entityData.get(DATA_LEFT);
    }

    @Override
    public void tick() {
        super.tick();

        // Empujones: jugadores y leviatanes cercanos lo hacen rodar; el agua lo frena y flota despacio.
        for (Entity other : this.level().getEntities(this, this.getBoundingBox().inflate(0.05D, -0.1D, 0.05D),
                e -> e instanceof Player || e instanceof AbstractLeviathan)) {
            if (other.isSpectator()) {
                continue;
            }
            Vec3 away = this.position().subtract(other.position()).multiply(1.0D, 0.0D, 1.0D);
            if (away.lengthSqr() < 1.0E-4D) {
                away = new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D);
            }
            double push = other instanceof Player ? 0.045D : 0.12D;
            this.setDeltaMovement(this.getDeltaMovement().add(away.normalize().scale(push)));
        }
        if (this.entityData.get(DATA_HIT) > 0 && !this.level().isClientSide) {
            this.entityData.set(DATA_HIT, this.entityData.get(DATA_HIT) - 1);
        }
        Vec3 d = this.getDeltaMovement();
        boolean wet = this.isInWater();
        double gravity = wet ? -0.012D : -0.04D;
        double fric = this.onGround() ? 0.78D : (wet ? 0.9D : 0.97D);
        this.setDeltaMovement(d.x * fric, d.y * (wet ? 0.85D : 0.98D) + gravity, d.z * fric);
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
        // Fases: golpecitos desde dentro cada vez más seguidos, latidos de luz y, al final, sacudidas fuertes.
        double frac = this.left / (double) HATCH_TICKS;
        if (frac < 0.75D && this.left > 100) {
            int period = frac > 0.45D ? 400 : (frac > 0.2D ? 160 : (frac > 0.07D ? 60 : 30));
            if (this.left % period == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 0.9F + (float) (1.0D - frac), 1.3F - (float) (1.0D - frac) * 0.6F);
                this.entityData.set(DATA_HIT, 8);
                this.entityData.set(DATA_HIT_DIR, this.random.nextFloat() * 360.0F);
            }
        }
        if (this.left > 0 && this.left <= 100 && this.left % 10 == 0 && this.level() instanceof ServerLevel sl) {
            this.entityData.set(DATA_HIT, 8);
            this.entityData.set(DATA_HIT_DIR, this.random.nextFloat() * 360.0F);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 2.2F, 0.6F);
            sl.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.2D, this.getZ(), 6, 0.5D, 0.6D, 0.5D, 0.01D);
            sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.4D, this.getZ(), 10, 0.7D, 0.8D, 0.7D, 0.06D);
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

    /** Cada golpe lo sacude y le quita vida; al agotarse se rompe en pedazos (y se pierde el huevo). */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isRemoved() || this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.level().isClientSide) {
            return true;
        }
        float dmg = Math.max(1.0F, Math.min(amount, 6.0F));
        float hp = this.entityData.get(DATA_HP) - dmg;
        this.entityData.set(DATA_HP, hp);
        this.entityData.set(DATA_HIT, 10);
        Entity src = source.getEntity();
        float dir = src != null ? (float) (Math.atan2(this.getZ() - src.getZ(), this.getX() - src.getX()) * 57.2958D) : this.random.nextFloat() * 360.0F;
        this.entityData.set(DATA_HIT_DIR, dir);
        if (src != null) {
            Vec3 away = this.position().subtract(src.position()).multiply(1.0D, 0.0D, 1.0D);
            if (away.lengthSqr() > 1.0E-4D) {
                this.setDeltaMovement(this.getDeltaMovement().add(away.normalize().scale(0.22D)).add(0.0D, 0.05D, 0.0D));
            }
        }
        if (this.level() instanceof ServerLevel sl) {
            sl.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 1.6F, 0.8F + this.random.nextFloat() * 0.3F);
            sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 1.4D, this.getZ(), 14, 0.7D, 0.9D, 0.7D, 0.2D);
            sl.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + 1.4D, this.getZ(), 6, 0.7D, 0.9D, 0.7D, 0.05D);
        }
        if (hp <= 0.0F) {
            this.shatter();
        }
        return true;
    }

    /** Se rompe: pedazos de cáscara, luz y algo de botín (no nace nada). */
    private void shatter() {
        if (this.level() instanceof ServerLevel sl) {
            sl.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.NEUTRAL, 3.0F, 0.6F);
            sl.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.NEUTRAL, 3.0F, 0.5F);
            sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.3D, this.getZ(), 60, 0.9D, 1.0D, 0.9D, 0.12D);
            sl.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1.3D, this.getZ(), 25, 0.9D, 1.0D, 0.9D, 0.06D);
            sl.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + 1.3D, this.getZ(), 30, 0.9D, 1.0D, 0.9D, 0.1D);
            sl.sendParticles(new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK,
                    net.minecraft.world.level.block.Blocks.PRISMARINE_BRICKS.defaultBlockState()), this.getX(), this.getY() + 1.3D, this.getZ(),
                    50, 0.9D, 1.0D, 0.9D, 0.15D);
            this.spawnAtLocation(net.minecraft.world.item.Items.PRISMARINE_SHARD, 1 + this.random.nextInt(3));
            if (this.random.nextInt(3) == 0) {
                this.spawnAtLocation(net.minecraft.world.item.Items.GLOW_INK_SAC);
            }
        }
        this.discard();
    }

    /** Agacharse y clic derecho con la mano vacía recoge el huevo intacto. */
    @Override
    public net.minecraft.world.InteractionResult interact(Player player, net.minecraft.world.InteractionHand hand) {
        if (player.isSecondaryUseActive() && player.getItemInHand(hand).isEmpty()) {
            if (!this.level().isClientSide) {
                if (!player.getAbilities().instabuild) {
                    this.spawnAtLocation(ModItems.LEVIATHAN_EGG.get());
                }
                this.discard();
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return net.minecraft.world.InteractionResult.PASS;
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
        if (tag.contains("Health")) {
            this.entityData.set(DATA_HP, tag.getFloat("Health"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Left", this.left);
        tag.putFloat("Health", this.entityData.get(DATA_HP));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

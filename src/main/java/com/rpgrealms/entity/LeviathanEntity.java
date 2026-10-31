package com.rpgrealms.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Leviatán adulto: colosal, lento y muy resistente. Vive en los lagos de las cuevas del Abismo.
 * Ataques: mordisco, ráfaga de orbes abisales y un rayo cargado que recorre toda la cueva.
 */
public class LeviathanEntity extends AbstractLeviathan {

    /** true = solo ataca si lo hieren (como la cría). false = ataca a los jugadores cercanos. */
    private static final boolean PROVOKED_ONLY = true;

    public static final int PHASE_IDLE = 0;
    public static final int PHASE_BOLTS = 1;
    public static final int PHASE_BEAM_CHARGE = 2;
    public static final int PHASE_BEAM_FIRE = 3;

    private static final EntityDataAccessor<Integer> DATA_PHASE =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_12);

    public LeviathanEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 8);
        this.xpReward = 500;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 800.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)      // lento comparado con la cría
                .add(Attributes.ATTACK_DAMAGE, 32.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    // ------------------------------------------------------------------ datos sincronizados

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, PHASE_IDLE);
    }

    @Override
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    // ------------------------------------------------------------------ IA

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CombatGoal(this));
        this.goalSelector.addGoal(1, new RoamGoal(this));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        if (!PROVOKED_ONLY) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    protected double mouthForward() {
        return 10.5D;
    }

    @Override
    protected double mouthUp() {
        return 1.8D;
    }

    @Override
    protected double cullingInflate() {
        return 32.0D;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    // ------------------------------------------------------------------ ataques

    /** Dispara un orbe abisal desde la boca hacia el objetivo. */
    public void fireBolt(LivingEntity target) {
        Vec3 mouth = this.getMouthPos();
        Vec3 aim = target.getBoundingBox().getCenter().subtract(mouth);
        if (aim.lengthSqr() < 1.0E-4D) {
            return;
        }
        AbyssBoltEntity bolt = new AbyssBoltEntity(this.level(), this, aim.x, aim.y, aim.z);
        bolt.setPos(mouth.x, mouth.y, mouth.z);
        bolt.setDeltaMovement(aim.normalize().scale(1.4D));
        this.level().addFreshEntity(bolt);
        this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.HOSTILE, 3.0F, 0.5F);
    }

    /** Partículas que se concentran en la boca mientras carga el rayo. */
    public void chargeBeamParticles(int tick) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 mouth = this.getMouthPos();
        float radius = 6.0F * (1.0F - tick / 50.0F) + 0.5F;
        for (int i = 0; i < 6; i++) {
            double a = this.random.nextDouble() * Math.PI * 2.0D;
            double b = (this.random.nextDouble() - 0.5D) * Math.PI;
            double x = mouth.x + Math.cos(a) * Math.cos(b) * radius;
            double y = mouth.y + Math.sin(b) * radius;
            double z = mouth.z + Math.sin(a) * Math.cos(b) * radius;
            sendParticle(serverLevel, ParticleTypes.GLOW, x, y, z, 1, 0.0D);
        }
        if (tick % 10 == 0) {
            this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                    SoundEvents.BEACON_AMBIENT, SoundSource.HOSTILE, 4.0F, 0.5F + tick / 100.0F);
        }
    }

    /** Un tick del rayo: dibuja la línea de partículas y daña lo que cruza. */
    public void fireBeamTick(Vec3 dir, int tick) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 from = this.getMouthPos();
        double maxLen = 56.0D;
        double len = maxLen;
        for (double s = 1.0D; s <= maxLen; s += 1.0D) {
            Vec3 p = from.add(dir.scale(s));
            BlockPos bp = BlockPos.containing(p);
            BlockState state = this.level().getBlockState(bp);
            if (!state.getCollisionShape(this.level(), bp).isEmpty()) {
                len = s;
                break;
            }
        }
        Vec3 end = from.add(dir.scale(len));

        for (double s = 0.0D; s <= len; s += 1.2D) {
            Vec3 p = from.add(dir.scale(s));
            sendParticle(serverLevel, ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.0D);
            sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 2, 0.15D);
        }
        sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, end.x, end.y, end.z, 14, 0.4D);

        if (tick % 5 == 0) {
            this.level().playSound(null, from.x, from.y, from.z,
                    SoundEvents.GUARDIAN_ATTACK, SoundSource.HOSTILE, 4.0F, 0.6F);
            Vec3 ab = end.subtract(from);
            double abLen2 = Math.max(ab.lengthSqr(), 1.0E-4D);
            AABB box = new AABB(from, end).inflate(3.0D);
            for (LivingEntity e : this.level().getEntitiesOfClass(LivingEntity.class, box,
                    x -> x != this && !(x instanceof AbstractLeviathan))) {
                Vec3 c = e.getBoundingBox().getCenter();
                double t = Mth.clamp(c.subtract(from).dot(ab) / abLen2, 0.0D, 1.0D);
                Vec3 closest = from.add(ab.scale(t));
                double r = 2.0D + e.getBbWidth() * 0.5D;
                if (closest.distanceToSqr(c) <= r * r) {
                    e.hurt(this.damageSources().indirectMagic(this, this), 5.0F);
                }
            }
        }
    }

    /** Envía partículas visibles desde lejos (el rayo es largo). */
    private static void sendParticle(ServerLevel level, ParticleOptions type,
                                     double x, double y, double z, int count, double spread) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) < 96.0D * 96.0D) {
                level.sendParticles(player, type, true, x, y, z, count, spread, spread, spread, 0.0D);
            }
        }
    }

    // ------------------------------------------------------------------ barra de jefe

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
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

    // ------------------------------------------------------------------ sonidos

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ELDER_GUARDIAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    // ------------------------------------------------------------------ objetivos y movimiento

    private boolean isWaterAt(double x, double y, double z) {
        return this.level().getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER);
    }

    /** Nada sin rumbo por el lago, sin depender del pathfinding (la hitbox es enorme). */
    static class RoamGoal extends Goal {
        private final LeviathanEntity mob;
        private Vec3 dest;
        private int timeout;

        RoamGoal(LeviathanEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() == null && this.mob.isInWater() && this.mob.getRandom().nextInt(40) == 0;
        }

        @Override
        public void start() {
            this.dest = null;
            for (int i = 0; i < 10; i++) {
                double x = this.mob.getX() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * 24.0D;
                double y = this.mob.getY() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * 5.0D;
                double z = this.mob.getZ() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * 24.0D;
                if (!this.mob.isWaterAt(x, y, z)) {
                    continue;
                }
                Vec3 to = new Vec3(x, y, z);
                BlockHitResult hit = this.mob.level().clip(new ClipContext(this.mob.position(), to,
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
                if (hit.getType() == HitResult.Type.MISS) {
                    this.dest = to;
                    break;
                }
            }
            this.timeout = 300;
        }

        @Override
        public boolean canContinueToUse() {
            return this.dest != null && this.timeout > 0 && this.mob.getTarget() == null
                    && this.mob.position().distanceToSqr(this.dest) > 16.0D;
        }

        @Override
        public void tick() {
            this.timeout--;
            this.mob.getMoveControl().setWantedPosition(this.dest.x, this.dest.y, this.dest.z, 1.0D);
        }

        @Override
        public void stop() {
            this.dest = null;
        }
    }

    /** Combate: se acerca, muerde de cerca y, a distancia, dispara orbes o carga el rayo. */
    static class CombatGoal extends Goal {
        private static final int MODE_NONE = 0;
        private static final int MODE_BOLTS = 1;
        private static final int MODE_CHARGE = 2;
        private static final int MODE_BEAM = 3;

        private final LeviathanEntity mob;
        private int mode;
        private int timer;
        private int cooldown = 40;
        private int boltsLeft;
        private int biteCooldown;
        private Vec3 beamDir = Vec3.ZERO;

        CombatGoal(LeviathanEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity t = this.mob.getTarget();
            return t != null && t.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.mode = MODE_NONE;
            this.cooldown = 30;
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            this.mode = MODE_NONE;
            this.mob.setPhase(PHASE_IDLE);
            this.mob.setAggressive(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }
            this.mob.getLookControl().setLookAt(target, 8.0F, 8.0F);
            if (this.biteCooldown > 0) {
                this.biteCooldown--;
            }

            Vec3 mouth = this.mob.getMouthPos();
            Vec3 center = target.getBoundingBox().getCenter();
            double mouthDist = mouth.distanceTo(center);

            switch (this.mode) {
                case MODE_BOLTS -> {
                    this.mob.getMoveControl().setWantedPosition(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0.0D);
                    this.timer++;
                    if (this.timer % 12 == 0) {
                        this.mob.fireBolt(target);
                        this.boltsLeft--;
                    }
                    if (this.boltsLeft <= 0) {
                        this.endAttack(50 + this.mob.getRandom().nextInt(40));
                    }
                }
                case MODE_CHARGE -> {
                    this.timer++;
                    this.mob.chargeBeamParticles(this.timer);
                    if (this.timer >= 50) {
                        Vec3 d = center.subtract(mouth);
                        this.beamDir = d.lengthSqr() < 1.0E-4D ? this.mob.getViewVector(1.0F) : d.normalize();
                        this.mode = MODE_BEAM;
                        this.timer = 0;
                        this.mob.setPhase(PHASE_BEAM_FIRE);
                    }
                }
                case MODE_BEAM -> {
                    this.timer++;
                    Vec3 d = center.subtract(mouth);
                    if (d.lengthSqr() > 1.0E-4D) {
                        // El rayo sigue al objetivo con retraso: se puede esquivar moviéndose.
                        this.beamDir = this.beamDir.lerp(d.normalize(), 0.10D).normalize();
                    }
                    this.mob.fireBeamTick(this.beamDir, this.timer);
                    if (this.timer >= 40) {
                        this.endAttack(80 + this.mob.getRandom().nextInt(60));
                    }
                }
                default -> {
                    // Mordisco si lo tiene en la boca.
                    if (mouthDist < 6.0D + target.getBbWidth()) {
                        if (this.biteCooldown <= 0) {
                            this.mob.swing(InteractionHand.MAIN_HAND);
                            this.mob.doHurtTarget(target);
                            this.biteCooldown = 30;
                        }
                    } else if (mouthDist > 24.0D && this.mob.isWaterAt(center.x, center.y, center.z)) {
                        // Se acerca (solo por agua) hasta quedar a distancia de tiro.
                        this.mob.getMoveControl().setWantedPosition(center.x, center.y, center.z, 1.0D);
                    }

                    this.cooldown--;
                    if (this.cooldown <= 0 && mouthDist > 7.0D && this.mob.getSensing().hasLineOfSight(target)) {
                        if (mouthDist > 14.0D && this.mob.getRandom().nextInt(3) == 0) {
                            this.mode = MODE_CHARGE;
                            this.timer = 0;
                            this.mob.setPhase(PHASE_BEAM_CHARGE);
                        } else {
                            this.mode = MODE_BOLTS;
                            this.timer = 0;
                            this.boltsLeft = 4;
                            this.mob.setPhase(PHASE_BOLTS);
                        }
                    }
                }
            }
        }

        private void endAttack(int nextCooldown) {
            this.mode = MODE_NONE;
            this.timer = 0;
            this.cooldown = nextCooldown;
            this.mob.setPhase(PHASE_IDLE);
        }
    }
}

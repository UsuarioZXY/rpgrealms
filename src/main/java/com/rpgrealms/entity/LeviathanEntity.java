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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
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
 * Leviatán adulto: colosal, lento y muy resistente. Vive en los lagos del Abismo.
 * Ataques: mordisco, ráfaga de orbes abisales y un rayo cargado que recorre toda la cueva.
 */
public class LeviathanEntity extends AbstractLeviathan {

    /** true = solo ataca si lo hieren (como la cría). false = ataca a los jugadores cercanos. */
    private static final boolean PROVOKED_ONLY = true;

    public static final int PHASE_IDLE = 0;
    public static final int PHASE_BOLTS = 1;
    public static final int PHASE_BEAM_CHARGE = 2;
    public static final int PHASE_BEAM_FIRE = 3;

    public static final int CHARGE_TICKS = 50;
    public static final int BEAM_TICKS = 40;

    private static final EntityDataAccessor<Integer> DATA_PHASE =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TIMER =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_RECOIL =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.INT);
    // Extremo del rayo, relativo a la posición de la entidad (para dibujarlo en el cliente).
    private static final EntityDataAccessor<Float> DATA_BEAM_X =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_BEAM_Y =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_BEAM_Z =
            SynchedEntityData.defineId(LeviathanEntity.class, EntityDataSerializers.FLOAT);

    private boolean enraged = false;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.rpgrealms.leviathan"),
            BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_12);

    public LeviathanEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level, 3.2F);
        this.xpReward = 500;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)      // lento comparado con la cría
                .add(Attributes.ATTACK_DAMAGE, 45.0D)
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
        this.entityData.define(DATA_TIMER, 0);
        this.entityData.define(DATA_RECOIL, 0);
        this.entityData.define(DATA_BEAM_X, 0.0F);
        this.entityData.define(DATA_BEAM_Y, 0.0F);
        this.entityData.define(DATA_BEAM_Z, 0.0F);
    }

    @Override
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    @Override
    public int getPhaseTimer() {
        return this.entityData.get(DATA_TIMER);
    }

    public void setPhaseTimer(int ticks) {
        this.entityData.set(DATA_TIMER, ticks);
    }

    @Override
    public int getRecoil() {
        return this.entityData.get(DATA_RECOIL);
    }

    @Override
    public double bodyScale() {
        return 6.5D;
    }

    @Override
    protected double cruiseSpeed() {
        return 0.30D;   // ~6 bloques/s: majestuoso
    }

    /** Extremo del rayo relativo a los pies de la entidad. */
    public Vec3 getBeamEndOffset() {
        return new Vec3(this.entityData.get(DATA_BEAM_X), this.entityData.get(DATA_BEAM_Y),
                this.entityData.get(DATA_BEAM_Z));
    }

    // ------------------------------------------------------------------ IA

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CombatGoal(this));
        this.goalSelector.addGoal(1, new RoamGoal(this, 48.0D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        if (!PROVOKED_ONLY) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    protected double mouthForward() {
        return 26.0D;
    }

    @Override
    protected double mouthUp() {
        return 2.6D;
    }

    @Override
    protected double trailBack() {
        return 48.0D;
    }

    @Override
    protected double cullingInflate() {
        return 80.0D; // el rayo llega a 56 bloques: no debe desaparecer al salir de pantalla
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
        Vec3 dir = aim.normalize();
        // Abanico de orbes: 3 con buena vida, 5 herido. Los laterales se curvan hacia el objetivo (guiado).
        int count = this.getHealth() <= this.getMaxHealth() * 0.5F ? 5 : 3;
        for (int i = 0; i < count; i++) {
            double offset = (i - (count - 1) / 2.0D) * 0.24D;   // radianes (~14º entre orbes)
            Vec3 d = dir.yRot((float) offset);
            AbyssBoltEntity bolt = new AbyssBoltEntity(this.level(), this, d.x, d.y, d.z);
            bolt.setPos(mouth.x, mouth.y, mouth.z);
            bolt.setDeltaMovement(d.scale(i == (count - 1) / 2 ? 1.5D : 1.1D));
            this.level().addFreshEntity(bolt);
        }

        // Retroceso visual (el cliente lo anima) y empujoncito físico hacia atrás.
        this.entityData.set(DATA_RECOIL, 6);
        this.setDeltaMovement(this.getDeltaMovement().add(dir.scale(-0.07D)));

        if (this.level() instanceof ServerLevel serverLevel) {
            sendParticle(serverLevel, ParticleTypes.FLASH, mouth.x, mouth.y, mouth.z, 1, 0.0D, 0.0D);
            sendParticle(serverLevel, ParticleTypes.GLOW, mouth.x, mouth.y, mouth.z, 16, 0.8D, 0.08D);
            sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, mouth.x, mouth.y, mouth.z, 20, 1.2D, 0.2D);
            // anillo que sale de la boca
            Vec3 up = Math.abs(dir.y) < 0.99D ? new Vec3(0.0D, 1.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
            Vec3 u = dir.cross(up).normalize();
            Vec3 v = dir.cross(u);
            for (int i = 0; i < 16; i++) {
                double a = i * Math.PI / 8.0D;
                Vec3 p = mouth.add(dir.scale(1.5D)).add(u.scale(Math.cos(a) * 2.2D)).add(v.scale(Math.sin(a) * 2.2D));
                sendParticle(serverLevel, ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.0D, 0.0D);
            }
        }
        this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 3.0F, 0.5F);
        this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.HOSTILE, 3.0F, 0.5F);
    }

    /** Un tick de carga: las partículas se concentran en espiral en la boca y crece un núcleo luminoso. */
    public void chargeBeamParticles(int tick) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        Vec3 mouth = this.getMouthPos();
        float prog = Mth.clamp(tick / (float) CHARGE_TICKS, 0.0F, 1.0F);
        if (tick == 1) {
            this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                    SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.HOSTILE, 5.0F, 0.7F);
        }
        double radius = 8.0D * (1.0F - prog) + 0.8D;
        double spin = tick * 0.55D;
        for (int i = 0; i < 4; i++) {
            double a = spin + i * Math.PI / 2.0D;
            double b = Math.sin(tick * 0.2D + i) * 0.9D;
            double x = mouth.x + Math.cos(a) * Math.cos(b) * radius;
            double y = mouth.y + Math.sin(b) * radius;
            double z = mouth.z + Math.sin(a) * Math.cos(b) * radius;
            sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0.0D, 0.0D);
            sendParticle(serverLevel, ParticleTypes.GLOW, x, y, z, 1, 0.0D, 0.0D);
        }
        // Núcleo que se va llenando
        sendParticle(serverLevel, ParticleTypes.END_ROD, mouth.x, mouth.y, mouth.z, 2 + (int) (prog * 6.0F),
                0.25D + prog * 0.9D, 0.0D);
        if (tick % 8 == 0) {
            this.level().playSound(null, mouth.x, mouth.y, mouth.z,
                    SoundEvents.BEACON_AMBIENT, SoundSource.HOSTILE, 4.0F, 0.5F + prog);
        }
    }

    /** Un tick del rayo: guarda el extremo (el cliente dibuja el haz), añade chispas y daña lo que cruza. */
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

        // El cliente dibuja el haz entre la boca y este punto.
        this.entityData.set(DATA_BEAM_X, (float) (end.x - this.getX()));
        this.entityData.set(DATA_BEAM_Y, (float) (end.y - this.getY()));
        this.entityData.set(DATA_BEAM_Z, (float) (end.z - this.getZ()));

        // Retroceso del cuerpo mientras dispara.
        this.setDeltaMovement(this.getDeltaMovement().add(dir.scale(-0.02D)));

        if (tick == 1) {
            this.level().playSound(null, from.x, from.y, from.z,
                    SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 6.0F, 0.6F);
            sendParticle(serverLevel, ParticleTypes.SONIC_BOOM, from.x, from.y, from.z, 1, 0.0D, 0.0D);
            sendParticle(serverLevel, ParticleTypes.FLASH, from.x, from.y, from.z, 1, 0.0D, 0.0D);
        }

        // Hélice doble de chispas alrededor del haz.
        Vec3 up = Math.abs(dir.y) < 0.99D ? new Vec3(0.0D, 1.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
        Vec3 u = dir.cross(up).normalize();
        Vec3 v = dir.cross(u);
        long gt = this.level().getGameTime();
        for (double s = 0.0D; s <= len; s += 2.0D) {
            double ang = s * 0.9D + gt * 0.6D;
            for (int strand = 0; strand < 2; strand++) {
                double a = ang + strand * Math.PI;
                Vec3 p = from.add(dir.scale(s)).add(u.scale(Math.cos(a) * 1.5D)).add(v.scale(Math.sin(a) * 1.5D));
                sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 1, 0.0D, 0.0D);
            }
            if (((int) s) % 6 == 0) {
                Vec3 c = from.add(dir.scale(s));
                sendParticle(serverLevel, ParticleTypes.END_ROD, c.x, c.y, c.z, 1, 0.3D, 0.0D);
            }
        }
        // Impacto
        sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, end.x, end.y, end.z, 18, 0.9D, 0.3D);
        sendParticle(serverLevel, ParticleTypes.GLOW, end.x, end.y, end.z, 8, 0.7D, 0.05D);
        if (tick % 4 == 0) {
            sendParticle(serverLevel, ParticleTypes.EXPLOSION, end.x, end.y, end.z, 1, 0.5D, 0.0D);
        }

        if (tick % 5 == 0) {
            this.level().playSound(null, from.x, from.y, from.z,
                    SoundEvents.GUARDIAN_ATTACK, SoundSource.HOSTILE, 4.0F, 0.6F);
            this.level().playSound(null, end.x, end.y, end.z,
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.HOSTILE, 3.0F, 0.5F);
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
                                     double x, double y, double z, int count, double spread, double speed) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) < 96.0D * 96.0D) {
                level.sendParticles(player, type, true, x, y, z, count, spread, spread, spread, speed);
            }
        }
    }

    // ------------------------------------------------------------------ barra de jefe

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (!this.enraged && this.getHealth() <= this.getMaxHealth() * 0.5F) {
            this.enraged = true;
            this.bossEvent.setColor(BossEvent.BossBarColor.RED);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 8.0F, 0.6F);
            if (this.level() instanceof ServerLevel serverLevel) {
                Vec3 m = this.getMouthPos();
                sendParticle(serverLevel, ParticleTypes.SONIC_BOOM, m.x, m.y, m.z, 1, 0.0D, 0.0D);
                sendParticle(serverLevel, ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 2.0D, this.getZ(),
                        80, 6.0D, 0.4D);
            }
        }
        int recoil = this.entityData.get(DATA_RECOIL);
        if (recoil > 0) {
            this.entityData.set(DATA_RECOIL, recoil - 1);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        this.enraged = this.getHealth() <= this.getMaxHealth() * 0.5F;
        if (this.enraged) {
            this.bossEvent.setColor(BossEvent.BossBarColor.RED);
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

    // ------------------------------------------------------------------ movimiento

    private boolean isWaterAt(double x, double y, double z) {
        return this.isWaterPoint(x, y, z);
    }

    /** Punto de agua más lejano que se puede alcanzar en línea recta hacia el objetivo (null si no hay). */
    private Vec3 nearestWaterPointToward(Vec3 target) {
        Vec3 from = this.position();
        Vec3 d = target.subtract(from);
        double len = d.length();
        if (len < 1.0E-3D) {
            return null;
        }
        Vec3 dir = d.scale(1.0D / len);
        Vec3 best = null;
        for (double s = 3.0D; s <= len; s += 2.0D) {
            Vec3 p = from.add(dir.scale(s));
            if (this.isWaterAt(p.x, p.y, p.z)) {
                best = p;
            } else {
                break;
            }
        }
        return best;
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
            this.mob.setPhaseTimer(0);
            this.mob.setAggressive(false);
        }

        private void hold() {
            this.mob.getMoveControl().setWantedPosition(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0.0D);
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
                    this.hold();
                    this.timer++;
                    this.mob.setPhaseTimer(this.timer);
                    if (this.timer % 18 == 0) {
                        this.mob.fireBolt(target);
                        this.boltsLeft--;
                    }
                    if (this.boltsLeft <= 0) {
                        this.endAttack(this.nextCooldown());
                    }
                }
                case MODE_CHARGE -> {
                    this.hold();
                    this.timer++;
                    this.mob.setPhaseTimer(this.timer);
                    this.mob.chargeBeamParticles(this.timer);
                    if (this.timer >= CHARGE_TICKS) {
                        Vec3 d = center.subtract(mouth);
                        this.beamDir = d.lengthSqr() < 1.0E-4D ? this.mob.getViewVector(1.0F) : d.normalize();
                        this.mode = MODE_BEAM;
                        this.timer = 0;
                        this.mob.setPhaseTimer(0);
                        this.mob.setPhase(PHASE_BEAM_FIRE);
                    }
                }
                case MODE_BEAM -> {
                    this.hold();
                    this.timer++;
                    this.mob.setPhaseTimer(this.timer);
                    Vec3 d = center.subtract(mouth);
                    if (d.lengthSqr() > 1.0E-4D) {
                        // El rayo sigue al objetivo con retraso: se puede esquivar moviéndose.
                        this.beamDir = this.beamDir.lerp(d.normalize(), 0.10D).normalize();
                    }
                    this.mob.fireBeamTick(this.beamDir, this.timer);
                    if (this.timer >= BEAM_TICKS) {
                        this.endAttack(this.nextCooldown());
                    }
                }
                default -> {
                    float hp = this.mob.getHealth() / this.mob.getMaxHealth();
                    boolean wounded = hp <= 0.5F;
                    boolean desperate = hp <= 0.25F;
                    double reach = 9.0D + target.getBbWidth();

                    if (!wounded) {
                        // CON BUENA VIDA: va de frente a matar. Se acerca lo máximo que permite el agua y muerde.
                        if (mouthDist < reach) {
                            this.tryBite(target, 20);
                        } else {
                            Vec3 p = this.mob.nearestWaterPointToward(center);
                            if (p != null) {
                                this.mob.getMoveControl().setWantedPosition(p.x, p.y, p.z, 1.7D);
                            }
                        }
                    } else {
                        // CON POCA VIDA: guarda distancia y prefiere el rayo.
                        if (mouthDist < reach) {
                            this.tryBite(target, 30);     // si lo tienen encima, muerde
                        } else if (mouthDist < 16.0D) {
                            Vec3 away = this.mob.position().subtract(center).multiply(1.0D, 0.0D, 1.0D);
                            if (away.lengthSqr() > 1.0E-4D) {
                                Vec3 back = this.mob.position().add(away.normalize().scale(14.0D));
                                if (this.mob.isWaterAt(back.x, this.mob.getY(), back.z)) {
                                    this.mob.getMoveControl().setWantedPosition(back.x, this.mob.getY(), back.z, 1.2D);
                                }
                            }
                        } else if (mouthDist > 40.0D) {
                            Vec3 p = this.mob.nearestWaterPointToward(center);
                            if (p != null) {
                                this.mob.getMoveControl().setWantedPosition(p.x, p.y, p.z, 1.0D);
                            }
                        }
                    }

                    this.cooldown--;
                    if (this.cooldown <= 0 && this.mob.getSensing().hasLineOfSight(target)) {
                        if (wounded) {
                            // Rayo casi siempre; orbes de vez en cuando.
                            double beamChance = desperate ? 0.9D : 0.7D;
                            if (mouthDist > 5.0D && this.mob.getRandom().nextDouble() < beamChance) {
                                this.startCharge();
                            } else if (mouthDist > 7.0D) {
                                this.startBolts();
                            }
                        } else if (mouthDist > 12.0D) {
                            // Sano pero sin poder alcanzarlo (p. ej. el jugador está en tierra): dispara.
                            if (this.mob.getRandom().nextInt(5) == 0) {
                                this.startCharge();
                            } else {
                                this.startBolts();
                            }
                        }
                    }
                }
            }
        }

        private void tryBite(LivingEntity target, int cooldownTicks) {
            if (this.biteCooldown <= 0) {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
                this.biteCooldown = cooldownTicks;
            }
        }

        private void startCharge() {
            this.mode = MODE_CHARGE;
            this.timer = 0;
            this.mob.setPhaseTimer(0);
            this.mob.setPhase(PHASE_BEAM_CHARGE);
        }

        private void startBolts() {
            this.mode = MODE_BOLTS;
            this.timer = 0;
            this.boltsLeft = this.mob.getHealth() <= this.mob.getMaxHealth() * 0.5F ? 4 : 3;
            this.mob.setPhaseTimer(0);
            this.mob.setPhase(PHASE_BOLTS);
        }

        /** Pausa hasta el siguiente ataque a distancia: mucho más corta cuanta menos vida tenga. */
        private int nextCooldown() {
            float hp = this.mob.getHealth() / this.mob.getMaxHealth();
            if (hp <= 0.25F) {
                return 25 + this.mob.getRandom().nextInt(20);
            }
            if (hp <= 0.5F) {
                return 35 + this.mob.getRandom().nextInt(25);
            }
            return 60 + this.mob.getRandom().nextInt(40);
        }

        private void endAttack(int nextCooldown) {
            this.mode = MODE_NONE;
            this.timer = 0;
            this.cooldown = nextCooldown;
            this.mob.setPhase(PHASE_IDLE);
            this.mob.setPhaseTimer(0);
        }
    }
}

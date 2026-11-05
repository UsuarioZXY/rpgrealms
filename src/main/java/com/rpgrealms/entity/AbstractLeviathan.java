package com.rpgrealms.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.joml.Vector3f;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import java.util.EnumSet;
import net.minecraft.world.phys.Vec3;

/**
 * Base común de la Cría y del Leviatán adulto: nado suave, burbujas por el hocico,
 * posición de la boca y área de culling ampliada (el modelo es mucho más largo que la hitbox).
 */
public abstract class AbstractLeviathan extends WaterAnimal {

    /** Hitbox por partes: cabeza, pecho, cuerpo, grupa y dos tramos de cola (t = posición de morro a cola). */
    private static final String[] PART_NAMES = {"head", "chest", "body", "rump", "tail1", "tail2"};
    private static final double[] PART_T = {0.08D, 0.26D, 0.46D, 0.64D, 0.80D, 0.93D};
    private static final double[] PART_W = {0.70D, 1.00D, 1.00D, 0.80D, 0.50D, 0.35D};
    private static final double[] PART_H = {0.75D, 1.00D, 1.00D, 0.80D, 0.50D, 0.35D};
    private final LeviathanPart[] parts;

    protected AbstractLeviathan(EntityType<? extends WaterAnimal> type, Level level, float turnDegPerTick) {
        super(type, level);
        double bodyH = this.bodyScale() * 2.0D;   // el cuerpo mide 32 px de alto en el modelo
        this.parts = new LeviathanPart[PART_NAMES.length];
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i] = new LeviathanPart(this, PART_NAMES[i], (float) (bodyH * PART_W[i]), (float) (bodyH * PART_H[i]));
        }
        this.moveControl = new SwimControl(this, turnDegPerTick);
        // El LookControl de vainilla pone el cabeceo a 0 cada tick; aquí lo manda el control de nado.
        this.lookControl = new LookControl(this) {
            @Override
            protected boolean resetXRotOnTick() {
                return false;
            }
        };
    }

    /** Velocidad de crucero (bloques/tick) con velocidad relativa 1.0. */
    protected abstract double cruiseSpeed();

    /** Agua en ese punto. Si el chunk no está cargado devuelve false: así nunca fuerza la carga/generación de chunks. */
    protected boolean isWaterPoint(double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        return this.level().hasChunkAt(pos) && this.level().getFluidState(pos).is(FluidTags.WATER);
    }

    /**
     * Control de nado propio. El de vainilla solo avanza con un camino calculado, y con hitboxes tan grandes
     * el pathfinding casi nunca encuentra uno. Este nada en línea recta, gira con un radio de giro realista
     * y "llega" a un radio mayor que ese giro (si no, daría vueltas alrededor del destino sin alcanzarlo).
     */
    public static class SwimControl extends MoveControl {
        private final AbstractLeviathan fish;
        private final float turn;

        public SwimControl(AbstractLeviathan mob, float turnDegPerTick) {
            super(mob);
            this.fish = mob;
            this.turn = turnDegPerTick;
        }

        @Override
        public void tick() {
            if (!this.fish.isInWater() || this.operation != MoveControl.Operation.MOVE_TO) {
                return;   // fuera del agua no hay empuje (si no, "volaría")
            }
            double dx = this.wantedX - this.fish.getX();
            double dy = this.wantedY - this.fish.getY();
            double dz = this.wantedZ - this.fish.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double cruise = this.fish.cruiseSpeed();
            double turnRadius = cruise / Math.toRadians(this.turn);
            if (dist < Math.max(2.0D, turnRadius * 1.1D) || this.speedModifier <= 0.0D) {
                this.operation = MoveControl.Operation.WAIT;
                return;
            }

            float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
            float diff = Mth.wrapDegrees(targetYaw - this.fish.getYRot());
            float yaw = this.fish.getYRot() + Mth.clamp(diff, -this.turn, this.turn);
            this.fish.setYRot(yaw);
            this.fish.yBodyRot = yaw;
            this.fish.yHeadRot = yaw;

            double horiz = Math.sqrt(dx * dx + dz * dz);
            float targetPitch = Mth.clamp((float) -(Mth.atan2(dy, horiz) * (180.0D / Math.PI)), -45.0F, 45.0F);
            float pr = this.turn * 0.7F;
            this.fish.setXRot(this.fish.getXRot() + Mth.clamp(targetPitch - this.fish.getXRot(), -pr, pr));

            // Avanza fuerte solo cuando mira hacia donde va; si está girando, apenas avanza.
            double align = Mth.clamp(Math.cos(Math.toRadians(diff)), 0.15D, 1.0D);
            double accel = cruise * this.speedModifier / 9.0D * align;
            Vec3 look = Vec3.directionFromRotation(this.fish.getXRot(), this.fish.getYRot());
            this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(look.scale(accel)));
        }
    }

    /** Embiste y muerde a quien la hirió, sin pathfinding (su hitbox es demasiado grande para él). */
    public static class BiteGoal extends Goal {
        private final AbstractLeviathan mob;
        private final double speed;
        private int biteCooldown;

        public BiteGoal(AbstractLeviathan mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            net.minecraft.world.entity.LivingEntity t = this.mob.getTarget();
            return t != null && t.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            net.minecraft.world.entity.LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }
            this.biteCooldown--;
            Vec3 center = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
            double dist = this.mob.getMouthPos().distanceTo(center);
            if (dist < 2.0D + this.mob.bodyScale() * 0.6D + target.getBbWidth() && this.biteCooldown <= 0) {
                this.mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
                this.biteCooldown = 20;
            }
            if (this.mob.isWaterPoint(center.x, center.y, center.z)) {
                this.mob.getMoveControl().setWantedPosition(center.x, center.y, center.z, this.speed);
            }
        }
    }

    /**
     * Crece: se sustituye por la siguiente etapa si ésta cabe en el sitio (prueba también un poco más arriba).
     * Devuelve false si no hay espacio (se reintenta más tarde).
     */
    protected boolean growInto(EntityType<? extends AbstractLeviathan> type) {
        if (!(this.level() instanceof net.minecraft.server.level.ServerLevel sl)) {
            return false;
        }
        AbstractLeviathan next = type.create(sl);
        if (next == null) {
            return false;
        }
        boolean fits = false;
        for (double lift : new double[]{0.0D, 1.5D, 3.0D}) {
            next.moveTo(this.getX(), this.getY() + lift, this.getZ(), this.getYRot(), 0.0F);
            if (sl.noCollision(next)) {
                fits = true;
                break;
            }
        }
        if (!fits) {
            return false;
        }
        if (this.hasCustomName()) {
            next.setCustomName(this.getCustomName());
        }
        next.setPersistenceRequired();
        sl.addFreshEntity(next);
        sl.sendParticles(ParticleTypes.END_ROD, next.getX(), next.getY() + next.getBbHeight() * 0.5D, next.getZ(),
                80, next.getBbWidth() * 0.5D, next.getBbHeight() * 0.3D, next.getBbWidth() * 0.5D, 0.05D);
        sl.sendParticles(ParticleTypes.GLOW, next.getX(), next.getY() + next.getBbHeight() * 0.5D, next.getZ(),
                60, next.getBbWidth() * 0.5D, next.getBbHeight() * 0.3D, next.getBbWidth() * 0.5D, 0.05D);
        sl.playSound(null, next.getX(), next.getY(), next.getZ(), net.minecraft.sounds.SoundEvents.ENDER_DRAGON_GROWL,
                net.minecraft.sounds.SoundSource.NEUTRAL, 3.0F, 0.9F);
        this.discard();
        return true;
    }

    /** Nada sin rumbo con calma, como una ballena: destinos lejanos, sin chocar y sin hundirse hasta el fondo. */
    public static class RoamGoal extends Goal {
        private final AbstractLeviathan mob;
        private final double range;
        private final double surfaceBias;
        private Vec3 dest;
        private int timeout;
        private int checkTick;
        private Vec3 lastPos = Vec3.ZERO;

        public RoamGoal(AbstractLeviathan mob, double range) {
            this(mob, range, 0.0D);
        }

        /** surfaceBias: probabilidad de elegir un destino cerca de la superficie (para que las crías salten). */
        public RoamGoal(AbstractLeviathan mob, double range, double surfaceBias) {
            this.mob = mob;
            this.range = range;
            this.surfaceBias = surfaceBias;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob.getTarget() != null || !this.mob.isInWater() || this.mob.getRandom().nextInt(25) != 0) {
                return false;
            }
            this.pickDestination();
            return this.dest != null;   // sin destino válido, el objetivo ni arranca
        }

        private void pickDestination() {
            this.dest = null;
            double h = this.mob.getBbHeight();
            for (int i = 0; i < 16 && this.dest == null; i++) {
                double ang = this.mob.getRandom().nextDouble() * Math.PI * 2.0D;
                double d = this.range * (0.5D + this.mob.getRandom().nextDouble() * 0.5D);
                double x = this.mob.getX() + Math.cos(ang) * d;
                double z = this.mob.getZ() + Math.sin(ang) * d;
                // Columna de agua en el destino: superficie y fondo.
                int by = Mth.floor(this.mob.getY() + 0.5D);   // desde los pies: en la superficie el centro del cuerpo puede estar en el aire
                if (!this.mob.isWaterPoint(x, by, z)) {
                    continue;
                }
                int top = by;
                while (top < by + 64 && this.mob.isWaterPoint(x, top + 1, z)) {
                    top++;
                }
                int floor = by;
                while (floor > by - 64 && this.mob.isWaterPoint(x, floor - 1, z)) {
                    floor--;
                }
                if (top - floor < h * 0.6D) {
                    continue;   // demasiado poco profundo
                }
                double lo = floor + h * 0.5D + 1.0D;
                double hi = top - h * 0.5D - 1.0D;
                double y;
                if (hi >= lo && this.mob.getRandom().nextDouble() < this.surfaceBias) {
                    y = hi;   // justo bajo la superficie
                } else if (hi < lo) {
                    y = (top + floor) * 0.5D + 0.5D;   // agua justa: nada por el medio de la columna
                } else {
                    y = Mth.clamp(this.mob.getY() + h * 0.5D + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * 4.0D, lo, hi);
                }
                Vec3 to = new Vec3(x, y - h * 0.5D, z);   // y de los pies
                if (this.pathClear(to)) {
                    this.dest = to;
                }
            }
        }

        @Override
        public void start() {
            this.timeout = 400;
            this.checkTick = 0;
            this.lastPos = this.mob.position();
        }

        /** Comprueba que el cuerpo entero cabe a lo largo del trayecto. */
        private boolean pathClear(Vec3 to) {
            Vec3 from = this.mob.position();
            Vec3 delta = new Vec3(to.x - from.x, to.y - from.y, to.z - from.z);
            for (int k = 1; k <= 6; k++) {
                Vec3 off = delta.scale(k / 6.0D);
                if (!this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().move(off))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.dest != null && this.timeout > 0 && this.mob.getTarget() == null && this.mob.isInWater()
                    && this.mob.getMoveControl().hasWanted();
        }

        @Override
        public void tick() {
            if (this.dest == null) {
                this.timeout = 0;
                return;
            }
            this.timeout--;
            this.mob.getMoveControl().setWantedPosition(this.dest.x, this.dest.y, this.dest.z, 1.0D);
            // Anti-atasco: si en 2 segundos casi no se movió, abandona este destino.
            if (++this.checkTick >= 40) {
                this.checkTick = 0;
                if (this.mob.position().distanceToSqr(this.lastPos) < 4.0D) {
                    this.timeout = 0;
                }
                this.lastPos = this.mob.position();
            }
        }

        @Override
        public void stop() {
            this.dest = null;
        }
    }

    /** Distancia (en bloques) desde el centro de la entidad hasta la boca. */
    protected abstract double mouthForward();

    /** Altura (en bloques) de la boca sobre los pies de la entidad. */
    protected abstract double mouthUp();

    /** Cuánto se amplía el AABB de culling en horizontal. */
    protected abstract double cullingInflate();

    /** Distancia desde el centro hasta la cola (para repartir las partículas del rastro). */
    protected double trailBack() {
        return this.mouthForward() * 2.2D;
    }

    /** Cantidad relativa de partículas místicas (1 = adulto). */
    protected double trailDensity() {
        return 1.0D;
    }

    /** Fase de ataque sincronizada con el cliente (0 = reposo). El adulto la sobrescribe. */
    public int getPhase() {
        return 0;
    }

    /** Fuerza del retroceso de disparo (0..6). Solo el adulto lo usa. */
    public int getRecoil() {
        return 0;
    }

    /** Progreso (en ticks) de la fase de ataque actual. Solo el adulto lo usa. */
    public int getPhaseTimer() {
        return 0;
    }

    /** Posición de la boca RELATIVA a los pies de la entidad, interpolada para el render. */
    public Vec3 getMouthOffset(float partialTick) {
        float yaw = Mth.rotLerp(partialTick, this.yBodyRotO, this.yBodyRot);
        float pitch = Mth.lerp(partialTick, this.xRotO, this.getXRot());
        Vec3 dir = Vec3.directionFromRotation(pitch * 0.7F, yaw);
        return dir.scale(this.mouthForward()).add(0.0D, this.mouthUp(), 0.0D);
    }

    public Vec3 getMouthPos() {
        return this.position().add(this.getMouthOffset(1.0F));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (!this.isUnderWater()) {
                // con la cabeza fuera del agua la gravedad lo devuelve (saltos y superficie)
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    private int hopCooldown;

    @Override
    public boolean causeFallDamage(float distance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;   // criaturas marinas: sin daño de caída (saltos y varado)
    }

    protected double hopPower() {
        return 0.95D / Math.sqrt(this.bodyScale());
    }

    protected double hopLift() {
        return 0.9D / Math.pow(this.bodyScale(), 0.2D);
    }

    /** Punto de agua más cercano (hasta 24 bloques) o null. */
    private Vec3 findNearestWater() {
        for (int r = 2; r <= 24; r += 2) {
            for (int k = 0; k < 16; k++) {
                double a = k * Math.PI / 8.0D;
                double x = this.getX() + Math.cos(a) * r;
                double z = this.getZ() + Math.sin(a) * r;
                for (int dy = -3; dy <= 1; dy++) {
                    if (this.isWaterPoint(x, this.getY() + dy, z)) {
                        return new Vec3(x, this.getY() + dy, z);
                    }
                }
            }
        }
        return null;
    }

    /** Varado en tierra: da saltos como un pez, orientándose hacia el agua más cercana. */
    private void beachedHop() {
        if (--this.hopCooldown > 0) {
            return;
        }
        this.hopCooldown = 14 + this.random.nextInt(16);
        Vec3 to = this.findNearestWater();
        double ang = to != null ? Math.atan2(to.z - this.getZ(), to.x - this.getX()) : this.random.nextDouble() * Math.PI * 2.0D;
        float yaw = (float) (ang * (180.0D / Math.PI)) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
        double p = this.hopPower();
        this.setDeltaMovement(Math.cos(ang) * p, this.hopLift(), Math.sin(ang) * p);
        this.hasImpulse = true;
        this.playSound(net.minecraft.sounds.SoundEvents.COD_FLOP, 1.5F, 0.5F);
    }

    public static final int BITE_TICKS = 12;
    private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> DATA_BITE =
            net.minecraft.network.syncher.SynchedEntityData.defineId(AbstractLeviathan.class,
                    net.minecraft.network.syncher.EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BITE, 0);
    }

    /** Ticks que le quedan a la animación de mordisco (0 = ninguna). */
    public int getBiteTicks() {
        return this.entityData.get(DATA_BITE);
    }

    /**
     * Progreso 0..1 de la animación propia de mordisco. Abre la boca despacio, la mantiene abierta un instante,
     * cierra de golpe y se recupera. Devuelve {abertura, embestida de cabeza}.
     */
    public float[] biteCurve() {
        int t = this.getBiteTicks();
        if (t <= 0) {
            return new float[]{0.0F, 0.0F};
        }
        float p = 1.0F - t / (float) BITE_TICKS;
        float open = p < 0.45F ? p / 0.45F : p < 0.62F ? 1.0F - (p - 0.45F) / 0.17F : 0.0F;
        float lunge = (float) Math.sin(Math.min(1.0F, p / 0.7F) * Math.PI);
        return new float[]{open, lunge};
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide && this.getBiteTicks() > 0) {
            this.entityData.set(DATA_BITE, this.getBiteTicks() - 1);
        }
        if (!this.level().isClientSide && !this.isInWater() && this.onGround() && this.isAlive()) {
            this.beachedHop();
        }
        // Burbujas que salen del hocico (más seguidas cuando está enfadado).
        if (this.level().isClientSide && this.isInWater()
                && this.random.nextInt(this.isAggressive() ? 3 : 25) == 0) {
            Vec3 mouth = this.getMouthPos();
            this.level().addParticle(ParticleTypes.BUBBLE,
                    mouth.x + (this.random.nextDouble() - 0.5D),
                    mouth.y + (this.random.nextDouble() - 0.5D),
                    mouth.z + (this.random.nextDouble() - 0.5D),
                    0.0D, 0.06D, 0.0D);
        }
        this.updateParts();
        if (this.alertCooldown > 0) {
            this.alertCooldown--;
        }
        if (this.level().isClientSide) {
            this.mysticTrail();
            this.clientMagic();
        } else if (this.isAlive()) {
            this.serverMagic();
        }
        super.aiStep();
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.parts;
    }

    /** Las partes necesitan ids consecutivos al del leviatán (igual que el Dragón del End). */
    @Override
    public void setId(int id) {
        super.setId(id);
        if (this.parts != null) {
            for (int i = 0; i < this.parts.length; i++) {
                this.parts[i].setId(id + i + 1);
            }
        }
    }

    /** Coloca las partes a lo largo del cuerpo según su rumbo y cabeceo (cliente y servidor). */
    private void updateParts() {
        Vec3 dir = Vec3.directionFromRotation(this.getXRot() * 0.7F, this.yBodyRot);
        double front = this.mouthForward();
        double span = front + this.trailBack();
        double cy = this.getY() + this.getBbHeight() * 0.5D;
        for (int i = 0; i < this.parts.length; i++) {
            LeviathanPart part = this.parts[i];
            double along = front - PART_T[i] * span;
            double x = this.getX() + dir.x * along;
            double y = cy + dir.y * along - part.getBbHeight() * 0.5D;
            double z = this.getZ() + dir.z * along;
            part.xo = part.xOld = x;
            part.yo = part.yOld = y;
            part.zo = part.zOld = z;
            part.setPos(x, y, z);
        }
    }

    private int alertCooldown;

    /** Escudo de runas que se dibuja girando alrededor del cuerpo (0 = ninguno). */
    protected int shieldCount() {
        return 0;
    }

    /** Seres vivos que no son leviatanes dentro de un radio (para ondas y gritos). */
    protected java.util.List<net.minecraft.world.entity.LivingEntity> victims(double radius) {
        return this.level().getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class,
                this.getBoundingBox().inflate(radius),
                e -> e != this && e.isAlive() && !(e instanceof AbstractLeviathan)
                        && !(e instanceof net.minecraft.world.entity.player.Player p && p.isSpectator())
                        && e.distanceToSqr(this) <= radius * radius);
    }

    /**
     * Onda de choque alrededor del cuerpo: daño, empuje hacia fuera (más débil lejos), lentitud y oscuridad.
     * El anillo visible sale de las partículas que genera.
     */
    protected void shockwave(double radius, float damage, double knock, double lift, int slowTicks, int darkTicks) {
        for (net.minecraft.world.entity.LivingEntity e : this.victims(radius)) {
            double dist = Math.sqrt(e.distanceToSqr(this));
            double f = 1.0D - 0.5D * Math.min(1.0D, dist / radius);
            if (damage > 0.0F) {
                e.hurt(this.damageSources().indirectMagic(this, this), (float) (damage * f));
            }
            Vec3 away = e.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D);
            away = away.lengthSqr() < 1.0E-4D ? new Vec3(this.random.nextDouble() - 0.5D, 0.0D, this.random.nextDouble() - 0.5D) : away;
            away = away.normalize();
            e.setDeltaMovement(e.getDeltaMovement().add(away.x * knock * f, lift * f, away.z * knock * f));
            e.hurtMarked = true;
            if (slowTicks > 0) {
                e.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, slowTicks, 1));
            }
            if (darkTicks > 0) {
                e.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.DARKNESS, darkTicks, 0));
            }
        }
        if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            double cy = this.getY() + this.getBbHeight() * 0.5D;
            // tres anillos concéntricos a distintas alturas y velocidades, más una columna de agua central
            this.ringBurst(sl, this.getX(), cy, this.getZ(), ParticleTypes.CLOUD, 0.9D, 0.0D);
            this.ringBurst(sl, this.getX(), cy, this.getZ(), ParticleTypes.END_ROD, 1.3D, 0.05D);
            this.ringBurst(sl, this.getX(), cy - 1.0D, this.getZ(), ParticleTypes.BUBBLE_POP, 0.7D, 0.1D);
            this.ringBurst(sl, this.getX(), cy + 1.0D, this.getZ(), ParticleTypes.GLOW, 1.0D, 0.0D);
            this.ringBurst(sl, this.getX(), cy, this.getZ(), ParticleTypes.NAUTILUS, 0.5D, 0.15D);
            sl.sendParticles(ParticleTypes.SPLASH, this.getX(), cy, this.getZ(), 80, radius * 0.15D, 1.0D, radius * 0.15D, 0.4D);
            sl.sendParticles(ParticleTypes.SONIC_BOOM, this.getX(), cy, this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.level().playSound(null, this.getX(), cy, this.getZ(), net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE,
                    net.minecraft.sounds.SoundSource.HOSTILE, 4.0F, 0.5F);
        }
    }

    /** Animación de mordisco cuando algo golpea a alguien (mandíbula que se cierra, marcas de colmillos y sonido). */
    public void biteHit(net.minecraft.world.entity.LivingEntity victim) {
        if (this.level().isClientSide || !this.isAlive()) {
            return;
        }
        if (this.getBiteTicks() <= 0) {
            this.entityData.set(DATA_BITE, BITE_TICKS);
            // embestida real del cuerpo hacia la víctima
            net.minecraft.world.phys.Vec3 to = victim.position().add(0.0D, victim.getBbHeight() * 0.5D, 0.0D).subtract(this.position());
            if (this.isInWater() && to.lengthSqr() > 1.0E-3D) {
                this.setDeltaMovement(this.getDeltaMovement().add(to.normalize().scale(0.25D)));
            }
        }
        this.playSound(net.minecraft.sounds.SoundEvents.DOLPHIN_ATTACK, 2.0F, 0.5F);
        if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            double vy = victim.getY() + victim.getBbHeight() * 0.6D;
            sl.sendParticles(ParticleTypes.CRIT, victim.getX(), vy, victim.getZ(), 20, 0.4D, 0.4D, 0.4D, 0.3D);
            sl.sendParticles(ParticleTypes.DAMAGE_INDICATOR, victim.getX(), vy, victim.getZ(), 6, 0.3D, 0.3D, 0.3D, 0.1D);
            sl.sendParticles(ParticleTypes.END_ROD, victim.getX(), vy, victim.getZ(), 8, 0.3D, 0.3D, 0.3D, 0.05D);
        }
    }

    /** Anillo de partículas que se expande en el plano horizontal (count 0 = velocidad por partícula). */
    protected void ringBurst(net.minecraft.server.level.ServerLevel sl, double x, double y, double z,
                             net.minecraft.core.particles.ParticleOptions type, double speed, double vy) {
        int points = 48;
        for (int i = 0; i < points; i++) {
            double ang = Math.PI * 2.0D * i / points;
            sl.sendParticles(type, x, y, z, 0, Math.cos(ang), vy, Math.sin(ang), speed);
        }
    }

    /** Los demás leviatanes cercanos acuden a defender a quien pide ayuda (chillido). */
    protected void callForHelp(net.minecraft.world.entity.LivingEntity attacker) {
        if (this.alertCooldown > 0 || attacker instanceof AbstractLeviathan) {
            return;
        }
        this.alertCooldown = 100;
        this.playSound(net.minecraft.sounds.SoundEvents.DOLPHIN_HURT, 3.0F, 0.6F);
        for (AbstractLeviathan other : this.level().getEntitiesOfClass(AbstractLeviathan.class,
                this.getBoundingBox().inflate(64.0D), o -> o != this && o.getTarget() == null)) {
            other.setTarget(attacker);
        }
        if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    /** Radio de la bendición del leviatán (0 = no bendice). */
    protected double blessRadius() {
        return this.bodyScale() * 3.5D;
    }

    /**
     * Magia del lado del servidor: bendición (respiración acuática, visión nocturna y gracia del delfín) para los
     * jugadores cercanos mientras está en paz, y campanillas místicas de vez en cuando.
     */
    private void serverMagic() {
        if (this.tickCount % 40 == 0 && this.getTarget() == null && !this.isAggressive()) {
            double r = this.blessRadius();
            for (net.minecraft.world.entity.player.Player p : this.level().getEntitiesOfClass(
                    net.minecraft.world.entity.player.Player.class, this.getBoundingBox().inflate(r))) {
                if (p.isSpectator() || p.distanceToSqr(this) > r * r) {
                    continue;
                }
                net.minecraft.world.effect.MobEffectInstance cur = p.getEffect(com.rpgrealms.registry.ModEffects.VALLSARON_BLESSING.get());
                if (cur == null || cur.getDuration() < 11900) {
                    p.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            com.rpgrealms.registry.ModEffects.VALLSARON_BLESSING.get(), 12000, 0, false, false, true));
                }
            }
        }
        if (this.tickCount % 100 == 50) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, net.minecraft.sounds.SoundSource.NEUTRAL,
                    2.5F, 0.5F + this.random.nextFloat() * 0.3F);
        }
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        boolean r = super.hurt(source, amount);
        if (r && source.getEntity() instanceof net.minecraft.world.entity.LivingEntity attacker && !this.level().isClientSide) {
            this.callForHelp(attacker);
        }
        if (r && this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            double w = this.getBbWidth() * 0.4D;
            sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
                    24, w, this.getBbHeight() * 0.25D, w, 0.25D);
            sl.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
                    10, w, this.getBbHeight() * 0.25D, w, 0.05D);
        }
        return r;
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource source) {
        super.die(source);
        if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            double w = this.getBbWidth() * 0.6D;
            double cy = this.getY() + this.getBbHeight() * 0.5D;
            sl.sendParticles(ParticleTypes.END_ROD, this.getX(), cy, this.getZ(), 160, w, this.getBbHeight() * 0.4D, w, 0.12D);
            sl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), cy, this.getZ(), 80, w, this.getBbHeight() * 0.4D, w, 0.06D);
            sl.sendParticles(ParticleTypes.GLOW, this.getX(), cy, this.getZ(), 120, w, this.getBbHeight() * 0.4D, w, 0.1D);
            sl.playSound(null, this.getX(), cy, this.getZ(), net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE,
                    net.minecraft.sounds.SoundSource.NEUTRAL, 4.0F, 0.6F);
        }
    }

    /**
     * Magia visual: anillo de luz que gira alrededor del cuerpo, glifos que caen sobre él, latido que expande un
     * anillo de destellos y, en superficie, un soplo de ballena con destellos.
     */
    private void clientMagic() {
        double sc = this.bodyScale();
        Vec3 fwd = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        Vec3 side = new Vec3(-fwd.z, 0.0D, fwd.x);
        double cy = this.getY() + this.getBbHeight() * 0.5D;
        double ring = Math.max(1.5D, this.getBbWidth() * 0.45D);
        double ringAlong = this.mouthForward() * 0.1D;
        double cx = this.getX() + fwd.x * ringAlong;
        double cz = this.getZ() + fwd.z * ringAlong;

        // Tres hélices de luz con estela que giran alrededor del cuerpo
        ring = Math.max(2.0D, this.getBbWidth() * 0.6D);
        boolean angryNow = this.isAggressive();
        DustColorTransitionOptions big = new DustColorTransitionOptions(
                angryNow ? new Vector3f(1.0F, 0.3F, 0.2F) : new Vector3f(0.3F, 0.95F, 1.0F),
                angryNow ? new Vector3f(0.6F, 0.0F, 0.2F) : new Vector3f(0.7F, 0.3F, 1.0F), 2.6F);
        double a = this.tickCount * 0.14D;
        for (int k = 0; k < 3; k++) {
            for (int t = 0; t < 4; t++) {
                double ang = a - t * 0.13D + k * Math.PI * 2.0D / 3.0D;
                double drift = (this.tickCount % 40) / 40.0D * 3.0D + t * 0.12D;
                double px = cx + side.x * Math.cos(ang) * ring - fwd.x * drift;
                double pz = cz + side.z * Math.cos(ang) * ring - fwd.z * drift;
                double py = cy + Math.sin(ang) * ring * 0.8D;
                if (t == 0) {
                    this.level().addParticle(ParticleTypes.END_ROD, px, py, pz, 0.0D, 0.0D, 0.0D);
                }
                this.level().addParticle(t % 2 == 0 ? big : ParticleTypes.GLOW, px, py, pz, 0.0D, 0.0D, 0.0D);
            }
        }

        // Glifos de encantamiento que caen sobre el cuerpo
        int glyphs = 2 + (int) (sc * 0.6D);
        for (int i = 0; i < glyphs; i++) {
            double along = (this.random.nextDouble() - 0.4D) * this.mouthForward() * 1.4D;
            double gx = this.getX() + fwd.x * along + side.x * this.random.nextGaussian() * ring * 0.8D;
            double gz = this.getZ() + fwd.z * along + side.z * this.random.nextGaussian() * ring * 0.8D;
            this.level().addParticle(ParticleTypes.ENCHANT, gx, cy + ring * 0.7D, gz,
                    this.random.nextGaussian() * 0.8D, 1.2D + this.random.nextDouble() * 1.2D, this.random.nextGaussian() * 0.8D);
        }

        // Chispas de aura sueltas alrededor
        for (int i = 0; i < 2 + (int) sc / 2; i++) {
            double along = (this.random.nextDouble() - 0.5D) * this.mouthForward() * 2.0D;
            this.level().addParticle(this.random.nextInt(3) == 0 ? ParticleTypes.ELECTRIC_SPARK : ParticleTypes.GLOW,
                    this.getX() + fwd.x * along + side.x * this.random.nextGaussian() * ring,
                    cy + this.random.nextGaussian() * ring * 0.6D,
                    this.getZ() + fwd.z * along + side.z * this.random.nextGaussian() * ring,
                    this.random.nextGaussian() * 0.02D, 0.03D, this.random.nextGaussian() * 0.02D);
        }

        // Latido cada 3 s: dos anillos de luz (vertical y horizontal) que se expanden
        if (this.tickCount % 60 == 0) {
            int points = (int) Math.min(96, 36 + sc * 8);
            for (int i = 0; i < points; i++) {
                double ang = Math.PI * 2.0D * i / points;
                double dx = side.x * Math.cos(ang);
                double dz = side.z * Math.cos(ang);
                double dy = Math.sin(ang);
                this.level().addParticle(ParticleTypes.GLOW, cx, cy, cz, dx * 0.6D, dy * 0.4D, dz * 0.6D);
                this.level().addParticle(big, cx, cy, cz, dx * 0.5D, dy * 0.33D, dz * 0.5D);
                this.level().addParticle(ParticleTypes.END_ROD, cx, cy, cz, Math.cos(ang) * 0.7D, 0.0D, Math.sin(ang) * 0.7D);
            }
            this.level().playLocalSound(this.getX(), cy, this.getZ(), net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE,
                    net.minecraft.sounds.SoundSource.NEUTRAL, 1.5F, 0.6F, false);
        }

        // Escudo de runas: orbes de luz que giran alrededor del cuerpo
        int sh = this.shieldCount();
        for (int i = 0; i < sh; i++) {
            double ang = this.tickCount * 0.09D + i * Math.PI * 2.0D / sh;
            double rr = Math.max(3.0D, this.getBbWidth() * 0.75D);
            double ox = side.x * Math.cos(ang) * rr + fwd.x * Math.sin(ang) * rr * 0.6D;
            double oz = side.z * Math.cos(ang) * rr + fwd.z * Math.sin(ang) * rr * 0.6D;
            double oy = Math.sin(ang * 2.0D) * rr * 0.25D;
            this.level().addParticle(big, cx + ox, cy + oy, cz + oz, 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.END_ROD, cx + ox, cy + oy, cz + oz, 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.ENCHANT, cx + ox, cy + oy + 0.5D, cz + oz, 0.0D, 0.4D, 0.0D);
            double a2 = ang - 0.25D;
            this.level().addParticle(ParticleTypes.GLOW, cx + side.x * Math.cos(a2) * rr + fwd.x * Math.sin(a2) * rr * 0.6D,
                    cy + Math.sin(a2 * 2.0D) * rr * 0.25D, cz + side.z * Math.cos(a2) * rr + fwd.z * Math.sin(a2) * rr * 0.6D, 0.0D, 0.0D, 0.0D);
        }

        // Aletas pectorales y cola sueltan polvo místico mientras nadan (más al aletear rápido)
        double spd = this.getDeltaMovement().length();
        if (this.isInWater()) {
            int per = 2 + (int) Math.min(4.0D, spd * 14.0D);
            for (int sgn = -1; sgn <= 1; sgn += 2) {
                for (int i = 0; i < per; i++) {
                    double r = this.random.nextDouble();
                    double lat = sc * (1.1D + r * 3.8D);              // a lo largo de la aleta, de la base a la punta
                    double back = lat * 0.42D - sc * 0.25D;           // las aletas van barridas hacia atrás
                    double px = this.getX() - fwd.x * back + side.x * sgn * lat;
                    double pz = this.getZ() - fwd.z * back + side.z * sgn * lat;
                    double py = cy - sc * 0.55D - r * sc * 0.25D;
                    double vx = -this.getDeltaMovement().x * 0.3D + this.random.nextGaussian() * 0.01D;
                    double vz = -this.getDeltaMovement().z * 0.3D + this.random.nextGaussian() * 0.01D;
                    this.level().addParticle(i % 3 == 0 ? ParticleTypes.END_ROD : (i % 3 == 1 ? ParticleTypes.GLOW : big),
                            px, py, pz, vx, -0.01D, vz);
                    if (r > 0.85D) {
                        this.level().addParticle(ParticleTypes.ENCHANT, px, py + 0.4D, pz, 0.0D, 0.6D, 0.0D);
                    }
                }
            }
            // la cola deja una estela de luz en abanico
            double tailAlong = this.trailBack() * 0.95D;
            for (int sgn = -1; sgn <= 1; sgn += 2) {
                double lat = sc * (0.4D + this.random.nextDouble() * 1.4D);
                this.level().addParticle(this.random.nextBoolean() ? ParticleTypes.END_ROD : big,
                        this.getX() - fwd.x * tailAlong + side.x * sgn * lat, cy + (this.random.nextDouble() - 0.5D) * sc * 0.4D,
                        this.getZ() - fwd.z * tailAlong + side.z * sgn * lat, 0.0D, 0.0D, 0.0D);
            }
        }

        // Herido: pierde luz a chorros por las grietas (más cuanto menos vida le queda)
        float hp = this.getHealth() / this.getMaxHealth();
        if (hp < 0.6F && this.tickCount % 2 == 0) {
            int n = hp < 0.3F ? 4 : 2;
            for (int i = 0; i < n; i++) {
                double al = this.random.nextDouble() * this.mouthForward() - this.mouthForward() * 0.4D;
                this.level().addParticle(this.isAggressive() ? ParticleTypes.LAVA : ParticleTypes.GLOW,
                        this.getX() + fwd.x * al + side.x * (this.random.nextDouble() - 0.5D) * sc * 1.5D,
                        cy + sc * 0.5D,
                        this.getZ() + fwd.z * al + side.z * (this.random.nextDouble() - 0.5D) * sc * 1.5D,
                        0.0D, 0.05D, 0.0D);
            }
        }
        // Velocidad alta: ola de proa y estela de burbujas
        if (this.isInWater() && spd > 0.35D) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE_COLUMN_UP,
                        this.getX() + fwd.x * this.mouthForward() * 0.5D + side.x * (this.random.nextDouble() - 0.5D) * sc * 2.0D,
                        cy + (this.random.nextDouble() - 0.5D) * sc,
                        this.getZ() + fwd.z * this.mouthForward() * 0.5D + side.z * (this.random.nextDouble() - 0.5D) * sc * 2.0D,
                        -this.getDeltaMovement().x * 0.5D, 0.05D, -this.getDeltaMovement().z * 0.5D);
            }
            this.level().addParticle(ParticleTypes.CLOUD,
                    this.getX() - fwd.x * this.trailBack() * 0.5D, cy, this.getZ() - fwd.z * this.trailBack() * 0.5D, 0.0D, 0.0D, 0.0D);
        }
        // En el aire (salto): gotas de agua y destellos cayendo
        if (!this.isInWater() && !this.onGround() && !this.isNoGravity()) {
            this.level().addParticle(ParticleTypes.FALLING_WATER, this.getX() + (this.random.nextDouble() - 0.5D) * sc * 2.0D,
                    cy, this.getZ() + (this.random.nextDouble() - 0.5D) * sc * 2.0D, 0.0D, -0.1D, 0.0D);
            this.level().addParticle(ParticleTypes.END_ROD, this.getX() + (this.random.nextDouble() - 0.5D) * sc * 2.0D,
                    cy, this.getZ() + (this.random.nextDouble() - 0.5D) * sc * 2.0D, 0.0D, -0.05D, 0.0D);
        }

        // Soplo de ballena con destellos cuando la cabeza asoma
        double topY = this.getY() + this.getBbHeight();
        net.minecraft.core.BlockPos above = net.minecraft.core.BlockPos.containing(this.getX(), topY + 0.3D, this.getZ());
        if (this.isInWater() && !this.level().getFluidState(above).is(FluidTags.WATER)
                && this.tickCount % 2 == 0) {
            double bx = this.getX() + fwd.x * this.mouthForward() * 0.35D;
            double bz = this.getZ() + fwd.z * this.mouthForward() * 0.35D;
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(ParticleTypes.SPLASH, bx, topY, bz,
                        this.random.nextGaussian() * 0.08D, 0.5D + this.random.nextDouble() * 0.3D, this.random.nextGaussian() * 0.08D);
            }
            this.level().addParticle(ParticleTypes.CLOUD, bx, topY + 0.5D, bz, 0.0D, 0.25D, 0.0D);
            this.level().addParticle(ParticleTypes.END_ROD, bx, topY + 1.0D, bz,
                    this.random.nextGaussian() * 0.05D, 0.2D, this.random.nextGaussian() * 0.05D);
        }
    }

    /** Escala del modelo (para colocar las puntas de las aletas). */
    public abstract double bodyScale();

    /**
     * Rastro místico, tranquilo como el de una ballena: motas cian que se funden a violeta a lo largo del cuerpo,
     * destellos suaves que suben, glifos que flotan y, al nadar, cintas de luz que salen de las aletas
     * pectorales y de las puntas de la cola. Rojas solo si está furioso.
     */
    private void mysticTrail() {
        Vec3 vel = this.getDeltaMovement();
        double speed = vel.length();
        boolean angry = this.isAggressive();
        double amount = (0.35D + Math.min(speed * 5.0D, 2.0D) + (angry ? 0.5D : 0.0D)) * this.trailDensity();
        int n = (int) amount;
        if (this.random.nextDouble() < amount - n) {
            n++;
        }
        Vec3 fwd = Vec3.directionFromRotation(0.0F, this.yBodyRot);
        Vec3 side = new Vec3(-fwd.z, 0.0D, fwd.x);
        double front = this.mouthForward();
        double back = this.trailBack();
        double halfWidth = Math.max(1.0D, front * 0.25D);
        double cy = this.getY() + this.getBbHeight() * 0.5D;
        Vector3f c1 = angry ? new Vector3f(1.0F, 0.35F, 0.2F) : new Vector3f(0.35F, 0.95F, 1.0F);
        Vector3f c2 = angry ? new Vector3f(0.5F, 0.0F, 0.1F) : new Vector3f(0.45F, 0.25F, 0.95F);
        DustColorTransitionOptions mote = new DustColorTransitionOptions(c1, c2, 1.5F);

        for (int i = 0; i < n; i++) {
            double along = front * 0.7D - this.random.nextDouble() * (front * 0.7D + back);
            double x = this.getX() + fwd.x * along + side.x * (this.random.nextGaussian() * halfWidth);
            double z = this.getZ() + fwd.z * along + side.z * (this.random.nextGaussian() * halfWidth);
            double y = cy + this.random.nextGaussian() * halfWidth * 0.6D;
            double dx = -vel.x * 0.2D + this.random.nextGaussian() * 0.008D;
            double dz = -vel.z * 0.2D + this.random.nextGaussian() * 0.008D;
            switch (this.random.nextInt(5)) {
                case 0, 1 -> this.level().addParticle(mote, x, y, z, dx, 0.015D, dz);
                case 2 -> this.level().addParticle(ParticleTypes.GLOW, x, y, z, dx, 0.02D, dz);
                case 3 -> this.level().addParticle(ParticleTypes.END_ROD, x, y, z, dx * 0.4D, 0.012D, dz * 0.4D);
                default -> this.level().addParticle(ParticleTypes.NAUTILUS, x, y + 0.6D, z,
                        this.random.nextGaussian() * 0.25D, 0.2D + this.random.nextDouble() * 0.2D,
                        this.random.nextGaussian() * 0.25D);
            }
        }

        // Cintas de luz desde las puntas de las aletas mientras nada.
        if (speed > 0.06D) {
            double sc = this.bodyScale();
            double pecAlong = front * 0.1D - this.random.nextDouble() * front * 0.3D;
            double tailAlong = -back;
            for (int sgn = -1; sgn <= 1; sgn += 2) {
                double px = this.getX() + fwd.x * pecAlong + side.x * sgn * sc * 2.3D;
                double pz = this.getZ() + fwd.z * pecAlong + side.z * sgn * sc * 2.3D;
                this.level().addParticle(mote, px, cy - sc * 0.3D, pz, -vel.x * 0.1D, 0.0D, -vel.z * 0.1D);
                if (this.random.nextInt(2) == 0) {
                    double tx = this.getX() + fwd.x * tailAlong + side.x * sgn * sc * 1.9D;
                    double tz = this.getZ() + fwd.z * tailAlong + side.z * sgn * sc * 1.9D;
                    this.level().addParticle(mote, tx, cy, tz, -vel.x * 0.1D, 0.0D, -vel.z * 0.1D);
                }
            }
            this.level().addParticle(ParticleTypes.GLOW, this.getX() + fwd.x * tailAlong, cy,
                    this.getZ() + fwd.z * tailAlong, 0.0D, 0.02D, 0.0D);
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        double h = this.cullingInflate();
        return this.getBoundingBox().inflate(h, h * 0.3D, h);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    /** Aparece solo en agua profunda de los océanos. */
    public static boolean checkWaterSpawn(EntityType<? extends AbstractLeviathan> type, ServerLevelAccessor level,
                                          MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean water = level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above()).is(FluidTags.WATER);
        if (!water) {
            return false;
        }
        return pos.getY() <= level.getSeaLevel() - 10;
    }
}

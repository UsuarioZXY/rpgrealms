package com.bluelock.mod.entity;

import com.bluelock.mod.block.GoalNetBlock;
import com.bluelock.mod.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Balon de futbol.
 * - Se pega al jugador cercano y lo sigue (dribleo automatico). Agacharse (shift) lo suelta.
 * - Se patea con click izquierdo sobre el balon o con la tecla de patada (R por defecto).
 * - Si entra en una red de porteria cuenta gol.
 */
public class SoccerBallEntity extends Entity implements ItemSupplier {
    private static final double GRAVITY = 0.04;
    private static final double AIR_DRAG = 0.99;
    private static final double GROUND_FRICTION = 0.94;
    private static final double BOUNCE_FLOOR = 0.6;
    private static final double BOUNCE_WALL = 0.7;
    private static final double CLAIM_RANGE = 1.4;
    private static final double RELEASE_RANGE_SQR = 36.0;

    /** Rotacion acumulada del balon al rodar (solo se usa en el cliente para dibujarlo). */
    public final Quaternionf rollRot = new Quaternionf();

    private int kickCooldown = 0;
    private int goalCooldown = 0;
    private int resetTimer = 0;
    private boolean lastOwnerGround = false;
    private double lastOwnerY = 0;
    @Nullable
    private Vec3 home;
    @Nullable
    private UUID ownerId;
    @Nullable
    private UUID lastToucherId;

    public SoccerBallEntity(EntityType<? extends SoccerBallEntity> type, Level level) {
        super(type, level);
    }

    // ---------------------------------------------------------------- tick

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            updateRoll();
            return;
        }
        if (getY() < level().getMinBuildHeight() - 16) {
            discard();
            return;
        }
        if (kickCooldown > 0) kickCooldown--;
        if (goalCooldown > 0) goalCooldown--;
        if (resetTimer > 0 && --resetTimer == 0) {
            resetToHome();
        }

        updateOwnerAndDribble();
        applyPhysics();
    }

    private void updateOwnerAndDribble() {
        Player owner = ownerId != null ? level().getPlayerByUUID(ownerId) : null;
        if (owner != null && (!owner.isAlive() || owner.isSpectator() || owner.isShiftKeyDown()
                || distanceToSqr(owner) > RELEASE_RANGE_SQR)) {
            owner = null;
            ownerId = null;
        }

        if (owner == null && kickCooldown == 0) {
            Player near = level().getNearestPlayer(this, CLAIM_RANGE);
            double dy = getY() - (near != null ? near.getY() : 0);
            if (near != null && !near.isShiftKeyDown() && dy > -0.6 && dy < 0.7) {
                owner = near;
                ownerId = near.getUUID();
                lastToucherId = ownerId;
            }
        }

        // Robo: otro jugador mas cerca del balon se lo quita al que lo lleva.
        if (owner != null && kickCooldown == 0) {
            Player rival = level().getNearestPlayer(this, 0.9);
            if (rival != null && rival != owner && !rival.isShiftKeyDown()
                    && rival.distanceToSqr(this) + 0.04 < owner.distanceToSqr(this)) {
                owner = rival;
                ownerId = rival.getUUID();
                lastToucherId = ownerId;
            }
        }

        // Saltar con el balon: lo levantas suavemente hacia adelante.
        boolean ground = owner != null && owner.onGround();
        boolean jumped = owner != null && lastOwnerGround && !ground && owner.getY() > lastOwnerY + 0.05;
        lastOwnerGround = ground;
        lastOwnerY = owner != null ? owner.getY() : 0;

        if (owner != null && kickCooldown == 0) {
            if (jumped) {
                lob(owner);
            } else {
                dribble(owner);
            }
        }
    }

    /** Levantada suave con el pie: sube y avanza un poco, sin ser un tiro. */
    private void lob(Player owner) {
        Vec3 look = owner.getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z);
        if (dir.lengthSqr() < 1.0E-4) {
            dir = Vec3.directionFromRotation(0, owner.getYRot());
        }
        dir = dir.normalize();
        setDeltaMovement(dir.x * 0.2, 0.42, dir.z * 0.2);
        ownerId = null;
        lastToucherId = owner.getUUID();
        kickCooldown = 14;
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_WEAK,
                SoundSource.PLAYERS, 0.6F, 1.5F);
    }

    /** Mueve el balon hacia un punto delante del jugador, en la direccion en la que mira. */
    private void dribble(Player owner) {
        Vec3 look = owner.getLookAngle();
        Vec3 dir = new Vec3(look.x, 0, look.z);
        if (dir.lengthSqr() < 1.0E-4) {
            return;
        }
        dir = dir.normalize();
        double distance = owner.isSprinting() ? 1.25 : 0.9;
        double targetX = owner.getX() + dir.x * distance;
        double targetZ = owner.getZ() + dir.z * distance;

        Vec3 push = new Vec3((targetX - getX()) * 0.6, 0, (targetZ - getZ()) * 0.6);
        if (push.length() > 0.8) {
            push = push.normalize().scale(0.8);
        }
        Vec3 v = getDeltaMovement();
        setDeltaMovement(push.x, v.y, push.z);
    }

    private void applyPhysics() {
        Vec3 v = getDeltaMovement().add(0, -GRAVITY, 0);
        if (isInWater()) {
            v = new Vec3(v.x * 0.85, v.y * 0.85 + 0.07, v.z * 0.85);
        }

        Vec3 before = position();
        move(MoverType.SELF, v);
        Vec3 moved = position().subtract(before);

        double vx = v.x;
        double vy = v.y;
        double vz = v.z;
        boolean grounded = false;

        if (Math.abs(moved.x - v.x) > 1.0E-4) vx = -v.x * BOUNCE_WALL;
        if (Math.abs(moved.z - v.z) > 1.0E-4) vz = -v.z * BOUNCE_WALL;
        if (Math.abs(moved.y - v.y) > 1.0E-4) {
            if (v.y < 0) {
                grounded = true;
                if (Math.abs(v.y) > 0.12) {
                    vy = -v.y * BOUNCE_FLOOR;
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.SLIME_BLOCK_HIT,
                            SoundSource.NEUTRAL, 0.4F, 1.4F);
                } else {
                    vy = 0;
                }
            } else {
                vy = -v.y * 0.3;
            }
        }

        double friction = grounded ? GROUND_FRICTION : AIR_DRAG;
        vx *= friction;
        vz *= friction;
        vy *= 0.99;
        if (vx * vx + vz * vz < 9.0E-6) {
            vx = 0;
            vz = 0;
        }

        if (isInNet()) {
            vx *= 0.2;
            vy *= 0.2;
            vz *= 0.2;
            scoreGoal();
        }

        setDeltaMovement(vx, vy, vz);
    }

    private boolean isInNet() {
        BlockPos pos = BlockPos.containing(getX(), getY() + 0.2, getZ());
        return level().getBlockState(pos).getBlock() instanceof GoalNetBlock;
    }

    private void scoreGoal() {
        if (goalCooldown > 0 || !(level() instanceof ServerLevel server)) {
            return;
        }
        goalCooldown = 80;
        if (home != null) {
            resetTimer = 70;
        }

        Player scorer = lastToucherId != null ? server.getPlayerByUUID(lastToucherId) : null;
        Component title = Component.translatable("message.bluelock.goal");
        Component chat = scorer != null
                ? Component.translatable("message.bluelock.goal_by", scorer.getDisplayName())
                : title;

        for (ServerPlayer p : server.players()) {
            if (p.distanceToSqr(this) < 64 * 64) {
                p.connection.send(new ClientboundSetTitlesAnimationPacket(5, 40, 10));
                p.connection.send(new ClientboundSetTitleTextPacket(title));
                p.sendSystemMessage(chat);
            }
        }
        server.sendParticles(ParticleTypes.FIREWORK, getX(), getY() + 0.5, getZ(), 40, 0.6, 0.6, 0.6, 0.15);
        server.playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.5F, 1.0F);
        server.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    /** Punto donde el balon reaparece despues de un gol. */
    public void setHome(Vec3 pos) {
        this.home = pos;
    }

    private void resetToHome() {
        if (home == null) {
            return;
        }
        if (level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.POOF, getX(), getY() + 0.3, getZ(), 12, 0.2, 0.2, 0.2, 0.02);
        }
        setDeltaMovement(Vec3.ZERO);
        ownerId = null;
        kickCooldown = 0;
        setPos(home.x, home.y, home.z);
        if (level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.POOF, getX(), getY() + 0.3, getZ(), 12, 0.2, 0.2, 0.2, 0.02);
        }
    }

    /** Cliente: gira el balon segun lo que avanza, como si rodara sobre el suelo. */
    private void updateRoll() {
        double dx = getX() - xo;
        double dz = getZ() - zo;
        double d = Math.sqrt(dx * dx + dz * dz);
        if (d < 1.0E-4 || d > 2.0) {
            return;
        }
        float angle = (float) (d / 0.25);
        float ax = (float) (dz / d);
        float az = (float) (-dx / d);
        new Quaternionf().rotateAxis(angle, ax, 0.0F, az).mul(rollRot, rollRot);
        rollRot.normalize();
    }

    // ---------------------------------------------------------------- patada

    /** Patea el balon en la direccion en la que mira el jugador. */
    public void kick(Player player, double power) {
        Vec3 look = player.getLookAngle();
        Vec3 h = new Vec3(look.x, 0, look.z);
        if (h.lengthSqr() < 1.0E-4) {
            h = Vec3.directionFromRotation(0, player.getYRot());
        }
        h = h.normalize();
        double lift = 0.12 + Math.max(0, look.y) * 0.9;
        setDeltaMovement(h.x * power, power * lift, h.z * power);

        ownerId = null;
        lastToucherId = player.getUUID();
        kickCooldown = 12;
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS, 0.8F, 1.3F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide || isRemoved()) {
            return false;
        }
        if (source.getEntity() instanceof Player player) {
            kick(player, player.isSprinting() ? 1.4 : 0.9);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if (!level().isClientSide) {
                if (!player.getAbilities().instabuild) {
                    ItemStack stack = new ItemStack(ModItems.SOCCER_BALL.get());
                    if (!player.addItem(stack)) {
                        spawnAtLocation(stack);
                    }
                }
                discard();
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    // ---------------------------------------------------------------- misc

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 64.0 * 64.0;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // sin sonido de pasos
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.SOCCER_BALL.get());
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.getBoolean("HasHome")) {
            home = new Vec3(tag.getDouble("HomeX"), tag.getDouble("HomeY"), tag.getDouble("HomeZ"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (home != null) {
            tag.putBoolean("HasHome", true);
            tag.putDouble("HomeX", home.x);
            tag.putDouble("HomeY", home.y);
            tag.putDouble("HomeZ", home.z);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

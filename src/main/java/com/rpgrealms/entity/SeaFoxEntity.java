package com.rpgrealms.entity;

import com.rpgrealms.registry.ModEntities;
import com.rpgrealms.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

/**
 * Zorro marino: mamífero pequeño y esquivo de las islas, con cola de pez. Le gusta meterse al agua,
 * deja huellas luminosas y se domestica con Pez Rúnico.
 */
public class SeaFoxEntity extends TamableAnimal {
    public SeaFoxEntity(EntityType<? extends SeaFoxEntity> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0D, 1.5D,
                e -> !this.isTame() && e instanceof Player p && !this.isFood(p.getMainHandItem()) && !this.isFood(p.getOffhandItem())));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.1D, Ingredient.of(ModItems.RUNE_LANTERN_FISH.get()), false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.1D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new SwimForFunGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.RUNE_LANTERN_FISH.get());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        SeaFoxEntity baby = ModEntities.SEA_FOX.get().create(level);
        if (baby != null && this.isTame() && this.getOwnerUUID() != null) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }
        return baby;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean ok = this.isTame() ? this.isOwnedBy(player) || this.isFood(stack) : this.isFood(stack);
            return ok ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(4.0F);
                return InteractionResult.SUCCESS;
            }
            InteractionResult r = super.mobInteract(player, hand);
            if (!r.consumesAction() && this.isOwnedBy(player)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }
            return r;
        } else if (this.isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            double sp = this.getDeltaMovement().horizontalDistanceSqr();
            // huellas luminosas al caminar y estela de luz al nadar
            if (this.isInWater()) {
                if (this.tickCount % 4 == 0) {
                    this.level().addParticle(ParticleTypes.GLOW, this.getX(), this.getY() + 0.2D, this.getZ(), 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getY() + 0.3D, this.getZ(), 0.0D, 0.04D, 0.0D);
                }
            } else if (this.onGround() && sp > 0.0004D && this.tickCount % 5 == 0) {
                this.level().addParticle(ParticleTypes.GLOW, this.getX() + (this.random.nextDouble() - 0.5D) * 0.3D,
                        this.getY() + 0.05D, this.getZ() + (this.random.nextDouble() - 0.5D) * 0.3D, 0.0D, 0.0D, 0.0D);
            }
            if (this.random.nextInt(50) == 0) {
                this.level().addParticle(ParticleTypes.END_ROD, this.getX(), this.getY() + 0.8D, this.getZ(), 0.0D, 0.01D, 0.0D);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? SoundEvents.FOX_AMBIENT : null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 1.25F;
    }

    public static boolean checkSpawn(EntityType<? extends SeaFoxEntity> type, ServerLevelAccessor level,
                                     MobSpawnType reason, BlockPos pos, RandomSource random) {
        return (level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) || level.getBlockState(pos.below()).is(BlockTags.SAND))
                && level.getRawBrightness(pos, 0) > 8;
    }

    /** De vez en cuando se acerca al agua más cercana y se mete a nadar. */
    static class SwimForFunGoal extends Goal {
        private final SeaFoxEntity mob;
        private BlockPos target;

        SwimForFunGoal(SeaFoxEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isInWater() || this.mob.isInSittingPose() || this.mob.getRandom().nextInt(250) != 0) {
                return false;
            }
            BlockPos o = this.mob.blockPosition();
            double best = Double.MAX_VALUE;
            this.target = null;
            for (int dx = -10; dx <= 10; dx++) {
                for (int dz = -10; dz <= 10; dz++) {
                    for (int dy = -2; dy <= 0; dy++) {
                        BlockPos p = o.offset(dx, dy, dz);
                        if (this.mob.level().getFluidState(p).is(FluidTags.WATER) && this.mob.level().getBlockState(p.above()).isAir()) {
                            double d = p.distSqr(o);
                            if (d < best && d > 4.0D) {
                                best = d;
                                this.target = p;
                            }
                        }
                    }
                }
            }
            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && !this.mob.isInWater() && this.mob.getNavigation().isInProgress();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D, 1.1D);
        }
    }
}

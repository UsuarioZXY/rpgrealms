package com.rpgrealms.entity;

import com.rpgrealms.registry.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Base común de la Cría y del Leviatán adulto: nado suave, burbujas por el hocico,
 * posición de la boca y área de culling ampliada (el modelo es mucho más largo que la hitbox).
 */
public abstract class AbstractLeviathan extends WaterAnimal {

    protected AbstractLeviathan(EntityType<? extends WaterAnimal> type, Level level, int maxTurnY) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, maxTurnY, 0.15F, 0.5F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    /** Distancia (en bloques) desde el centro de la entidad hasta la boca. */
    protected abstract double mouthForward();

    /** Altura (en bloques) de la boca sobre los pies de la entidad. */
    protected abstract double mouthUp();

    /** Cuánto se amplía el AABB de culling en horizontal. */
    protected abstract double cullingInflate();

    /** Fase de ataque sincronizada con el cliente (0 = reposo). El adulto la sobrescribe. */
    public int getPhase() {
        return 0;
    }

    public Vec3 getMouthPos() {
        Vec3 dir = Vec3.directionFromRotation(this.getXRot() * 0.7F, this.yBodyRot);
        return this.position().add(dir.scale(this.mouthForward())).add(0.0D, this.mouthUp(), 0.0D);
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
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void aiStep() {
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
        super.aiStep();
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

    /** Aparece solo en agua profunda: océanos profundos en el Overworld, o cualquier lago del Abismo. */
    public static boolean checkWaterSpawn(EntityType<? extends AbstractLeviathan> type, ServerLevelAccessor level,
                                          MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean water = level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above()).is(FluidTags.WATER);
        if (!water) {
            return false;
        }
        boolean inAbyss = level.getLevel().dimension() == ModDimensions.ABYSS;
        return inAbyss || pos.getY() <= level.getSeaLevel() - 10;
    }
}

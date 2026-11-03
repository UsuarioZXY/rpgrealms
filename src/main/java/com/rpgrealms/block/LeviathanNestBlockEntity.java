package com.rpgrealms.block;

import com.rpgrealms.entity.LeviathanEggEntity;
import com.rpgrealms.registry.ModBlockEntities;
import com.rpgrealms.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * El nido produce un huevo de Vallsaron cada 8 minutos (si no hay ya 2 huevos cerca y hay sitio libre).
 */
public class LeviathanNestBlockEntity extends BlockEntity {
    /** Ticks entre huevos (9600 = 8 minutos). */
    public static final int INTERVAL = 9600;
    /** Máximo de huevos simultáneos en un radio de 12 bloques. */
    public static final int MAX_EGGS = 2;

    private int timer = INTERVAL / 2;

    public LeviathanNestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NEST.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LeviathanNestBlockEntity nest) {
        if (!(level instanceof ServerLevel sl)) {
            return;
        }
        nest.timer--;
        // Aviso: el nido late y suelta destellos antes de poner el huevo
        if (nest.timer < 100 && nest.timer % 10 == 0) {
            sl.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D, 6, 0.3D, 0.2D, 0.3D, 0.04D);
        }
        if (nest.timer > 0) {
            return;
        }
        nest.timer = 200;   // si no se puede, reintenta en 10 s
        AABB area = new AABB(pos).inflate(12.0D);
        if (level.getEntitiesOfClass(LeviathanEggEntity.class, area).size() >= MAX_EGGS) {
            return;
        }
        LeviathanEggEntity egg = ModEntities.LEVIATHAN_EGG.get().create(sl);
        if (egg == null) {
            return;
        }
        egg.moveTo(pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        if (!level.noCollision(egg)) {
            egg.moveTo(pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, egg.getYRot(), 0.0F);
            if (!level.noCollision(egg)) {
                return;
            }
        }
        level.addFreshEntity(egg);
        nest.timer = INTERVAL + level.random.nextInt(1200);
        sl.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 2.0F, 0.6F);
        sl.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 40, 0.5D, 0.5D, 0.5D, 0.08D);
        sl.sendParticles(ParticleTypes.GLOW, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 30, 0.6D, 0.5D, 0.6D, 0.05D);
        nest.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Timer", this.timer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.timer = tag.contains("Timer") ? tag.getInt("Timer") : INTERVAL / 2;
    }
}

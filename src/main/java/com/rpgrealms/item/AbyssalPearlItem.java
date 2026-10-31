package com.rpgrealms.item;

import com.rpgrealms.registry.ModDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Perla Abisal: al usarla viajas al Abismo (cuevas gigantes y oscuras). Usada dentro, te devuelve
 * al punto desde el que entraste.
 */
public class AbyssalPearlItem extends Item {
    private static final String TAG_X = "rpgrealms_origin_x";
    private static final String TAG_Y = "rpgrealms_origin_y";
    private static final String TAG_Z = "rpgrealms_origin_z";
    private static final String TAG_DIM = "rpgrealms_origin_dim";

    public AbyssalPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide || !(player instanceof ServerPlayer sp)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        MinecraftServer server = sp.getServer();
        if (server == null) {
            return InteractionResultHolder.fail(stack);
        }

        boolean inAbyss = level.dimension() == ModDimensions.ABYSS;
        CompoundTag data = sp.getPersistentData();
        ServerLevel dest;
        Vec3 target;

        if (!inAbyss) {
            dest = server.getLevel(ModDimensions.ABYSS);
            if (dest == null) {
                sp.displayClientMessage(Component.translatable("message.rpgrealms.abyss_missing"), true);
                return InteractionResultHolder.fail(stack);
            }
            // Recordamos de dónde venimos.
            data.putDouble(TAG_X, sp.getX());
            data.putDouble(TAG_Y, sp.getY());
            data.putDouble(TAG_Z, sp.getZ());
            data.putString(TAG_DIM, level.dimension().location().toString());
            BlockPos safe = findSafeSpot(dest, sp.blockPosition());
            target = Vec3.atBottomCenterOf(safe);
        } else {
            ResourceKey<Level> backKey = Level.OVERWORLD;
            if (data.contains(TAG_DIM)) {
                ResourceLocation id = ResourceLocation.tryParse(data.getString(TAG_DIM));
                if (id != null) {
                    backKey = ResourceKey.create(Registries.DIMENSION, id);
                }
            }
            dest = server.getLevel(backKey);
            if (dest == null) {
                dest = server.overworld();
            }
            if (data.contains(TAG_X) && dest.dimension() == backKey) {
                target = new Vec3(data.getDouble(TAG_X), data.getDouble(TAG_Y), data.getDouble(TAG_Z));
            } else {
                BlockPos spawn = dest.getSharedSpawnPos();
                target = Vec3.atBottomCenterOf(spawn);
            }
        }

        level.playSound(null, sp.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.6F);
        sp.getCooldowns().addCooldown(this, 100);
        sp.teleportTo(dest, target.x, target.y, target.z, sp.getYRot(), sp.getXRot());
        dest.playSound(null, BlockPos.containing(target), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.6F);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.rpgrealms.abyssal_pearl.desc").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("item.rpgrealms.abyssal_pearl.desc2").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    // ------------------------------------------------------------------ sitio de llegada

    /** Busca suelo firme cerca de (x, z); si no hay, construye una plataforma. */
    private static BlockPos findSafeSpot(ServerLevel level, BlockPos origin) {
        int minY = level.getMinBuildHeight() + 4;
        int maxY = level.getMaxBuildHeight() - 6;
        int seaLevel = level.getSeaLevel();

        for (int radius = 0; radius <= 24; radius += 3) {
            for (int dx = -radius; dx <= radius; dx += 3) {
                for (int dz = -radius; dz <= radius; dz += 3) {
                    if (radius != 0 && Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue; // solo el borde del anillo
                    }
                    int x = origin.getX() + dx;
                    int z = origin.getZ() + dz;
                    level.getChunk(x >> 4, z >> 4); // fuerza la generación del chunk
                    for (int y = seaLevel + 2; y < maxY; y++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (isStandable(level, pos)) {
                            return pos;
                        }
                    }
                }
            }
        }

        // Plan B: plataforma de piedra con espacio libre encima.
        int py = Math.max(minY, seaLevel + 8);
        BlockPos base = new BlockPos(origin.getX(), py, origin.getZ());
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                level.setBlockAndUpdate(base.offset(dx, 0, dz), Blocks.STONE.defaultBlockState());
                for (int dy = 1; dy <= 4; dy++) {
                    level.setBlockAndUpdate(base.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                }
            }
        }
        return base.above();
    }

    private static boolean isStandable(ServerLevel level, BlockPos feet) {
        BlockPos below = feet.below();
        BlockState floor = level.getBlockState(below);
        BlockState body = level.getBlockState(feet);
        BlockState head = level.getBlockState(feet.above());
        return floor.isFaceSturdy(level, below, Direction.UP)
                && floor.getFluidState().isEmpty()
                && !floor.is(Blocks.MAGMA_BLOCK)
                && !floor.is(Blocks.BEDROCK)
                && body.getCollisionShape(level, feet).isEmpty() && body.getFluidState().isEmpty()
                && head.getCollisionShape(level, feet.above()).isEmpty() && head.getFluidState().isEmpty();
    }
}

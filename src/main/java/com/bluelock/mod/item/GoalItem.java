package com.bluelock.mod.item;

import com.bluelock.mod.block.GoalNetBlock;
import com.bluelock.mod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Click derecho sobre el suelo: construye una porteria completa mirando hacia el jugador.
 *
 * Medidas: boca de 5 de ancho x 3 de alto, 3 de fondo (postes delanteros en profundidad 0,
 * traseros en profundidad 2). Lleva travesano delantero y trasero, barras laterales superiores,
 * red del fondo, del techo y de los lados. El centro de la linea de gol es el bloque donde se hace click.
 */
public class GoalItem extends Item {
    public GoalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos base = ctx.getClickedPos().relative(ctx.getClickedFace());
        Direction forward = player.getDirection();
        Direction side = forward.getClockWise();

        BlockState post = ModBlocks.GOAL_POST.get().defaultBlockState();
        BlockState postUp = post.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState barSide = post.setValue(RotatedPillarBlock.AXIS, side.getAxis());
        BlockState barDepth = post.setValue(RotatedPillarBlock.AXIS, forward.getAxis());

        BlockState net = ModBlocks.GOAL_NET.get().defaultBlockState();
        BlockState netBack = net.setValue(GoalNetBlock.AXIS, forward.getAxis());
        BlockState netSide = net.setValue(GoalNetBlock.AXIS, side.getAxis());
        BlockState netRoof = net.setValue(GoalNetBlock.AXIS, Direction.Axis.Y);

        // Postes verticales: delanteros (profundidad 0) y traseros (profundidad 2).
        for (int y = 0; y <= 2; y++) {
            for (int depth : new int[]{0, 2}) {
                put(level, at(base, forward, side, depth, -3, y), postUp);
                put(level, at(base, forward, side, depth, 3, y), postUp);
            }
        }
        // Travesanos delantero y trasero.
        for (int x = -3; x <= 3; x++) {
            put(level, at(base, forward, side, 0, x, 3), barSide);
            put(level, at(base, forward, side, 2, x, 3), barSide);
        }
        // Barras superiores que unen el frente con el fondo.
        put(level, at(base, forward, side, 1, -3, 3), barDepth);
        put(level, at(base, forward, side, 1, 3, 3), barDepth);

        // Red del fondo.
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                put(level, at(base, forward, side, 2, x, y), netBack);
            }
        }
        // Red del techo.
        for (int x = -2; x <= 2; x++) {
            put(level, at(base, forward, side, 1, x, 3), netRoof);
        }
        // Redes laterales.
        for (int y = 0; y <= 2; y++) {
            put(level, at(base, forward, side, 1, -3, y), netSide);
            put(level, at(base, forward, side, 1, 3, y), netSide);
        }

        if (!player.getAbilities().instabuild) {
            ctx.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    private static BlockPos at(BlockPos base, Direction forward, Direction side, int depth, int lateral, int y) {
        return base.relative(forward, depth).relative(side, lateral).above(y);
    }

    private static void put(Level level, BlockPos pos, BlockState state) {
        BlockState existing = level.getBlockState(pos);
        if (existing.isAir() || existing.canBeReplaced()) {
            level.setBlock(pos, state, 3);
        }
    }
}

package com.bluelock.mod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Poste blanco de la porteria. Se puede colocar vertical u horizontal (travesano). */
public class GoalPostBlock extends RotatedPillarBlock {
    private static final VoxelShape SHAPE_X = Block.box(0, 5, 5, 16, 11, 11);
    private static final VoxelShape SHAPE_Y = Block.box(5, 0, 5, 11, 16, 11);
    private static final VoxelShape SHAPE_Z = Block.box(5, 5, 0, 11, 11, 16);

    public GoalPostBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.SNOW)
                .strength(2.0F, 6.0F)
                .sound(SoundType.METAL)
                .noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(AXIS)) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            case Z -> SHAPE_Z;
        };
    }
}

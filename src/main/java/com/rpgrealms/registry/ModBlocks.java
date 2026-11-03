package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.block.LeviathanNestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RpgRealms.MODID);

    public static final RegistryObject<Block> LEVIATHAN_NEST = BLOCKS.register("leviathan_nest",
            () -> new LeviathanNestBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.0F)
                    .sound(SoundType.CORAL_BLOCK)
                    .lightLevel(state -> 7)
                    .noOcclusion()));
}

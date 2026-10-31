package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.block.LumenCrystalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RpgRealms.MODID);

    /** Roca luminosa que aflora en las paredes y el suelo del Abismo. */
    public static final RegistryObject<Block> LUMEN_STONE = BLOCKS.register("lumen_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(2.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)
                    .lightLevel(state -> 9)));

    /** Cristal luminoso decorativo. */
    public static final RegistryObject<Block> LUMEN_CRYSTAL = BLOCKS.register("lumen_crystal",
            () -> new LumenCrystalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noCollission()
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .pushReaction(PushReaction.DESTROY)
                    .lightLevel(state -> 13)));
}

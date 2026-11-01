package com.bluelock.mod.registry;

import com.bluelock.mod.BlueLockMod;
import com.bluelock.mod.block.GoalNetBlock;
import com.bluelock.mod.block.GoalPostBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BlueLockMod.MODID);

    public static final RegistryObject<Block> GOAL_POST =
            BLOCKS.register("goal_post", GoalPostBlock::new);

    public static final RegistryObject<Block> GOAL_NET =
            BLOCKS.register("goal_net", GoalNetBlock::new);
}

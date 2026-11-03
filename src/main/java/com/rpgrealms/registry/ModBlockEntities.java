package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.block.LeviathanNestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RpgRealms.MODID);

    public static final RegistryObject<BlockEntityType<LeviathanNestBlockEntity>> NEST = TYPES.register("leviathan_nest",
            () -> BlockEntityType.Builder.of(LeviathanNestBlockEntity::new, ModBlocks.LEVIATHAN_NEST.get()).build(null));
}

package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.item.AbyssalPearlItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RpgRealms.MODID);

    // Huevos generadores
    public static final RegistryObject<Item> LEVIATHAN_CUB_SPAWN_EGG = ITEMS.register("leviathan_cub_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LEVIATHAN_CUB, 0x1F3A5F, 0x9FC5E8, new Item.Properties()));

    public static final RegistryObject<Item> LEVIATHAN_SPAWN_EGG = ITEMS.register("leviathan_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LEVIATHAN, 0x050B1A, 0x4DE8FF, new Item.Properties()));

    // Llave de la dimensión
    public static final RegistryObject<Item> ABYSSAL_PEARL = ITEMS.register("abyssal_pearl",
            () -> new AbyssalPearlItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    // Munición del adulto (no se obtiene; solo sirve de modelo del proyectil)
    public static final RegistryObject<Item> ABYSS_ORB = ITEMS.register("abyss_orb",
            () -> new Item(new Item.Properties()));

    // Bloques
    public static final RegistryObject<Item> LUMEN_STONE = ITEMS.register("lumen_stone",
            () -> new BlockItem(ModBlocks.LUMEN_STONE.get(), new Item.Properties()));

    public static final RegistryObject<Item> LUMEN_CRYSTAL = ITEMS.register("lumen_crystal",
            () -> new BlockItem(ModBlocks.LUMEN_CRYSTAL.get(), new Item.Properties()));
}

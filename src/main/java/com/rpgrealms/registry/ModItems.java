package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.item.LeviathanEggItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RpgRealms.MODID);

    // Huevos generadores
    public static final RegistryObject<Item> LEVIATHAN_CUB_SPAWN_EGG = ITEMS.register("leviathan_cub_spawn_egg",
            () -> new com.rpgrealms.item.ClassSpawnEggItem(ModEntities.LEVIATHAN_CUB, 0x1F3A5F, 0x9FC5E8, new Item.Properties()));

    public static final RegistryObject<Item> LEVIATHAN_TEEN_SPAWN_EGG = ITEMS.register("leviathan_teen_spawn_egg",
            () -> new com.rpgrealms.item.ClassSpawnEggItem(ModEntities.LEVIATHAN_TEEN, 0x0A1630, 0x7FD0FF, new Item.Properties()));

    public static final RegistryObject<Item> LEVIATHAN_SPAWN_EGG = ITEMS.register("leviathan_spawn_egg",
            () -> new com.rpgrealms.item.ClassSpawnEggItem(ModEntities.LEVIATHAN, 0x050B1A, 0x4DE8FF, new Item.Properties()));

    public static final RegistryObject<Item> LEVIATHAN_EGG = ITEMS.register("leviathan_egg",
            () -> new LeviathanEggItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> LEVIATHAN_NEST = ITEMS.register("leviathan_nest",
            () -> new net.minecraft.world.item.BlockItem(ModBlocks.LEVIATHAN_NEST.get(), new Item.Properties()));

    // Botín del Vallsaron
    public static final RegistryObject<Item> VALLSARON_SCALE = ITEMS.register("vallsaron_scale",
            () -> new com.rpgrealms.item.VallsaronLootItem(new Item.Properties().rarity(net.minecraft.world.item.Rarity.UNCOMMON),
                    "item.rpgrealms.vallsaron_scale.lore", false));
    public static final RegistryObject<Item> VALLSARON_FIN = ITEMS.register("vallsaron_fin",
            () -> new com.rpgrealms.item.VallsaronLootItem(new Item.Properties().rarity(net.minecraft.world.item.Rarity.RARE),
                    "item.rpgrealms.vallsaron_fin.lore", false));
    public static final RegistryObject<Item> VALLSARON_TOOTH = ITEMS.register("vallsaron_tooth",
            () -> new com.rpgrealms.item.VallsaronLootItem(new Item.Properties().rarity(net.minecraft.world.item.Rarity.RARE),
                    "item.rpgrealms.vallsaron_tooth.lore", false));
    public static final RegistryObject<Item> VALLSARON_EYE = ITEMS.register("vallsaron_eye",
            () -> new com.rpgrealms.item.VallsaronLootItem(new Item.Properties().stacksTo(16).rarity(net.minecraft.world.item.Rarity.EPIC),
                    "item.rpgrealms.vallsaron_eye.lore", true));
    public static final RegistryObject<Item> VALLSARON_HEART = ITEMS.register("vallsaron_heart",
            () -> new com.rpgrealms.item.VallsaronLootItem(new Item.Properties().stacksTo(16).rarity(net.minecraft.world.item.Rarity.EPIC),
                    "item.rpgrealms.vallsaron_heart.lore", true));

    // Munición del adulto (no se obtiene; solo sirve de modelo del proyectil)
    public static final RegistryObject<Item> ABYSS_ORB = ITEMS.register("abyss_orb",
            () -> new Item(new Item.Properties()));
}

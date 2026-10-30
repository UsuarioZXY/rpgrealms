package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.item.AshenBladeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RpgRealms.MODID);

    public static final RegistryObject<Item> ASHEN_BLADE = ITEMS.register("ashen_blade",
            () -> new AshenBladeItem(Tiers.DIAMOND, 4, -2.4F,
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> ASHEN_KING_SPAWN_EGG = ITEMS.register("ashen_king_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ASHEN_KING, 0x2B2B2B, 0xFF6A00, new Item.Properties()));
}

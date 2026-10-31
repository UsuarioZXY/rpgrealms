package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {
    /** El Abismo: dimensión de cuevas gigantes y oscuras (definida en data/rpgrealms/dimension/abyss.json). */
    public static final ResourceKey<Level> ABYSS =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(RpgRealms.MODID, "abyss"));
}

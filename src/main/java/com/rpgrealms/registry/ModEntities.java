package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AbyssBoltEntity;
import com.rpgrealms.entity.LeviathanCubEntity;
import com.rpgrealms.entity.LeviathanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RpgRealms.MODID);

    /** Cría de Leviatán (rápida, poca vida y daño). Modelo a escala 2.6. */
    public static final RegistryObject<EntityType<LeviathanCubEntity>> LEVIATHAN_CUB = ENTITIES.register("leviathan_cub",
            () -> EntityType.Builder.of(LeviathanCubEntity::new, MobCategory.WATER_CREATURE)
                    .sized(4.8F, 3.2F)
                    .clientTrackingRange(12)
                    .build("leviathan_cub"));

    /** Leviatán adulto (colosal, lento, dispara orbes y rayo). Modelo a escala 6.5. */
    public static final RegistryObject<EntityType<LeviathanEntity>> LEVIATHAN = ENTITIES.register("leviathan",
            () -> EntityType.Builder.of(LeviathanEntity::new, MobCategory.WATER_CREATURE)
                    .sized(11.5F, 7.0F)
                    .clientTrackingRange(32)
                    .build("leviathan"));

    /** Orbe abisal que escupe el adulto. */
    public static final RegistryObject<EntityType<AbyssBoltEntity>> ABYSS_BOLT = ENTITIES.register("abyss_bolt",
            () -> EntityType.Builder.<AbyssBoltEntity>of(AbyssBoltEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .fireImmune()
                    .build("abyss_bolt"));
}

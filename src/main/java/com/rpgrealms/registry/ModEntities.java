package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AbyssBoltEntity;
import com.rpgrealms.entity.LeviathanEggEntity;
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
                    .sized(4.4F, 3.8F)
                    .clientTrackingRange(12)
                    .build("leviathan_cub"));

    /** Leviatán adulto (colosal, lento, dispara orbes y rayo). Modelo a escala 6.5. */
    public static final RegistryObject<EntityType<LeviathanEntity>> LEVIATHAN = ENTITIES.register("leviathan",
            () -> EntityType.Builder.of(LeviathanEntity::new, MobCategory.WATER_CREATURE)
                    .sized(10.0F, 7.5F)
                    .clientTrackingRange(32)
                    .build("leviathan"));

    /** Huevo de Leviatán: eclosiona en una Cría. */
    public static final RegistryObject<EntityType<LeviathanEggEntity>> LEVIATHAN_EGG = ENTITIES.register("leviathan_egg",
            () -> EntityType.Builder.<LeviathanEggEntity>of(LeviathanEggEntity::new, MobCategory.MISC)
                    .sized(2.4F, 2.9F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("leviathan_egg"));

    /** Orbe abisal que escupe el adulto. */
    public static final RegistryObject<EntityType<AbyssBoltEntity>> ABYSS_BOLT = ENTITIES.register("abyss_bolt",
            () -> EntityType.Builder.<AbyssBoltEntity>of(AbyssBoltEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .fireImmune()
                    .build("abyss_bolt"));
}

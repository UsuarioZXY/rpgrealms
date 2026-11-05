package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AbyssBoltEntity;
import com.rpgrealms.entity.LeviathanEggEntity;
import com.rpgrealms.entity.LeviathanTeenEntity;
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

    /** Leviatán adolescente (crece hasta adulto). Modelo a escala 4.4. */
    public static final RegistryObject<EntityType<LeviathanTeenEntity>> LEVIATHAN_TEEN = ENTITIES.register("leviathan_teen",
            () -> EntityType.Builder.of(LeviathanTeenEntity::new, MobCategory.WATER_CREATURE)
                    .sized(6.8F, 5.5F)
                    .clientTrackingRange(24)
                    .build("leviathan_teen"));

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

    /** Pez linterna rúnico: pez de cardumen con runa y señuelo luminoso. */
    public static final RegistryObject<EntityType<com.rpgrealms.entity.LanternFishEntity>> LANTERN_FISH = ENTITIES.register("lantern_fish",
            () -> EntityType.Builder.of(com.rpgrealms.entity.LanternFishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.6F, 0.5F)
                    .clientTrackingRange(4)
                    .build("lantern_fish"));

    /** Orbe abisal que escupe el adulto. */
    public static final RegistryObject<EntityType<AbyssBoltEntity>> ABYSS_BOLT = ENTITIES.register("abyss_bolt",
            () -> EntityType.Builder.<AbyssBoltEntity>of(AbyssBoltEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .fireImmune()
                    .build("abyss_bolt"));
}

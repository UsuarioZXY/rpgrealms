package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AshenKingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RpgRealms.MODID);

    public static final RegistryObject<EntityType<AshenKingEntity>> ASHEN_KING = ENTITIES.register("ashen_king",
            () -> EntityType.Builder.of(AshenKingEntity::new, MobCategory.MONSTER)
                    .sized(1.2F, 3.4F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build("ashen_king"));
}

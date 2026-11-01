package com.bluelock.mod.registry;

import com.bluelock.mod.BlueLockMod;
import com.bluelock.mod.entity.SoccerBallEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES, BlueLockMod.MODID);

    public static final RegistryObject<EntityType<SoccerBallEntity>> SOCCER_BALL =
            ENTITIES.register("soccer_ball", () -> EntityType.Builder
                    .<SoccerBallEntity>of(SoccerBallEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("soccer_ball"));
}

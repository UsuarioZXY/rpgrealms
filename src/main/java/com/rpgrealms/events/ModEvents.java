package com.rpgrealms.events;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AbstractLeviathan;
import com.rpgrealms.entity.LeviathanCubEntity;
import com.rpgrealms.entity.LeviathanEntity;
import com.rpgrealms.registry.ModEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpgRealms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.LEVIATHAN_CUB.get(), LeviathanCubEntity.createAttributes().build());
        event.put(ModEntities.LEVIATHAN_TEEN.get(), com.rpgrealms.entity.LeviathanTeenEntity.createAttributes().build());
        event.put(ModEntities.LEVIATHAN.get(), LeviathanEntity.createAttributes().build());
        event.put(ModEntities.LANTERN_FISH.get(), com.rpgrealms.entity.LanternFishEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.LANTERN_FISH.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                com.rpgrealms.entity.LanternFishEntity::checkSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.LEVIATHAN_CUB.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                AbstractLeviathan::checkWaterSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.LEVIATHAN.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                AbstractLeviathan::checkWaterSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}

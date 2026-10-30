package com.rpgrealms.events;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.entity.AshenKingEntity;
import com.rpgrealms.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpgRealms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ASHEN_KING.get(), AshenKingEntity.createAttributes().build());
    }
}

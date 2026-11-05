package com.rpgrealms.entity;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Cualquier golpe de un Vallsaron (mordisco, orbe, rayo, onda, embestida) dispara su animación de mordisco. */
@Mod.EventBusSubscriber(modid = "rpgrealms")
public class LeviathanHitEvents {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof AbstractLeviathan l && event.getEntity() != l) {
            l.biteHit(event.getEntity());
        }
    }
}

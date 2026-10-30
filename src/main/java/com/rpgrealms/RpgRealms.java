package com.rpgrealms;

import com.rpgrealms.registry.ModEntities;
import com.rpgrealms.registry.ModItems;
import com.rpgrealms.registry.ModTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RpgRealms.MODID)
public class RpgRealms {
    public static final String MODID = "rpgrealms";

    public RpgRealms() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        ModTabs.TABS.register(bus);
    }
}

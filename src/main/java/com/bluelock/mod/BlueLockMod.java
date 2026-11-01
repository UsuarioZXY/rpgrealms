package com.bluelock.mod;

import com.bluelock.mod.network.ModNetwork;
import com.bluelock.mod.registry.ModBlocks;
import com.bluelock.mod.registry.ModCreativeTabs;
import com.bluelock.mod.registry.ModEntities;
import com.bluelock.mod.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(BlueLockMod.MODID)
public class BlueLockMod {
    public static final String MODID = "bluelock";

    public BlueLockMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModCreativeTabs.TABS.register(modBus);

        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }
}

package com.rpgrealms.registry;

import com.rpgrealms.RpgRealms;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RpgRealms.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.rpgrealms"))
                    .icon(() -> new ItemStack(ModItems.LEVIATHAN_SPAWN_EGG.get()))
                    .displayItems((params, output) -> output.accept(ModItems.LEVIATHAN_SPAWN_EGG.get()))
                    .build());
}

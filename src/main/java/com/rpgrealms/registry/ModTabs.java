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
                    .displayItems((params, output) -> {
                        output.accept(ModItems.LEVIATHAN_NEST.get());
                        output.accept(ModItems.LEVIATHAN_EGG.get());
                        output.accept(ModItems.LEVIATHAN_CUB_SPAWN_EGG.get());
                        output.accept(ModItems.LEVIATHAN_TEEN_SPAWN_EGG.get());
                        output.accept(ModItems.LEVIATHAN_SPAWN_EGG.get());
                        output.accept(ModItems.LANTERN_FISH_SPAWN_EGG.get());
                        output.accept(ModItems.LANTERN_FISH_BUCKET.get());
                        output.accept(ModItems.RUNE_LANTERN_FISH.get());
                        output.accept(ModItems.VALLSARON_SCALE.get());
                        output.accept(ModItems.VALLSARON_FIN.get());
                        output.accept(ModItems.VALLSARON_TOOTH.get());
                        output.accept(ModItems.VALLSARON_EYE.get());
                        output.accept(ModItems.VALLSARON_HEART.get());
                    })
                    .build());
}

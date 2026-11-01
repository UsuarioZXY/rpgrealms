package com.bluelock.mod.registry;

import com.bluelock.mod.BlueLockMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BlueLockMod.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bluelock"))
                    .icon(() -> new ItemStack(ModItems.SOCCER_BALL.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.SOCCER_BALL.get());
                        output.accept(ModItems.GOAL.get());
                        output.accept(ModItems.GOAL_POST.get());
                        output.accept(ModItems.GOAL_NET.get());
                    })
                    .build());
}

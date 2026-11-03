package com.rpgrealms.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/** Huevo generador que muestra la clase de la criatura (Leviatán) en su descripción. */
public class ClassSpawnEggItem extends ForgeSpawnEggItem {
    public ClassSpawnEggItem(RegistryObject<? extends EntityType<? extends Mob>> type, int bg, int fg, Item.Properties props) {
        super(type, bg, fg, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip,
                                net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.rpgrealms.class_leviathan").withStyle(ChatFormatting.AQUA));
    }
}

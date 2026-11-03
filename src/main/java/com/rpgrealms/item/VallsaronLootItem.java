package com.rpgrealms.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** Botín del Vallsaron (por ahora solo se obtiene al derrotarlo): muestra su historia y su clase. */
public class VallsaronLootItem extends Item {
    private final String loreKey;
    private final boolean foil;

    public VallsaronLootItem(Properties properties, String loreKey, boolean foil) {
        super(properties);
        this.loreKey = loreKey;
        this.foil = foil;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.foil || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(this.loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("tooltip.rpgrealms.class_leviathan").withStyle(ChatFormatting.AQUA));
    }
}

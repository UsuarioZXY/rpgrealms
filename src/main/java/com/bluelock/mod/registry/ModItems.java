package com.bluelock.mod.registry;

import com.bluelock.mod.BlueLockMod;
import com.bluelock.mod.item.GoalItem;
import com.bluelock.mod.item.SoccerBallItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BlueLockMod.MODID);

    public static final RegistryObject<Item> SOCCER_BALL =
            ITEMS.register("soccer_ball", () -> new SoccerBallItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> GOAL =
            ITEMS.register("goal", () -> new GoalItem(new Item.Properties().stacksTo(4)));

    public static final RegistryObject<Item> GOAL_POST =
            ITEMS.register("goal_post", () -> new BlockItem(ModBlocks.GOAL_POST.get(), new Item.Properties()));

    public static final RegistryObject<Item> GOAL_NET =
            ITEMS.register("goal_net", () -> new BlockItem(ModBlocks.GOAL_NET.get(), new Item.Properties()));
}

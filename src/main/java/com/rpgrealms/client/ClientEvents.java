package com.rpgrealms.client;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpgRealms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LeviathanCubModel.LAYER, LeviathanCubModel::createBodyLayer);
        event.registerLayerDefinition(LeviathanTeenModel.LAYER, LeviathanTeenModel::createBodyLayer);
        event.registerLayerDefinition(LeviathanModel.LAYER, LeviathanModel::createBodyLayer);
        event.registerLayerDefinition(LeviathanEggModel.LAYER, LeviathanEggModel::createBodyLayer);
        event.registerLayerDefinition(LanternFishModel.LAYER, LanternFishModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.LEVIATHAN_CUB.get(), LeviathanCubRenderer::new);
        event.registerEntityRenderer(ModEntities.LEVIATHAN_TEEN.get(), LeviathanTeenRenderer::new);
        event.registerEntityRenderer(ModEntities.LEVIATHAN.get(), LeviathanRenderer::new);
        event.registerEntityRenderer(ModEntities.ABYSS_BOLT.get(), AbyssBoltRenderer::new);
        event.registerEntityRenderer(ModEntities.LANTERN_FISH.get(), LanternFishRenderer::new);
        event.registerEntityRenderer(ModEntities.LEVIATHAN_EGG.get(), LeviathanEggRenderer::new);
    }
}

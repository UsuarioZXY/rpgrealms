package com.rpgrealms.client;

import com.rpgrealms.RpgRealms;
import com.rpgrealms.registry.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpgRealms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LeviathanCubModel.LAYER, LeviathanCubModel::createBodyLayer);
        event.registerLayerDefinition(LeviathanModel.LAYER, LeviathanModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.LEVIATHAN_CUB.get(), LeviathanCubRenderer::new);
        event.registerEntityRenderer(ModEntities.LEVIATHAN.get(), LeviathanRenderer::new);
        event.registerEntityRenderer(ModEntities.ABYSS_BOLT.get(), ctx -> new ThrownItemRenderer<>(ctx, 3.0F, true));
    }
}

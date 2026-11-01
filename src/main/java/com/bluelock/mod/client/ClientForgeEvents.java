package com.bluelock.mod.client;

import com.bluelock.mod.BlueLockMod;
import com.bluelock.mod.network.KickPacket;
import com.bluelock.mod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Patada cargada: al apretar la tecla empieza a cargar, al soltarla se manda la patada.
 * Un toque rapido da una patada suave; mantenerla (1 segundo) da la maxima potencia.
 */
@Mod.EventBusSubscriber(modid = BlueLockMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    private static boolean charging = false;
    private static int chargeTicks = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            charging = false;
            chargeTicks = 0;
            return;
        }

        if (ClientModEvents.KICK_KEY.consumeClick()) {
            charging = true;
            chargeTicks = 0;
        }

        if (charging) {
            if (ClientModEvents.KICK_KEY.isDown()) {
                chargeTicks = Math.min(KickPacket.MAX_CHARGE, chargeTicks + 1);
                mc.player.displayClientMessage(Component.literal(bar(chargeTicks)), true);
            } else {
                ModNetwork.CHANNEL.sendToServer(new KickPacket(chargeTicks));
                charging = false;
                chargeTicks = 0;
            }
        }
    }

    private static String bar(int ticks) {
        int max = KickPacket.MAX_CHARGE;
        String color = ticks >= max ? "§c" : (ticks > max / 2 ? "§6" : "§e");
        return color + "█".repeat(ticks) + "§8" + "█".repeat(max - ticks);
    }
}

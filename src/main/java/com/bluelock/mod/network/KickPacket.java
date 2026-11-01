package com.bluelock.mod.network;

import com.bluelock.mod.entity.SoccerBallEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Comparator;
import java.util.function.Supplier;

/** El cliente pide patear el balon mas cercano. "charge" va de 0 a 20 (ticks que se mantuvo la tecla). */
public class KickPacket {
    public static final int MAX_CHARGE = 20;
    private static final double REACH = 3.0;

    private final int charge;

    public KickPacket(int charge) {
        this.charge = Math.max(0, Math.min(MAX_CHARGE, charge));
    }

    public static void encode(KickPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.charge);
    }

    public static KickPacket decode(FriendlyByteBuf buf) {
        return new KickPacket(buf.readVarInt());
    }

    public static void handle(KickPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null || player.isSpectator()) {
                return;
            }
            double power = 0.7 + 0.9 * msg.charge / MAX_CHARGE + (player.isSprinting() ? 0.3 : 0.0);
            player.level().getEntitiesOfClass(SoccerBallEntity.class, player.getBoundingBox().inflate(REACH))
                    .stream()
                    .min(Comparator.comparingDouble((SoccerBallEntity b) -> player.distanceToSqr(b)))
                    .ifPresent(ball -> ball.kick(player, power));
        });
        ctx.setPacketHandled(true);
    }
}

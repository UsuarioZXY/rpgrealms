package com.bluelock.mod.item;

import com.bluelock.mod.entity.SoccerBallEntity;
import com.bluelock.mod.registry.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Click derecho: suelta un balon frente al jugador. */
public class SoccerBallItem extends Item {
    public SoccerBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            SoccerBallEntity ball = new SoccerBallEntity(ModEntities.SOCCER_BALL.get(), level);
            Vec3 look = player.getLookAngle();
            Vec3 forward = new Vec3(look.x, 0, look.z).normalize().scale(1.2);
            ball.setPos(player.getX() + forward.x, player.getY() + 0.3, player.getZ() + forward.z);
            ball.setHome(ball.position());
            level.addFreshEntity(ball);
            level.playSound(null, player.blockPosition(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 0.8F, 1.2F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

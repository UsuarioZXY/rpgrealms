package com.rpgrealms.item;

import com.rpgrealms.entity.LeviathanEggEntity;
import com.rpgrealms.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/** Coloca un Huevo de Leviatán como entidad (en tierra o sobre/dentro del agua). */
public class LeviathanEggItem extends Item {
    public LeviathanEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        BlockPos pos = hit.getBlockPos();
        BlockPos place = level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                ? pos : pos.relative(hit.getDirection());
        LeviathanEggEntity egg = ModEntities.LEVIATHAN_EGG.get().create(level);
        if (egg == null) {
            return InteractionResultHolder.fail(stack);
        }
        egg.moveTo(place.getX() + 0.5D, place.getY(), place.getZ() + 0.5D, player.getYRot(), 0.0F);
        level.addFreshEntity(egg);
        level.gameEvent(player, GameEvent.ENTITY_PLACE, place);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, false);
    }
}

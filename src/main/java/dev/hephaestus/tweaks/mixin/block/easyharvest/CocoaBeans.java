package dev.hephaestus.tweaks.mixin.block.easyharvest;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.TweaksPreferences;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CocoaBlock.class)
public abstract class CocoaBeans extends HorizontalFacingBlock {
    protected CocoaBeans(Settings settings) { super(settings);}

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (TweaksConfig.Plants.EasyHarvest.CROPS.getValue() && state.get(CocoaBlock.AGE) == 2) {
            if (!world.isClient) {
                Block.getDroppedStacks(state, (ServerWorld) world, pos, null).forEach((stack) -> {
                    if (stack.getItem() == Items.COCOA_BEANS) {
                        stack.setCount(stack.getCount() - 1);
                    }

                    boolean playerAvailable = player.isAlive() || player instanceof ServerPlayerEntity && !((ServerPlayerEntity)player).isDisconnected();
                    if (playerAvailable && !TweaksPreferences.EASY_HARVEST_DROP_AS_ITEMS.getValue(player.getUuid())) {
                        player.inventory.offerOrDrop(world, stack);
                    } else {
                        Block.dropStack(world, pos, stack);
                    }
                });

                world.setBlockState(pos, state.with(CocoaBlock.AGE, 0));
            }

            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}

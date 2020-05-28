package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.PlantBlock;
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

@Mixin(NetherWartBlock.class)
public abstract class NetherWartBlockMixin extends PlantBlock {
    protected NetherWartBlockMixin(Settings settings) { super(settings);}

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (Tweaks.CONFIG.easyHarvestCrops && state.get(NetherWartBlock.AGE) == 3) {
            if (!world.isClient) {
                Block.getDroppedStacks(state, (ServerWorld) world, pos, null).forEach((stack) -> {
                    if (stack.getItem() == Items.NETHER_WART) {
                        stack.setCount(stack.getCount() - 1);
                    }

                    boolean playerAvailable = player.isAlive() || player instanceof ServerPlayerEntity && !((ServerPlayerEntity)player).isDisconnected();
                    if (playerAvailable)
                        player.inventory.offerOrDrop(world, stack);
                    else
                        Block.dropStack(world, pos, stack);
                });

                world.setBlockState(pos, state.with(NetherWartBlock.AGE, 0));
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}

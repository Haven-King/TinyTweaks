package dev.hephaestus.tweaks.mixin.plants;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("deprecation")
@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock {
    protected CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Shadow public abstract boolean isMature(BlockState blockState);
    @Shadow public abstract BlockState withAge(int age);

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (Tweaks.CONFIG.easyHarvestCrops && this.isMature(state)) {
            if (!world.isClient) {
                Block.getDroppedStacks(state, (ServerWorld) world, pos, null).forEach((stack) -> {
                    if (stack.getItem().toString().contains("seeds")) {
                        stack.setCount(stack.getCount() - 1);
                    }

                    boolean playerAvailable = player.isAlive() || player instanceof ServerPlayerEntity && !((ServerPlayerEntity)player).method_14239();
                    if (playerAvailable)
                        player.inventory.offerOrDrop(world, stack);
                    else
                        Block.dropStack(world, pos, stack);
                });
                
                world.setBlockState(pos, this.withAge(0));
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}

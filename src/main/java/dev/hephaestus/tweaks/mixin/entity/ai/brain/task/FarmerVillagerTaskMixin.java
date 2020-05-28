package dev.hephaestus.tweaks.mixin.entity.ai.brain.task;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ai.brain.task.FarmerVillagerTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(FarmerVillagerTask.class)
public class FarmerVillagerTaskMixin {
    @Shadow @Nullable private BlockPos currentTarget;
    private BlockPos storage;

    private static boolean isStorage(Block block) {
        return block instanceof ChestBlock;
    }

    private long nextStoreTime;
    @Inject(method = "shouldRun", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(DDD)Lnet/minecraft/util/math/BlockPos$Mutable;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void setDepositBlock(ServerWorld serverWorld, VillagerEntity villagerEntity, CallbackInfoReturnable<Boolean> cir, BlockPos.Mutable mutable) {
        BlockState blockState = serverWorld.getBlockState(mutable);
        Block block = blockState.getBlock();
        if (Tweaks.CONFIG.farmerVillagerAutomation && (storage == null || !isStorage(serverWorld.getBlockState(storage).getBlock())) && isStorage(block)) {
            this.storage = new BlockPos(mutable);
            nextStoreTime = serverWorld.getTime() + 100L;
        }
    }

    @Inject(method = "chooseRandomTarget", at = @At("HEAD"), cancellable = true)
    public void shouldStore(ServerWorld world, CallbackInfoReturnable<BlockPos> cir) {
        if (Tweaks.CONFIG.farmerVillagerAutomation && storage != null && isStorage(world.getBlockState(storage).getBlock()) && world.getTime() > nextStoreTime) {
            cir.setReturnValue(storage);
        }
    }

    @Inject(method = "keepRunning", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void shouldStoreThings(ServerWorld serverWorld, VillagerEntity villagerEntity, long l, CallbackInfo ci, BlockState blockState, Block block) {
        if (Tweaks.CONFIG.farmerVillagerAutomation && currentTarget == storage) {
            @SuppressWarnings("ConstantConditions")
            Inventory target = HopperBlockEntity.getInventoryAt(serverWorld, this.currentTarget);

            if (target != null) {
                Inventory inventory = villagerEntity.getInventory();
                for(int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getStack(i);
                    if (stack.getCount() > stack.getMaxCount()/2) {
                        int amount = stack.getCount() - stack.getMaxCount()/2;
                        ItemStack itemStack2 = HopperBlockEntity.transfer(inventory, target, inventory.removeStack(i, amount), null);
                        if (itemStack2.isEmpty()) {
                            inventory.markDirty();
                            break;
                        }

                        inventory.getStack(i).decrement(amount);
                    }
                }
            }

            nextStoreTime = l + 600L;
        }
    }
}

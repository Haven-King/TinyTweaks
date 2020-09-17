package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.block.LeaveBlockWaterLogGetter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(at = @At("RETURN"), method = "getFluidState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;", cancellable = true)
    private void injectLeavesFluidState(BlockState state, CallbackInfoReturnable<FluidState> cir) {
        if (state.getBlock() instanceof LeavesBlock) {
            if (state.get(((LeaveBlockWaterLogGetter) this).getWaterLogProperty())) {
                cir.setReturnValue(Fluids.WATER.getStill(false));
            } else {
                cir.setReturnValue(cir.getReturnValue());
            }
        }
    }
}

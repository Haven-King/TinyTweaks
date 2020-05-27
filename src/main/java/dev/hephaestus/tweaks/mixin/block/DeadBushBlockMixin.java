package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DeadBushBlock.class)
public class DeadBushBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void canBeOnGrass(BlockState floor, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (Tweaks.CONFIG.rejuvenation.enabled && floor.getBlock() == Blocks.GRASS_BLOCK)
            cir.setReturnValue(true);
    }
}

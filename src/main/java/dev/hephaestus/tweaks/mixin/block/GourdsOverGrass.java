package dev.hephaestus.tweaks.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StemBlock.class)
public class GourdsOverGrass {
    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"))
    private boolean canBeReplaced(BlockState blockState) {
        return blockState.getMaterial().isReplaceable() || blockState.isAir();
    }
}

package dev.hephaestus.tweaks.mixin.block.mossythings;

import dev.hephaestus.tweaks.block.Moistener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MoistenBlock {
	@Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
	private void hasRandomTicks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (Moistener.canMoisten(state.getBlock())) {
			cir.setReturnValue(true);
		}
	}
}

package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BubbleColumnBlock.class, priority = 2000)
public class BubbleColumnBlockMixin {
	@Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"))
	private <T> void dontFluidTick(TickScheduler<T> tickScheduler, BlockPos pos, T object, int delay) {
		if (Tweaks.CONFIG.bubbleColumnsFlow) {
			tickScheduler.schedule(pos, object, delay);
		}
	}
}

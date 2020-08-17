package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MoveLilyPadsDown {
	@Shadow public abstract Block getBlock();

	private static final Vec3d DOWN = new Vec3d(0, -0.1D, 0);

	@Inject(method = "getModelOffset", at = @At("HEAD"), cancellable = true)
	private void offsetLilyPadsDown(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
		if (Tweaks.CONFIG.betterLilyPads && this.getBlock() == Blocks.LILY_PAD) {
			cir.setReturnValue(DOWN);
		}
	}
}

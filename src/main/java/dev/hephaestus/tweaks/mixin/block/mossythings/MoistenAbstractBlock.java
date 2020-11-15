package dev.hephaestus.tweaks.mixin.block.mossythings;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.block.Moistener;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractBlock.class)
public class MoistenAbstractBlock {
	@Inject(method = "scheduledTick", at = @At("HEAD"))
	private void doTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (Tweaks.CONFIG.mossyThings && Moistener.canMoisten(state.getBlock())) {
			boolean isSkyVisible = false;
			boolean isWaterNearby = false;
			for (BlockPos adjacent : BlockPos.iterate(pos.up().north().west(), pos.down().south().east())) {
				if (world.isSkyVisible(adjacent)) isSkyVisible = true;
				if (world.getFluidState(adjacent).getFluid() == Fluids.WATER) isWaterNearby = true;
			}

			if (isSkyVisible) {
				if (world.isRaining() && world.getBiome(pos).getDownfall() > 0) {
					world.setBlockState(pos, Moistener.moisten(state));
				} else if (world.isDay() && !isWaterNearby) {
					world.setBlockState(pos, Moistener.dry(state));
				}

			}
		}
	}
}

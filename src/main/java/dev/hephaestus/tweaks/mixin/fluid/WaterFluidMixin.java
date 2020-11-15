package dev.hephaestus.tweaks.mixin.fluid;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.block.Moistener;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin extends Fluid {
	@Override
	protected void onRandomTick(World world, BlockPos pos, FluidState state, Random random) {
		if (this.isStill(state) && Tweaks.CONFIG.mossyThings) {
			Iterable<BlockPos> adjacent = BlockPos.iterate(pos.down().north().west(), pos.south().east());

			for (BlockPos potentialPos : adjacent) {
				if (random.nextFloat() < 0.05) {
					BlockState potentialState = world.getBlockState(potentialPos);
					BlockState moist = Moistener.moisten(potentialState);
					if (moist != potentialState) {
						world.setBlockState(potentialPos, moist);
					}
				}
			}
		}
	}

	@Override
	protected boolean hasRandomTicks() {
		return true;
	}
}

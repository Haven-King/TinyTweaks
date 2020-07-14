package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NyliumBlock.class)
public abstract class NyliumBlockMixin {
	@Shadow private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {return true;}

	private static boolean canSpread(BlockState state, WorldView worldView, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = worldView.getBlockState(blockPos);
		return blockState.isAir() &&
				(state.getBlock() == Blocks.WARPED_NYLIUM && worldView.getBiome(pos) == Biomes.WARPED_FOREST ||
				state.getBlock() == Blocks.CRIMSON_NYLIUM && worldView.getBiome(pos) == Biomes.CRIMSON_FOREST);
	}

	@Inject(method = "randomTick", at = @At("TAIL"))
	private void spread(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (Tweaks.CONFIG.netherRejuvenation.enabled && stayAlive(state, world, pos)) {
			int friends = 0;

			for (int x = -1; x <= 1; x++) {
				for (int y = 0; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						Block friend = world.getBlockState(pos.add(x, y, z)).getBlock();
						if (friend instanceof FernBlock || friend instanceof TallPlantBlock) {
							friends++;
							friends *= 1.25;
						}
					}
				}
			}

			if (world.getBlockState(pos.up()).isAir() && random.nextFloat() < Tweaks.CONFIG.netherRejuvenation.growthRate + 0.2 * (friends)) {
				float f = random.nextFloat();
				double ratio = Tweaks.CONFIG.netherRejuvenation.sproutRootsRatio / 100D;
				Block block;

				if (f < Tweaks.CONFIG.netherRejuvenation.vinesChance && state.isOf(Blocks.WARPED_NYLIUM)) {
					block = Blocks.TWISTING_VINES;
				} else if (f > ratio) {
					block = state.isOf(Blocks.CRIMSON_NYLIUM) ? Blocks.CRIMSON_ROOTS : Blocks.WARPED_ROOTS;
				} else {
					block = state.isOf(Blocks.CRIMSON_NYLIUM) ? Blocks.CRIMSON_ROOTS : Blocks.NETHER_SPROUTS;
				}

				world.setBlockState(pos.up(), block.getDefaultState());
			} else {
				for(int i = 0; i < 4; ++i) {
					BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

					if (world.getBlockState(blockPos).isOf(Blocks.NETHERRACK) && canSpread(state, world, blockPos)) {
						world.setBlockState(blockPos, state);
					}
				}
			}
		}
	}
}

package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NyliumBlock.class)
public abstract class NetherRejuvenationNyliumSpread {
	@Shadow private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {return true;}

	private static boolean canSpread(BlockState state, ServerWorld world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = world.getBlockState(blockPos);



		return blockState.isAir() &&
				(state.getBlock() == Blocks.WARPED_NYLIUM && world.getRegistryManager().get(Registry.BIOME_KEY).getId(world.getBiome(pos)).getPath().equals("warped_forest") ||
				state.getBlock() == Blocks.CRIMSON_NYLIUM && world.getRegistryManager().get(Registry.BIOME_KEY).getId(world.getBiome(pos)).getPath().equals("crimson_forest"));
	}

	@Inject(method = "randomTick", at = @At("TAIL"))
	private void spread(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (Tweaks.CONFIG.netherRejuvenation.enabled && stayAlive(state, world, pos)) {
			int friends = 0;

			for (int x = -1; x <= 1; x++) {
				for (int y = 0; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						Block friend = world.getBlockState(pos.add(x, y, z)).getBlock();
						if (friend instanceof RootsBlock || friend instanceof SproutsBlock) {
							friends++;
							friends *= Tweaks.CONFIG.netherRejuvenation.thePowerOfFriendship;
						}
					}
				}
			}

			if (world.getBlockState(pos.up()).isAir() && random.nextFloat() < Tweaks.CONFIG.netherRejuvenation.rootsGrowthRate * (friends)) {
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

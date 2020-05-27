package dev.hephaestus.tweaks.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

public class MoistBlock extends Block {
	public MoistBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		randomTick(state, world, pos);
	}

	public static void randomTick(BlockState state, ServerWorld world, BlockPos pos) {
		if (Tweaks.CONFIG.mossyThings) {
			boolean isSkyVisible = false;
			for (BlockPos adjacent : BlockPos.iterate(pos.up().north().west(), pos.south().east())) {
				if (world.isSkyVisible(adjacent)) isSkyVisible = true;
			}

			if (isSkyVisible) {
				if (world.isRaining()) {
					world.setBlockState(pos, Moistener.moisten(state));
				} else if (world.isDay()) {
					world.setBlockState(pos, Moistener.dry(state));
				}
			}
		}
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public int getTickRate(WorldView worldView) {
		return 5;
	}
}

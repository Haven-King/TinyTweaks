package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.block.MoistBlock;
import dev.hephaestus.tweaks.block.Moistener;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin({StairsBlock.class, SlabBlock.class, WallBlock.class})
public abstract class MoistObjectsMixin extends Block {
	public MoistObjectsMixin(Settings settings) {
		super(settings);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		MoistBlock.randomTick(state, world, pos);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
}

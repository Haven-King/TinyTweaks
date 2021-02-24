package dev.hephaestus.tweaks.mixin.world;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class TickMore extends World {
	protected TickMore(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;getYOffset()I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void tickMore(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean bl, int startX, int startZ, Profiler profiler, ChunkSection[] var8, int var9, int var10, ChunkSection chunkSection) {
		int startY = chunkSection.getYOffset();

		for (int i = 0; i < randomTickSpeed; ++i) {
			for (int j = 0; j < TweaksConfig.Plants.Leaves.LEAF_DECAY_SPEED.getValue() - 1; ++j) {
				BlockPos blockPos4 = this.getRandomPosInChunk(startX, startY, startZ, 15);
				profiler.push("randomTick");
				BlockState blockState = chunkSection.getBlockState(blockPos4.getX() - startX, blockPos4.getY() - startY, blockPos4.getZ() - startZ);

				if (blockState.hasRandomTicks() && blockState.isIn(BlockTags.LEAVES)) {
					blockState.randomTick((ServerWorld) (Object) this, blockPos4, this.random);
				}

				profiler.pop();
			}
		}
	}
}

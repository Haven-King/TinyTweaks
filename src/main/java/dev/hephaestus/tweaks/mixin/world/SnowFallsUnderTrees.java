package dev.hephaestus.tweaks.mixin.world;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class SnowFallsUnderTrees extends World {
    protected SnowFallsUnderTrees(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void snowFalls(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean raining, int i, int j, Profiler profiler, BlockPos blockPos2, BlockPos blockPos3, Biome biome) {
        if (raining && biome.getTemperature(blockPos2) < 0.15F) {
            BlockState state = this.getBlockState(blockPos2);
            int bottom = this.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockPos2).getY() - 1;

            if (TweaksConfig.Plants.Leaves.SNOW_FALLS_THROUGH_LEAVES.getValue() && state.isOf(Blocks.SNOW) && bottom < blockPos3.getY() - 1) {
                BlockPos.Mutable mut = new BlockPos.Mutable().set(blockPos3).move(Direction.DOWN);

                while (mut.getY() > bottom) {
                    if (biome.canSetSnow(this, mut)) {
                        this.setBlockState(mut, Blocks.SNOW.getDefaultState());
                        break;
                    }

                    mut.move(Direction.DOWN);
                }
            }
        }
    }
}
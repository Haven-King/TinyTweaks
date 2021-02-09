package dev.hephaestus.tweaks.mixin.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(FreezeTopLayerFeature.class)
public class SnowGeneratesUnderTrees {
    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void keepItGoing(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> cir, BlockPos.Mutable mutable, BlockPos.Mutable mutable2, int i, int j, int k, int l, int m, Biome biome) {
        int bottom = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, k, l);

        BlockPos.Mutable mut2 = new BlockPos.Mutable();

        for (BlockPos pos : BlockPos.iterate(mutable, new BlockPos(k, bottom, l))) {
            if (biome.canSetSnow(structureWorldAccess, pos)) {
                mut2.set(pos).move(Direction.DOWN);
                structureWorldAccess.setBlockState(pos, Blocks.SNOW.getDefaultState(), 2);
                BlockState blockState = structureWorldAccess.getBlockState(mut2);

                if (blockState.contains(SnowyBlock.SNOWY)) {
                    structureWorldAccess.setBlockState(mut2, blockState.with(SnowyBlock.SNOWY, true), 2);
                }
            }
        }
    }
}

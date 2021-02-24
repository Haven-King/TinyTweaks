package dev.hephaestus.tweaks.mixin.block.rejuvenation;

import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(SpreadableBlock.class)
public class TallGrassSpread {
    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "net/minecraft/block/SpreadableBlock.getDefaultState()Lnet/minecraft/block/BlockState;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void growGrass(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (TweaksConfig.Plants.Rejuvenation.ENABLED.getValue() && state.getBlock() == Blocks.GRASS_BLOCK) {
            Block above = world.getBlockState(pos.up()).getBlock();
            int friends = 1;


            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Block friend = world.getBlockState(pos.add(x, y, z)).getBlock();
                        if (friend instanceof FernBlock || friend instanceof TallPlantBlock) {
                            friends++;
                            friends *= TweaksConfig.Plants.Rejuvenation.FRIENDSHIP.getValue();
                        }
                    }
                }
            }

            if (above == Blocks.AIR && random.nextFloat() < TweaksConfig.Plants.Rejuvenation.GRASS_GROWTH_RATE.getValue() * (friends)) {
                world.setBlockState(pos.up(), Blocks.GRASS.getDefaultState());
                ci.cancel();
            } else if (TweaksConfig.Plants.Rejuvenation.LONG_GRASS_RATIO.getValue() > 0.000001 && above == Blocks.GRASS) {
                Random tallGrassRandom = new Random();
                tallGrassRandom.setSeed(pos.asLong());
                if (tallGrassRandom.nextFloat() < TweaksConfig.Plants.Rejuvenation.LONG_GRASS_RATIO.getValue()
                        && random.nextFloat() < TweaksConfig.Plants.Rejuvenation.GRASS_GROWTH_RATE.getValue()) {
                    ((TallPlantBlock) Blocks.TALL_GRASS).placeAt(world, pos.up(), 2);
                    ci.cancel();
                }
            }


            if (TweaksConfig.Plants.Rejuvenation.DEAD_BUSHES_TO_SAPLINGS.getValue()
                    && world.getBlockState(pos.up()).getBlock() == Blocks.DEAD_BUSH) {
                Block sapling = Blocks.OAK_SAPLING;
                world.setBlockState(pos.up(), sapling.getDefaultState());
                ci.cancel();
            }
        }
    }
}

package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchMixin extends TorchBlock {
    protected RedstoneTorchMixin(Settings settings, ParticleEffect particle) {
        super(settings, particle);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return ((!Tweaks.CONFIG.grassDestroysRedstoneTorches) || world.getBlockState(pos.down()).getBlock() != Blocks.GRASS_BLOCK) && super.canPlaceAt(state, world, pos);
    }
}

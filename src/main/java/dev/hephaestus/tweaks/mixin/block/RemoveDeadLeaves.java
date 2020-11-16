package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class RemoveDeadLeaves {
	@Shadow public abstract Block getBlock();

	@Shadow public abstract void randomTick(ServerWorld world, BlockPos pos, Random random);

	@Inject(method = "scheduledTick", at = @At("TAIL"))
	private void removeDeadLeaves(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (Tweaks.CONFIG.instantLeafDecay && this.getBlock().getStateManager().getProperties().contains(Properties.DISTANCE_1_7)) {
			this.randomTick(world, pos, random);
		}
	}
}

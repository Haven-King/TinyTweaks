package dev.hephaestus.tweaks.mixin.entity.betterlilypads;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class SprintingParticles {
	@Shadow public World world;

	@Redirect(method = "spawnSprintingParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private BlockState lilyPadsOnWater(World world, BlockPos pos) {
		BlockState original = world.getBlockState(pos);
		BlockState above = world.getBlockState(pos.up());

		return original.getBlock().is(Blocks.WATER) && above.getBlock().is(Blocks.LILY_PAD) ? above : original;
	}
}

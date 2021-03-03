package dev.hephaestus.tweaks.mixin.entity.betterlilypads;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class FallingParticles extends Entity {
	public FallingParticles(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "fall", at = @At(value = "NEW", target = "net/minecraft/particle/BlockStateParticleEffect", ordinal = 0))
	private BlockState changeLandedState(BlockState oldLandedState, double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
		BlockState above = this.world.getBlockState(landedPosition.up());
		return landedState.getBlock().is(Blocks.WATER) && above.getBlock().is(Blocks.LILY_PAD) ? above : landedState;
	}
}

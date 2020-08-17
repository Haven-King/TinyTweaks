package dev.hephaestus.tweaks.mixin.entity.betterlilypads;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class FallingParticles extends Entity {
	@Unique private BlockState landedState;
	@Unique private BlockPos landedPosition;

	public FallingParticles(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "fall", at = @At(value = "NEW", target = "net/minecraft/particle/BlockStateParticleEffect"))
	private void captureArgs(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
		this.landedState = landedState;
		this.landedPosition = landedPosition;
	}

	@Redirect(method = "fall", at = @At(value = "NEW", target = "net/minecraft/particle/BlockStateParticleEffect"))
	private BlockStateParticleEffect lilyPadsOnWater(ParticleType<BlockStateParticleEffect> particleType, BlockState blockState) {
		BlockState above = this.world.getBlockState(this.landedPosition.up());

		return new BlockStateParticleEffect(particleType, this.landedState.getBlock().is(Blocks.WATER) && above.getBlock().is(Blocks.LILY_PAD) ? above : this.landedState);
	}
}

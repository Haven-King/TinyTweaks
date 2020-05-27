package dev.hephaestus.tweaks.mixin.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "isClimbing", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	public void onIsClimbing(CallbackInfoReturnable<Boolean> info, BlockState state) {
		BlockPos pos = this.getBlockPos();

		if ((state.getBlock() instanceof LeavesBlock || world.getBlockState(pos.up()).getBlock() instanceof LeavesBlock)) {
			boolean logsAdjacent =
							world.getBlockState(pos.north()).getBlock() instanceof LogBlock ||
											world.getBlockState(pos.east()).getBlock() instanceof LogBlock ||
											world.getBlockState(pos.south()).getBlock() instanceof LogBlock ||
											world.getBlockState(pos.west()).getBlock() instanceof LogBlock;

			info.setReturnValue(logsAdjacent);
		}
	}
}

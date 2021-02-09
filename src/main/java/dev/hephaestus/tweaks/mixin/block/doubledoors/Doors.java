package dev.hephaestus.tweaks.mixin.block.doubledoors;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DoorBlock.class)
public abstract class Doors {
	@Shadow @Final public static DirectionProperty FACING;

	@Shadow @Final public static EnumProperty<DoorHinge> HINGE;

	@Shadow @Final public static BooleanProperty OPEN;

	@Shadow protected abstract int getCloseSoundEventId();

	@Shadow protected abstract int getOpenSoundEventId();

	@Shadow @Final public static BooleanProperty POWERED;

	@Inject(method = "onUse", at = @At(value = "TAIL", ordinal = 1))
	private void openOther(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		openOther(world, pos, player);
	}

	@Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void openOther(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci, boolean bl) {
		openOther(world, pos, null);
	}

	@Unique
	private void openOther(World world, BlockPos pos, PlayerEntity player) {
		if (Tweaks.CONFIG.doubleDoors) {
			BlockState state = world.getBlockState(pos);
			BlockPos otherPos = null;
			BlockState otherState = null;
			switch (state.get(HINGE)) {
				case LEFT:
					otherPos = pos.offset(state.get(FACING).rotateYClockwise());
					otherState = world.getBlockState(otherPos);
					break;
				case RIGHT:
					otherPos = pos.offset(state.get(FACING).rotateYCounterclockwise());
					otherState = world.getBlockState(otherPos);
					break;
			}

			if (otherState.getBlock() == state.getBlock() && otherState.get(FACING) == state.get(FACING) && otherState.get(OPEN) != state.get(OPEN)) {
				otherState = otherState.cycle(OPEN);
				world.setBlockState(otherPos, otherState, 10);
				world.syncWorldEvent(player, state.get(OPEN) ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
			}
		}
	}
}

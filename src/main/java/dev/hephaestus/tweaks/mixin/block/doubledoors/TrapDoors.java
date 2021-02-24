package dev.hephaestus.tweaks.mixin.block.doubledoors;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(TrapdoorBlock.class)
public abstract class TrapDoors extends HorizontalFacingBlock {
	@Shadow @Final public static BooleanProperty OPEN;

	@Shadow @Final public static BooleanProperty WATERLOGGED;

	@Shadow protected abstract void playToggleSound(@Nullable PlayerEntity player, World world, BlockPos pos, boolean open);

	@Shadow @Final public static EnumProperty<BlockHalf> HALF;

	protected TrapDoors(Settings settings) {
		super(settings);
	}

	@Inject(method = "onUse", at = @At(value = "TAIL", ordinal = 1))
	private void openOthers(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		openOthers(world, pos, player);
	}

	@Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void openOthers(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci) {
		openOthers(world, pos, null);
	}

	private void openOthers(World world, BlockPos pos, PlayerEntity player) {
		if (TweaksConfig.Misc.DOUBLE_DOORS.getValue()) {
			BlockState state = world.getBlockState(pos);
			BlockPos otherPos = pos.offset(state.get(FACING));
			BlockState otherState = world.getBlockState(otherPos);
			if (otherState.getBlock() == state.getBlock() && otherState.get(OPEN) != state.get(OPEN) && otherState.get(HALF) == state.get(HALF)) {
				world.setBlockState(otherPos, otherState.cycle(OPEN));
				toggleAdjacent(world, otherPos, player);
			}

			toggleAdjacent(world, pos, player);
		}
	}

	private void toggleAdjacent(World world, BlockPos pos, PlayerEntity player) {
		BlockState state = world.getBlockState(pos);
		Direction [] dirs = new Direction[2];
		switch (state.get(FACING)) {
			case NORTH:
			case SOUTH:
				dirs[0] = Direction.EAST;
				dirs[1] = Direction.WEST;
				break;
			case WEST:
			case EAST:
				dirs[0] = Direction.NORTH;
				dirs[1] = Direction.SOUTH;
				break;
			default:
				return;
		}

		BlockPos otherPos;
		BlockState otherState;
		for (int d = 0; d < 2; ++d) {
			for (int i = 1; i < 16; ++i) {
				if ((otherState = world.getBlockState(otherPos = pos.offset(dirs[d], i))).getBlock() == state.getBlock() && otherState.get(OPEN) != state.get(OPEN) && otherState.get(HALF) == state.get(HALF)) {
					world.setBlockState(otherPos, otherState.cycle(OPEN), 2);
					if (otherState.get(WATERLOGGED)) {
						world.getFluidTickScheduler().schedule(otherPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
					}

					this.playToggleSound(player, world, otherPos, otherState.get(OPEN));
				} else {
					break;
				}
			}
		}
	}
}

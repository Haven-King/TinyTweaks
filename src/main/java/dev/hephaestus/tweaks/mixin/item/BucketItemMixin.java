package dev.hephaestus.tweaks.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BucketItem.class, priority = 2000)
public class BucketItemMixin {
	boolean shouldChange = false;
	@Inject(method = "use", at = @At("HEAD"))
	private void captureVariables(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		shouldChange = user.isSneaking();
	}

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", ordinal = 2))
	private Block change(BlockState blockState) {
		if (shouldChange)
			return Blocks.BEDROCK;
		else
			return blockState.getBlock();
	}

	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean placeBubbleColumnIfPossible(World world, BlockPos pos, BlockState state, int flags) {
		if (state.getFluidState().getFluid() == Fluids.WATER) {
			BlockState downState = world.getBlockState(pos.down());
			if (downState.getBlock() == Blocks.BUBBLE_COLUMN)
				state = downState;
			else if (downState.getBlock() == Blocks.SOUL_SAND || downState.getBlock() == Blocks.MAGMA_BLOCK)
				state = Blocks.BUBBLE_COLUMN.getDefaultState().with(BubbleColumnBlock.DRAG, BubbleColumnBlock.calculateDrag(world, pos.down()));
		}

		return world.setBlockState(pos, state, flags);
	}
}

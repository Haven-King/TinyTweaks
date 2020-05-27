package dev.hephaestus.tweaks.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.FernBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShovelItem.class)
public class ShovelMixin {
	@Inject(method = "useOnBlock", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemUsageContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	public void dropSeeds(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world, BlockPos blockPos) {
		Block up = world.getBlockState(blockPos.up()).getBlock();
		if (up instanceof FernBlock || up instanceof TallPlantBlock || up instanceof FlowerBlock) {
			world.breakBlock(blockPos.up(), true, context.getPlayer());
		}
	}
}

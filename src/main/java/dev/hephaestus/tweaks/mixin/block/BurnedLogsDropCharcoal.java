package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FireBlock.class)
public class BurnedLogsDropCharcoal {
	@Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 1))
	private void beforeSet(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo ci) {
		if (TweaksConfig.Misc.BURNING_LOGS_DROP_CHARCOAL.getValue() && world.getBlockState(pos).getBlock().isIn(BlockTags.LOGS_THAT_BURN)) {
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.CHARCOAL)));
		}
	}
}

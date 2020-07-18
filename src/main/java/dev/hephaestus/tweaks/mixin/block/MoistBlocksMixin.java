package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.block.MoistBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Blocks.class)
public class MoistBlocksMixin {
	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/Block", ordinal = 9))
	private static Block moistenCobblestone(Block.Settings settings) {
			return new MoistBlock(settings);
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/Block", ordinal = 41))
	private static Block dryMossyCobblestone(Block.Settings settings) {
		return new MoistBlock(settings);
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/Block", ordinal = 48))
	private static Block moistenStoneBricks(Block.Settings settings) {
		return new MoistBlock(settings);
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/Block", ordinal = 49))
	private static Block dryStoneBricks(Block.Settings settings) {
		return new MoistBlock(settings);
	}
}

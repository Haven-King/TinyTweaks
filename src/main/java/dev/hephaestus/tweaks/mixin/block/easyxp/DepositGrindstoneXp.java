package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.PlayerProvider;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$4")
public abstract class DepositGrindstoneXp implements PlayerProvider {
	@Shadow protected abstract int getExperience(World world);

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "method_17417", at = @At("HEAD"), cancellable = true)
	private void depositXpToPlayer(World world, BlockPos blockPos, CallbackInfo ci) {
		if (TweaksConfig.Misc.EASY_XP.getValue() && this.getPlayer() instanceof ServerPlayerEntity) {
			XpUtil.addXp((ServerPlayerEntity) this.getPlayer(), this.getExperience(world));
			world.syncWorldEvent(1042, blockPos, 0);
			ci.cancel();
		}
	}
}


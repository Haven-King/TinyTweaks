package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityGiveXp {
	@Shadow protected PlayerEntity attackingPlayer;

	@Inject(method = "dropXp", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getCurrentExperience(Lnet/minecraft/entity/player/PlayerEntity;)I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void depositXpToPlayer(CallbackInfo ci, int i) {
		if (i > 0 && this.attackingPlayer != null && Tweaks.CONFIG.easyXp) {
			this.attackingPlayer.addExperience(i);
			ci.cancel();
		}
	}
}

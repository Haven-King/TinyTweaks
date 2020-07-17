package dev.hephaestus.tweaks.mixin.entity;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {WanderingTraderEntity.class, PiglinEntity.class, VillagerEntity.class})
public class InteractableEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void useFlintAndSteel(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand).getItem().equals(Items.FLINT_AND_STEEL)) {
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}

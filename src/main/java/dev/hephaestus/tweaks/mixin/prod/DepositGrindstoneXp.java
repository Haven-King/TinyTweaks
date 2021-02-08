package dev.hephaestus.tweaks.mixin.prod;

import dev.hephaestus.tweaks.util.PlayerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$4")
public abstract class DepositGrindstoneXp implements PlayerProvider {
	@Unique private final ThreadLocal<PlayerEntity> player = new ThreadLocal<>();

	@Inject(method = "method_7667", at = @At("HEAD"))
	private void captureArgs(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		this.player.set(player);
	}

	@Override
	public PlayerEntity getPlayer() {
		return this.player.get();
	}
}


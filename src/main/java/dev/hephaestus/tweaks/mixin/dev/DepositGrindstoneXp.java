package dev.hephaestus.tweaks.mixin.dev;

import dev.hephaestus.tweaks.Tweaks;
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
public abstract class DepositGrindstoneXp {
	@Shadow protected abstract int getExperience(World world);

	@Unique private final ThreadLocal<PlayerEntity> player = new ThreadLocal<>();

	@Inject(method = "onTakeItem", at = @At("HEAD"))
	private void captureArgs(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		this.player.set(player);
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "method_17417", at = @At("HEAD"), cancellable = true)
	private void depositXpToPlayer(World world, BlockPos blockPos, CallbackInfo ci) {
		if (Tweaks.CONFIG.easyXp && this.player.get() instanceof ServerPlayerEntity) {
			XpUtil.addXp((ServerPlayerEntity) this.player.get(), this.getExperience(world));
			world.syncWorldEvent(1042, blockPos, 0);
			ci.cancel();
		}
	}
}

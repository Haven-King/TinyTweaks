package dev.hephaestus.tweaks.mixin.prod;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
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

@Mixin(GrindstoneScreenHandler.class)
public abstract class DepositGrindstoneXp {
	@SuppressWarnings("UnresolvedMixinReference")
	@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$4")
	public static abstract class SlotMixin {
		@Shadow
		protected abstract int getExperience(World world);

		@Unique
		private final ThreadLocal<PlayerEntity> player = new ThreadLocal<>();

		@Inject(method = "method_7667", remap = false, at = @At("HEAD"))
		private void captureArgs(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
			this.player.set(player);
		}

		@Inject(method = "method_17417", at = @At("HEAD"), cancellable = true)
		private void depositXpToPlayer(World world, BlockPos blockPos, CallbackInfo ci) {
			if (Tweaks.CONFIG.easyXp && this.player.get() instanceof ServerPlayerEntity) {
				XpUtil.addXp((ServerPlayerEntity) this.player.get(), this.getExperience(world));
				world.syncWorldEvent(1042, blockPos, 0);
				ci.cancel();
			}
		}
	}
}

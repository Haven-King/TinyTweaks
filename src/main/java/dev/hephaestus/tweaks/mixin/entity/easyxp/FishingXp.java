package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingXp {
	@Shadow @Nullable public abstract PlayerEntity getPlayerOwner();

	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean depositXpToPlayer(World world, Entity entity) {
		if (this.getPlayerOwner() != null && Tweaks.CONFIG.easyXp) {
			this.getPlayerOwner().addExperience((int) (Math.random() * 6 + 1));
			return false;
		}

		return world.spawnEntity(entity);
	}
}

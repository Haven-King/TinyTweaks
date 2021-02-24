package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
		if (this.getPlayerOwner() != null && TweaksConfig.Misc.EASY_XP.getValue() && entity instanceof ExperienceOrbEntity) {
			XpUtil.addXp((ServerPlayerEntity) this.getPlayerOwner(), (int) (Math.random() * 6 + 1));
			return false;
		}

		return world.spawnEntity(entity);
	}
}

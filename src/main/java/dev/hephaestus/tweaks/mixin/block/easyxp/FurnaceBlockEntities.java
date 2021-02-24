package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceBlockEntities {
	@Redirect(method = "dropExperience(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Vec3d;IF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private static boolean depositXpToPlayer(World world, Entity entity) {
		ExperienceOrbEntity orb = (ExperienceOrbEntity) entity;
		ServerPlayerEntity player = (ServerPlayerEntity) world.getClosestPlayer(entity.getX(), entity.getY(), entity.getZ(), 0.1D, false);

		if (player != null && TweaksConfig.Misc.EASY_XP.getValue()) {
			XpUtil.addXp(player, orb.getExperienceAmount());
			return false;
		} else {
			return world.spawnEntity(entity);
		}
	}
}

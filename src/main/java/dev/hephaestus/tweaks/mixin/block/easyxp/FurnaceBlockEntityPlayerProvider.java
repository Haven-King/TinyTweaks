package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.block.PlayerProvider;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityPlayerProvider implements PlayerProvider {
	@Shadow private static void dropExperience(World world, Vec3d vec3d, int i, float f) {
		throw new RuntimeException();
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "method_17761", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;dropExperience(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Vec3d;IF)V"))
	private static void depositXpToPlayer(World world, Vec3d vec3d, int i, float f) {
		ServerPlayerEntity playerEntity = PlayerProvider.STATIC.get().getPlayer();

		if (playerEntity != null && Tweaks.CONFIG.easyXp) {
			XpUtil.addXp(playerEntity, i);
			PlayerProvider.STATIC.get().setPlayer(null);
		} else {
			dropExperience(world, vec3d, i, f);
		}
	}
}

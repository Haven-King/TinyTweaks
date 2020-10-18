package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityBreedXp extends LivingEntity {
	@Shadow @Nullable public abstract ServerPlayerEntity getLovingPlayer();

	protected AnimalEntityBreedXp(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "breed", at = @At(value = "NEW", target = "net/minecraft/entity/ExperienceOrbEntity"), cancellable = true)
	private void depositXpToPlayer(ServerWorld serverWorld, AnimalEntity other, CallbackInfo ci) {
		ServerPlayerEntity playerEntity = this.getLovingPlayer();
		if (playerEntity == null && other.getLovingPlayer() != null) {
			playerEntity = other.getLovingPlayer();
		}

		if (playerEntity != null && Tweaks.CONFIG.easyXp) {
			XpUtil.addXp(playerEntity, this.getRandom().nextInt(7) + 1);
			ci.cancel();
		}
	}
}

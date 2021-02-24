package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
		FoxEntity.MateGoal.class,
		TurtleEntity.MateGoal.class
})
public abstract class MateGoalXp extends AnimalMateGoal {
	public MateGoalXp(AnimalEntity animal, double chance) {
		super(animal, chance);
	}

	@Inject(method = "breed", at = @At(value = "NEW", target = "net/minecraft/entity/ExperienceOrbEntity"), cancellable = true)
	private void depositXpToPlayer(CallbackInfo ci) {
		ServerPlayerEntity playerEntity = this.animal.getLovingPlayer();

		if (playerEntity != null && TweaksConfig.Misc.EASY_XP.getValue()) {
			XpUtil.addXp(playerEntity, this.animal.getRandom().nextInt(7) + 1);
			ci.cancel();
		}
	}
}

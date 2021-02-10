package dev.hephaestus.tweaks.mixin.entity.passive;

import dev.hephaestus.tweaks.entity.ai.goal.EatCropsGoal;
import dev.hephaestus.tweaks.entity.ai.goal.GroundFoodMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Shadow @Final protected GoalSelector goalSelector;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;initGoals()V", shift = At.Shift.AFTER))
    private void initAnimalGoals(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        //noinspection ConstantConditions
        if (((Object) this) instanceof AnimalEntity) {
            int matePriority = -1;

            for (PrioritizedGoal goal : ((GoalSelectorAccessor) this.goalSelector).getGoals()) {
                if (goal.getGoal() instanceof AnimalMateGoal) {
                    matePriority = goal.getPriority();
                    break;
                }
            }

            if (matePriority > -1) {
                this.goalSelector.add(matePriority, new EatCropsGoal((AnimalEntity) (Object) this));
                this.goalSelector.add(matePriority, new GroundFoodMateGoal((AnimalEntity) (Object) this));
            }
        }
    }
}

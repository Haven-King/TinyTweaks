package dev.hephaestus.tweaks.mixin.entity.passive;

import dev.hephaestus.tweaks.entity.ai.goal.GroundFoodMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OcelotEntity.class)
public abstract class OcelotEntityMixin extends AnimalEntity {
    protected OcelotEntityMixin(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoal(CallbackInfo ci) {
        this.goalSelector.add(9, new GroundFoodMateGoal(this, this::isBreedingItem));
    }
}

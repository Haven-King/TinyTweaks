package dev.hephaestus.tweaks.mixin.entity.passive;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.entity.ai.goal.GroundFoodMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(WolfEntity.class)
public class WolfEntityMixin extends TameableEntity {
    protected WolfEntityMixin(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoal(CallbackInfo ci) {
        this.goalSelector.add(8, new GroundFoodMateGoal(this, this::isBreedingItem));
    }

    @Shadow
    @Nullable
    @Override
    public WolfEntity createChild(PassiveEntity mate) {
        return null;
    }

    @Redirect(method = "canBreedWith", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;isTamed()Z"))
    public boolean allowWildWolfBreeding(WolfEntity wolfEntity) {
        if (Tweaks.CONFIG.breedWildWolves)
            return true;
        else
            return wolfEntity.isTamed();
    }
}

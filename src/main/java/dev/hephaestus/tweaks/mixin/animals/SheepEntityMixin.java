package dev.hephaestus.tweaks.mixin.animals;

import dev.hephaestus.tweaks.goals.GroundFoodMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(SheepEntity.class)
public class SheepEntityMixin extends AnimalEntity {
    protected SheepEntityMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addGoal(CallbackInfo ci) {
        this.goalSelector.add(3, new GroundFoodMateGoal(this, Ingredient.ofItems(Items.WHEAT)));
    }

    @Shadow
    @Nullable
    @Override
    public SheepEntity createChild(PassiveEntity mate) {
        return null;
    }
}

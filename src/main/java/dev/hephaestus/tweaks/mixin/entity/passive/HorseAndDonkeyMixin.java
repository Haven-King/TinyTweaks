package dev.hephaestus.tweaks.mixin.entity.passive;

import dev.hephaestus.tweaks.entity.ai.goal.GroundFoodMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(HorseBaseEntity.class)
public class HorseAndDonkeyMixin extends AnimalEntity {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_CARROT);

    protected HorseAndDonkeyMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    protected void initGoals(CallbackInfo ci) {
        this.goalSelector.add(3, new GroundFoodMateGoal(this, BREEDING_INGREDIENT));
    }

    @Shadow
    @Nullable
    @Override
    public PassiveEntity createChild(PassiveEntity mate) {
        return null;
    }
}
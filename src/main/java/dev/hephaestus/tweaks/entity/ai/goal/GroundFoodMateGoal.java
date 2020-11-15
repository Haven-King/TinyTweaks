package dev.hephaestus.tweaks.entity.ai.goal;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.GameRules;

import java.util.EnumSet;
import java.util.List;

public class GroundFoodMateGoal extends Goal {
    private final AnimalEntity animal;
    private final FoodPredicate food;
    private ItemEntity foodEntity;

    public interface FoodPredicate {
        boolean eval(ItemStack item);
    }

    public GroundFoodMateGoal(AnimalEntity animal, FoodPredicate predicate) {
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.animal = animal;
        this.food = predicate;
    }

    public GroundFoodMateGoal(AnimalEntity animal, Ingredient food) {
        this(animal, food::test);
    }

    public boolean canStart() {
        if (Tweaks.CONFIG.animalsEatOffGround && this.animal.getEntityWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && this.animal.canEat() && this.animal.getBreedingAge() == 0) {
            this.foodEntity = this.findFood();
        }

        return this.foodEntity != null;
    }

    public boolean shouldContinue() {
        return this.foodEntity != null && this.foodEntity.getStack().getCount() > 0 && this.animal.canEat() && this.animal.getBreedingAge() == 0;
    }

    public void stop() {
        this.foodEntity = null;
    }

    public void tick() {
        if (this.foodEntity != null) {
            this.animal.getLookControl().lookAt(this.foodEntity, 10.0F, (float) this.animal.getLookPitchSpeed());
            this.animal.getNavigation().startMovingTo(this.foodEntity, 1.0f);
            if (this.animal.squaredDistanceTo(this.foodEntity) < 4.0D) {
                this.feed();
            }
        }
    }

    private ItemEntity findFood() {
        List<ItemEntity> list = animal.world.getEntitiesByClass(ItemEntity.class, this.animal.getBoundingBox().expand(8.0D), null);
        double d = Double.MAX_VALUE;

        ItemEntity result = null;
        for (ItemEntity itemEntity: list) {
            if (this.food.eval(itemEntity.getStack()) && this.animal.squaredDistanceTo(itemEntity) < d) {
                result = itemEntity;
                d = this.animal.squaredDistanceTo(itemEntity);
            }
        }

        return result;
    }

    private void feed() {
        if (this.foodEntity.getStack().getCount() > 0) {
            foodEntity.getStack().decrement(1);
            animal.lovePlayer(null);
        }

        stop();
    }
}

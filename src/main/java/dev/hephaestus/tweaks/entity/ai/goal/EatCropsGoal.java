package dev.hephaestus.tweaks.entity.ai.goal;

import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class EatCropsGoal extends Goal {
    private final AnimalEntity animal;
    private final World world;

    private int timer;

    public EatCropsGoal(AnimalEntity animal) {
        this.animal = animal;
        this.world = animal.world;

        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
    }

    @Override
    public boolean canStart() {
        return this.animal.canEat() && TweaksConfig.Animals.EAT_CROPS.getValue() && this.canEat() != null;
    }

    @Override
    public void start() {
        this.timer = 40;
        this.world.sendEntityStatus(this.animal, (byte)10);
        this.animal.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.timer = Math.max(0, this.timer - 1);
        if (this.timer == 4) {
            BlockPos blockPos = this.canEat();

            if (blockPos != null) {
                if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    this.world.breakBlock(blockPos, true);
                }

                this.animal.onEatingGrass();
            }
        }
    }

    private @Nullable BlockPos canEat() {
        BlockPos[] positions = new BlockPos[] {this.animal.getBlockPos(), this.animal.getBlockPos().up()};

        for (BlockPos pos : positions) {
            BlockState state = this.world.getBlockState(pos);

            LootContext.Builder lootContext = new LootContext.Builder((ServerWorld) this.world)
                    .parameter(LootContextParameters.TOOL, ItemStack.EMPTY)
                    .parameter(LootContextParameters.ORIGIN, this.animal.getPos());

            for (ItemStack stack : state.getDroppedStacks(lootContext)) {
                if (this.animal.isBreedingItem(stack)) {
                    return pos;
                }
            }
        }

        return null;
    }
}

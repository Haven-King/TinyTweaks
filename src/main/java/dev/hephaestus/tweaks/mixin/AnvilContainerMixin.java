package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.container.AnvilContainer;
import net.minecraft.container.Property;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilContainer.class)
public abstract class AnvilContainerMixin {
    @Shadow public static int getNextCost(int cost) {return 0;}

    private boolean itemNameChanged = false;

    @Inject(method = "updateResult", at = @At("HEAD"))
    private void resetItemNameChanged(CallbackInfo ci) {
        itemNameChanged = false;
    }
    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 1, ordinal = 10))
    private int getRemoveNameCost(int k) {
        itemNameChanged = true;
        return Tweaks.CONFIG.namesAndThings.freeRenames ? 0 : k;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 1, ordinal = 11))
    private int getSetNameCost(int k) {
        itemNameChanged = true;
        return Tweaks.CONFIG.namesAndThings.freeRenames ? 0 : k;
    }

    @ModifyVariable(method = "updateResult", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/container/Property;set(I)V", ordinal = 5, shift = At.Shift.AFTER))
    private int setI(int i) {
        return itemNameChanged && Tweaks.CONFIG.namesAndThings.freeRenames ? 1 : i;
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/container/AnvilContainer;getNextCost(I)I"))
    private int changeNextCost(int cost) {
        return Tweaks.CONFIG.namesAndThings.freeRenames ? 2 * cost : getNextCost(cost);
    }

    @Mixin(targets = "net/minecraft/container/AnvilContainer$2")
    public static class AnvilContainerSlotMixin {
        @Dynamic
        @Redirect(method = "canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/container/Property;get()I"))
        private int redirInt(Property property) {
            // This condition is dumb. Why EXPLICITLY disallow 0 cost recipes? Like if there's a stack there
            // it should work, right?
            return Integer.MAX_VALUE;
        }
    }
}

package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.block.Block;
import net.minecraft.block.LanternBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MakeLanternsBlastResistant {
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getBlastResistance", at = @At("HEAD"), cancellable = true)
    private void getBlastResistance(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof LanternBlock && TweaksConfig.Misc.STURDY_LANTERNS.getValue()) {
            cir.setReturnValue(6F);
        }
    }
}

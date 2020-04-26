package dev.hephaestus.tweaks.mixin;

import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatEntityRenderer.class)
public class BoatEntityRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
    public void translate(MatrixStack matrixStack, double x, double y, double z) {
        matrixStack.translate(x, 2 * y, z);
    }
}

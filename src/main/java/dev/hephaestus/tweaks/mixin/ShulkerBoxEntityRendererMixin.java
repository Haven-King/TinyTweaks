package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.client.BarrelBlockEntityRenderer;
import dev.hephaestus.tweaks.client.LabelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ShulkerBoxBlockEntityRenderer.class)
public abstract class ShulkerBoxEntityRendererMixin extends BlockEntityRenderer<ShulkerBoxBlockEntity> {
    public ShulkerBoxEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.AFTER))
    private void renderLabel(ShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, int j, CallbackInfo ci) {
        if (Tweaks.CONFIG.namesAndThings.containerLabels && shulkerBoxBlockEntity.hasWorld()) {
            LabelRenderer.renderLabel(shulkerBoxBlockEntity, dispatcher, matrices, vertexConsumers, shulkerBoxBlockEntity.getAnimationStage() != ShulkerBoxBlockEntity.AnimationStage.CLOSED, Direction.UP);
        }
    }
}

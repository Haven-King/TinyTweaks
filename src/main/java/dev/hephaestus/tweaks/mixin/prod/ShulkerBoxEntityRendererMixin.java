package dev.hephaestus.tweaks.mixin.prod;

import dev.hephaestus.tweaks.TweaksPreferences;
import dev.hephaestus.tweaks.client.render.block.entity.LabelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Dynamic;
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

    @Dynamic
    @Inject(method = "method_3569", at = @At("TAIL"))
    private void renderLabel(BlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, int j, CallbackInfo ci) {
        if (TweaksPreferences.Labels.ENABLED.getValue() && shulkerBoxBlockEntity.hasWorld()) {
            LabelRenderer.renderLabel(((ShulkerBoxBlockEntity)shulkerBoxBlockEntity), dispatcher, matrices, vertexConsumers, ((ShulkerBoxBlockEntity)shulkerBoxBlockEntity).getAnimationStage() != ShulkerBoxBlockEntity.AnimationStage.CLOSED, Direction.UP);
        }
    }
}

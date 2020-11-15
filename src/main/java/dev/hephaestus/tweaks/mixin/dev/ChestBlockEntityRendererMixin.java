package dev.hephaestus.tweaks.mixin.dev;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.client.render.block.entity.LabelRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestBlockEntityRendererMixin<T extends BlockEntity & ChestAnimationProgress> extends BlockEntityRenderer<T> {
    public ChestBlockEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    public void renderLabel(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (Tweaks.CONFIG.namesAndThings.containerLabels && blockEntity instanceof LockableContainerBlockEntity) {
            LabelRenderer.renderLabel((LockableContainerBlockEntity) blockEntity, dispatcher, matrices, vertexConsumers, tickDelta);
        }
    }
}
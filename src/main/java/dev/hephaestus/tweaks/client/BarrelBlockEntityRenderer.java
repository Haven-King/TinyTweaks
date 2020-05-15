package dev.hephaestus.tweaks.client;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class BarrelBlockEntityRenderer extends BlockEntityRenderer<BarrelBlockEntity> {
    public BarrelBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void render(BarrelBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (Tweaks.CONFIG.namesAndThings.containerLabels && blockEntity.hasWorld()) {
            LabelRenderer.renderLabel(blockEntity, dispatcher, matrices, vertexConsumers, blockEntity.getCachedState().get(BarrelBlock.OPEN), blockEntity.getCachedState().get(BarrelBlock.FACING));
        }
    }
}

package dev.hephaestus.tweaks.client.render.block.entity;

import dev.hephaestus.tweaks.TweaksPreferences;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class BarrelBlockLabelRenderer extends BlockEntityRenderer<BarrelBlockEntity> {
    public BarrelBlockLabelRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void render(BarrelBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (TweaksPreferences.Labels.ENABLED.getValue() && blockEntity.hasWorld()) {
            LabelRenderer.renderLabel(blockEntity, dispatcher, matrices, vertexConsumers, blockEntity.getCachedState().get(BarrelBlock.OPEN), blockEntity.getCachedState().get(BarrelBlock.FACING));
        }
    }
}

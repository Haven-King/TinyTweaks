package dev.hephaestus.tweaks.client;

import dev.hephaestus.tweaks.Tweaks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;

@Environment(EnvType.CLIENT)
public class LabelRenderer {
    private static final Vector3f yAxis = new Vector3f(0F, 1F, 0F);
    private static final Quaternion rotate90 = new Quaternion(yAxis, 90, true);
    private static final Quaternion rotate180 = new Quaternion(yAxis, 180, true);
    private static final Quaternion rotate270 = new Quaternion(yAxis, 270, true);

    public static void renderLabel(LockableContainerBlockEntity blockEntity, BlockEntityRenderDispatcher dispatcher, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean open, Direction facing) {
        if (!blockEntity.hasCustomName() && MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().getDataQueryHandler().queryBlockNbt(blockEntity.getPos(), (tag) -> {
                if (tag.contains("CustomName", 8)) {
                    blockEntity.setCustomName(Text.Serializer.fromJson(tag.getString("CustomName")));
                } else {
                    blockEntity.setCustomName(new LiteralText(""));
                }
            });
        } else if (blockEntity.hasWorld() && dispatcher.crosshairTarget.getType() == HitResult.Type.BLOCK && ((BlockHitResult) dispatcher.crosshairTarget).getBlockPos().equals(blockEntity.getPos())) {
            if (!open) {
                matrices.push();
                BlockPos top = blockEntity.getPos().up();
                boolean cameraIsAbove = dispatcher.camera.getPos().getY() > top.getY();

                @SuppressWarnings("ConstantConditions") double dy = blockEntity.getWorld().getBlockState(top).isAir() && cameraIsAbove ? 1.25D : 0.5D;
                double dz = dy == 0.5D? 0.6D : 0.0D;

                matrices.translate(0.5D, dy, 0.5D);

                if (facing == Direction.UP) {
                    if (dy == 1.25D)
                        matrices.multiply(new Quaternion(yAxis, -(dispatcher.camera.getYaw() - 180), true));
                    else if (dispatcher.crosshairTarget.getType() == HitResult.Type.BLOCK){
                        facing = ((BlockHitResult) dispatcher.crosshairTarget).getSide();
                    }
                }

                switch (facing) {
                    case EAST:
                        matrices.multiply(rotate90);
                        break;
                    case NORTH:
                        matrices.multiply(rotate180);
                        break;
                    case WEST:
                        matrices.multiply(rotate270);
                        break;
                    case DOWN:
                        matrices.pop();
                        return;
                    default:
                        break;
                }

                matrices.translate(0D, 0D, dz);

                float scale = 0.010416667F * (((float) Tweaks.CONFIG.namesAndThings.labelScale) / 100);
                matrices.scale(scale, -scale, scale);

                TextRenderer textRenderer = dispatcher.getTextRenderer();
                String string = blockEntity.getName().asString();

                float s = (float) (-textRenderer.getStringWidth(string) / 2);
                textRenderer.draw(string, s, 0f, 16777215, true, matrices.peek().getModel(), vertexConsumers, false, 0, 15728880);
                matrices.pop();
            }
        }
    }
}

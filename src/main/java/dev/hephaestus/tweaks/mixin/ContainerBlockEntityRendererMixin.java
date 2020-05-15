package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ContainerBlockEntityRendererMixin<T extends BlockEntity> extends BlockEntityRenderer<T> {
    public ContainerBlockEntityRendererMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    public void renderLabel(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (Tweaks.CONFIG.namesAndThings.containerLabels) {
            if (dispatcher.crosshairTarget.getType() == HitResult.Type.BLOCK && ((BlockHitResult) dispatcher.crosshairTarget).getBlockPos().equals(blockEntity.getPos()) && blockEntity instanceof ChestBlockEntity && MinecraftClient.getInstance().getNetworkHandler() != null) {
                if (!((LockableContainerBlockEntity) blockEntity).hasCustomName()) {
                    MinecraftClient.getInstance().getNetworkHandler().getDataQueryHandler().queryBlockNbt(blockEntity.getPos(), (tag) -> {
                        if (tag.contains("CustomName", 8)) {
                            ((LockableContainerBlockEntity) blockEntity).setCustomName(Text.Serializer.fromJson(tag.getString("CustomName")));
                        } else {
                            ((LockableContainerBlockEntity) blockEntity).setCustomName(new LiteralText(""));
                        }
                    });
                } else {
                    World world = blockEntity.getWorld();
                    boolean bl = world != null;

                    BlockState blockState = bl ? blockEntity.getCachedState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
                    ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
                    Block block = blockState.getBlock();

                    AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock<?>) block;

                    DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource2;
                    if (bl) {
                        propertySource2 = abstractChestBlock.getBlockEntitySource(blockState, world, blockEntity.getPos(), true);
                    } else {
                        propertySource2 = DoubleBlockProperties.PropertyRetriever::getFallback;
                    }

                    float g = propertySource2.apply(ChestBlock.getAnimationProgressRetriever((ChestAnimationProgress) blockEntity)).get(tickDelta);
                    g = 1.0F - g;
                    g = 1.0F - g * g * g;

                    if (g < 0.01F) {
                        matrices.push();
                        switch (chestType) {
                            case LEFT:
                                matrices.translate(0.0D, 1.25D, 0.5);
                                break;
                            case RIGHT:
                                matrices.translate(1.0D, 1.25D, 0.5);
                                break;
                            case SINGLE:
                                matrices.translate(0.5D, 1.25D, 0.5);
                                break;
                        }
                        matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);

                        TextRenderer textRenderer = dispatcher.getTextRenderer();
                        String string = ((LockableContainerBlockEntity) blockEntity).getName().asString();

                        float s = (float) (-textRenderer.getStringWidth(string) / 2);
                        textRenderer.draw(string, s, 0f, 16777215, true, matrices.peek().getModel(), vertexConsumers, false, 0, light);
                        matrices.pop();
                    }
                }
            }
        }
    }
}

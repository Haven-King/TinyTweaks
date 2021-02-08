package dev.hephaestus.tweaks.mixin.client;

import dev.hephaestus.tweaks.util.EntityProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class SeeThroughLeaves {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getVisualShape",at = @At("HEAD"), cancellable = true)
    private void makeLeavesNotSolid( BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (((EntityProvider) context).getEntity() instanceof PlayerEntity && this.getBlock().isIn(BlockTags.LEAVES) && MinecraftClient.getInstance().options.graphicsMode.getId() > 0) {
            cir.setReturnValue(VoxelShapes.empty());
        }
    }
}

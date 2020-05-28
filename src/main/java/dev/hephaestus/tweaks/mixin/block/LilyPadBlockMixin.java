package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LilyPadBlock.class)
public abstract class LilyPadBlockMixin extends PlantBlock {
    @Shadow @Final protected static VoxelShape SHAPE;

    protected LilyPadBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;)Z"), cancellable = true)
    public void overwriteEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (Tweaks.CONFIG.betterLilyPads)
            ci.cancel();
    }

    private static final VoxelShape COLLIDER = Block.createCuboidShape(1.0D, -0.155D, 1.0D, 15.0D, -1.5D, 15.0D);

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void overwriteOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (Tweaks.CONFIG.betterLilyPads)
            cir.setReturnValue(COLLIDER);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (Tweaks.CONFIG.betterLilyPads)
            return context.isAbove(COLLIDER, pos, false) ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
        else
            return super.getCollisionShape(state, view, pos, context);
    }
}

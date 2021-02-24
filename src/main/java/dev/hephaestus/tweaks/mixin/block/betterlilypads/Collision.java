package dev.hephaestus.tweaks.mixin.block.betterlilypads;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
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
public abstract class Collision extends PlantBlock {
    @Shadow @Final protected static VoxelShape SHAPE;

    protected Collision(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;breakBlock(Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/entity/Entity;)Z"), cancellable = true)
    public void overwriteEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (TweaksConfig.Plants.BETTER_LILY_PADS.getValue())
            ci.cancel();
    }

    private static final VoxelShape OUTLINES = Block.createCuboidShape(1D, -1.75D, 1D, 15D, -0.75D, 15D);
    private static final VoxelShape COLLIDER = Block.createCuboidShape(1.0D, -10D, 1.0D, 15.0D, 0, 15.0D);

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void overwriteOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (TweaksConfig.Plants.BETTER_LILY_PADS.getValue())
            cir.setReturnValue(OUTLINES);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (TweaksConfig.Plants.BETTER_LILY_PADS.getValue())
            return context.isAbove(COLLIDER, pos, false) ? COLLIDER : VoxelShapes.empty();
        else
            return super.getCollisionShape(state, view, pos, context);
    }
}

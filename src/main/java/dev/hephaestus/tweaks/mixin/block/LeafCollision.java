package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class LeafCollision {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context != EntityShapeContext.ABSENT && this.getBlock().isIn(BlockTags.LEAVES)) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock().getStateManager().getProperty("persistent") != null && state.get(Properties.PERSISTENT)) {
                cir.setReturnValue(Tweaks.CONFIG.leaves.persistentCollide ? VoxelShapes.fullCube() : VoxelShapes.empty());
            } else {
                cir.setReturnValue(Tweaks.CONFIG.leaves.collide ? VoxelShapes.fullCube() : VoxelShapes.empty());
            }
        }
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    private void onEntityCollision(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (this.getBlock().isIn(BlockTags.LEAVES)) {
            entity.fallDistance = entity.fallDistance * 0.7f;

            Vec3d velocity = entity.getVelocity();
            entity.setVelocity(velocity.multiply(0.7D, (velocity.y > 0 ? 1.0D : 0.7D), 0.7D));

            if (entity.getVelocity().length() > 0.1D) {
                entity.handleFallDamage(entity.fallDistance, 0.5f);
            }
        }
    }
}
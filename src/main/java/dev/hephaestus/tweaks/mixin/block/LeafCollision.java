package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeafCollision extends Block{
    public LeafCollision(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (context == EntityShapeContext.ABSENT) {
            return VoxelShapes.fullCube();
        }

        if (state.get(LeavesBlock.PERSISTENT)) {
            return Tweaks.CONFIG.leaves.persistentCollide ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
        } else {
            return Tweaks.CONFIG.leaves.collide ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (Tweaks.CONFIG.leaves.slow && !(entity instanceof ItemEntity)) {
            entity.setVelocity(entity.getVelocity().multiply(Tweaks.CONFIG.leaves.slowAmount));
        }
    }
}
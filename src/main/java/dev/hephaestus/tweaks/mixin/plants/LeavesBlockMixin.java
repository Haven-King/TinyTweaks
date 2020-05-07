package dev.hephaestus.tweaks.mixin.plants;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends Block{

    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        if (state.get(LeavesBlock.PERSISTENT))
            return Tweaks.CONFIG.leaves.persistentCollide ? super.getCollisionShape(state, view, pos, context) :  VoxelShapes.empty();
        else
            return Tweaks.CONFIG.leaves.collide ? super.getCollisionShape(state, view, pos, context) :  VoxelShapes.empty();
    }
}
package dev.hephaestus.tweaks.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LilyPadBlock.class)
public abstract class LilyPadBlockMixin extends PlantBlock {
    protected LilyPadBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author Haven King
     * @reason to stop boats from breaking our lily pads.
     * I highly doubt anyone else will mess with this aspect of lily pads, but if you DO, ~~fuck off~~ talk to me
     */
    @Overwrite
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
    }

    private static final VoxelShape COLLIDER = Block.createCuboidShape(1.0D, -0.125D, 1.0D, 15.0D, 0D, 15.0D);

    /**
     * @author Haven King
     * @reason Because it's the same as injecting and returning, but with more clarity.
     */
    @Overwrite
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return COLLIDER;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return context.isAbove(COLLIDER, pos, false) ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
    }
}

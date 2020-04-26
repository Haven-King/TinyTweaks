package dev.hephaestus.tweaks.mixin.plants;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FernBlock.class)
public class FernBlockMixin extends PlantBlock {
    protected FernBlockMixin(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        if (Tweaks.CONFIG.spreadableGroundcover)
            return VoxelShapes.empty();
        else
            return super.getOutlineShape(state, view, pos, context);
    }
}

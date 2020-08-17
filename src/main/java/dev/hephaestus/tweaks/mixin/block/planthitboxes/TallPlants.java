package dev.hephaestus.tweaks.mixin.block.planthitboxes;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TallPlantBlock.class)
public class TallPlants extends PlantBlock {
    protected TallPlants(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (!Tweaks.CONFIG.plantHitboxes && !context.isHolding(Items.SHEARS))
            return VoxelShapes.empty();
        else
            return super.getOutlineShape(state, view, pos, context);
    }
}

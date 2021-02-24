package dev.hephaestus.tweaks.mixin.block.planthitboxes;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksPreferences;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FernBlock.class)
public class FernBlocks extends PlantBlock {
    @Shadow @Final protected static VoxelShape SHAPE;

    protected FernBlocks(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (!TweaksPreferences.PLANT_HITBOXES.getValue() && !(context instanceof ItemConvertible && ((ItemConvertible) context).asItem().isIn(Tweaks.SHOWS_GRASS_HITBOXES))) {
            return VoxelShapes.empty();
        } else {
            return SHAPE;
        }
    }
}

package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin extends PlantBlock {
	@Shadow @Final protected static VoxelShape SHAPE;

	protected FlowerBlockMixin(Settings settings) {
		super(settings);
	}


	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		if (!Tweaks.CONFIG.plantHitboxes && !context.isHolding(Items.SHEARS))
			return VoxelShapes.empty();
		else
			return SHAPE;
	}
}
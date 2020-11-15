package dev.hephaestus.tweaks.mixin.block;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SignType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlock.class)
public class EditableSigns extends AbstractSignBlock {
	protected EditableSigns(Settings settings, SignType type) {
		super(settings, type);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && player != null) {
			player.openEditSignScreen((SignBlockEntity)world.getBlockEntity(pos));
			return ActionResult.SUCCESS;
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}
}

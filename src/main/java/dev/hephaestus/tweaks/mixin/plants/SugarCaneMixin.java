package dev.hephaestus.tweaks.mixin.plants;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public class SugarCaneMixin extends Block {
    public SugarCaneMixin(Settings settings) {
        super(settings);
    }

    private boolean tryBreakSugarcane(World world, BlockPos pos, PlayerEntity player) {
        if (world.getBlockState(pos).getBlock() == Blocks.SUGAR_CANE) {
            boolean above = tryBreakSugarcane(world, pos.up(), player);
            boolean us = false;
            if (world.getBlockState(pos.down()).getBlock() == Blocks.SUGAR_CANE)
                us = world.breakBlock(pos, true, player);

            return above || us;
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (Tweaks.CONFIG.easyHarvestSugarcane && tryBreakSugarcane(world, pos, player)) return ActionResult.SUCCESS;
        else return ActionResult.PASS;
    }
}

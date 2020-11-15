package dev.hephaestus.tweaks.mixin.block.easyharvest;

import dev.hephaestus.tweaks.Tweaks;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public class SugarCane extends Block {
    public SugarCane(Settings settings) {
        super(settings);
    }

    private int destroySugarcane(World world, BlockPos pos) {
        int r = 0;
        if (world.getBlockState(pos).getBlock() == Blocks.SUGAR_CANE) {
            r = 1 + destroySugarcane(world, pos.up());
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        return r;
    }

    private boolean tryBreakSugarcane(World world, BlockPos pos, PlayerEntity player) {
        int n;

        if (world.getBlockState(pos.down()).getBlock() != Blocks.SUGAR_CANE)
            n = destroySugarcane(world, pos.up());
        else {
            n = destroySugarcane(world, pos);
        }

        if (n > 0) {
            boolean playerAvailable = player.isAlive() || player instanceof ServerPlayerEntity && !((ServerPlayerEntity) player).isDisconnected();
            if (playerAvailable && !Tweaks.CONFIG.easyHarvestDropAsItems) {
                player.inventory.offerOrDrop(world, new ItemStack(Items.SUGAR_CANE, n));
            } else {
                Block.dropStack(world, pos, new ItemStack(Items.SUGAR_CANE, n));
            }
        }

        return n > 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (Tweaks.CONFIG.easyHarvestSugarcane && tryBreakSugarcane(world, pos, player)) return ActionResult.SUCCESS;
        else return ActionResult.PASS;
    }
}

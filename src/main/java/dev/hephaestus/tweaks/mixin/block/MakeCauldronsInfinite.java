package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.CauldronChunk;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public class MakeCauldronsInfinite {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void makeInfinite(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);

        CauldronChunk cauldronChunk = (CauldronChunk) world.getChunk(pos);

        if (TweaksConfig.Misc.INFINITE_CAULDRONS.getValue() && stack.getItem().equals(Items.HEART_OF_THE_SEA) && !cauldronChunk.isInfinite(pos)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(Properties.LEVEL_3, 3));
                cauldronChunk.setInfinite(pos, true);

                for (int int_1 = -2; int_1 <= 2; ++int_1) {
                    for (int int_2 = -2; int_2 <= 2; ++int_2) {
                        if (int_1 > -2 && int_1 < 2 && int_2 == -1) {
                            int_2 = 2;
                        }

                        for (int int_3 = 0; int_3 <= 1; ++int_3) {
                            ((ServerWorld) world).spawnParticles(
                                    ParticleTypes.ENCHANT,
                                    (double) pos.getX() + 0.5D,
                                    (double) pos.getY() + 2.0D,
                                    (double) pos.getZ() + 0.5D,
                                    1,
                                    0,0,0,
                                    1);
                        }
                    }
                }

                if (!player.isCreative()) {
                    stack.decrement(1);
                }

                world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, 1F);
                player.sendMessage(new TranslatableText("block.cauldron.makeInfinite").styled(style -> style.withItalic(true).withColor(Formatting.AQUA)), true);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }

    @Inject(method = "setLevel", at = @At("HEAD"), cancellable = true)
    private void dontDecreaseLevelIfInfinite(World world, BlockPos pos, BlockState state, int level, CallbackInfo ci) {
        CauldronChunk cauldronChunk = (CauldronChunk) world.getChunk(pos);
        if (level < state.get(Properties.LEVEL_3) && cauldronChunk.isInfinite(pos)) {
            ci.cancel();
        }
    }
}

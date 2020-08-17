package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(CauldronBlock.class)
public class MakeCauldronsInfinite extends Block {
    @Unique private static final BooleanProperty INFINITE = BooleanProperty.of("infinite");

    public MakeCauldronsInfinite(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addInfiniteState(Block.Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(INFINITE, false));
    }

    @Inject(method = "setLevel", at = @At("HEAD"), cancellable = true)
    private void dontDecreaseLevelIfInfinite(World world, BlockPos pos, BlockState state, int level, CallbackInfo ci) {
        if (level < state.get(Properties.LEVEL_3) && state.get(INFINITE)) {
            ci.cancel();
        }
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void addInfiniteState(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(INFINITE);
    }

    @Unique private static boolean LAST_CAULDRON_INFINITE = false;

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void makeInfinite(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        LAST_CAULDRON_INFINITE = false;
        ItemStack stack = player.getStackInHand(hand);

        if (Tweaks.CONFIG.infiniteCauldrons && stack.getItem().equals(Items.HEART_OF_THE_SEA) && !state.get(INFINITE)) {
            if (world.isClient) {
                for(int int_1 = -2; int_1 <= 2; ++int_1) {
                    for(int int_2 = -2; int_2 <= 2; ++int_2) {
                        if (int_1 > -2 && int_1 < 2 && int_2 == -1) {
                            int_2 = 2;
                        }

                        Random random = world.getRandom();
                        for(int int_3 = 0; int_3 <= 1; ++int_3) {
                            world.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)int_1 + random.nextFloat()) - 0.5D, (float)int_3 - random.nextFloat() - 1.0F, (double)((float)int_2 + random.nextFloat()) - 0.5D);
                        }
                    }
                }
            } else {
                world.setBlockState(pos, state.with(INFINITE, true).with(Properties.LEVEL_3, 3));

                if (!player.isCreative()) {
                    stack.decrement(1);
                }

                world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, 1F);
                player.sendMessage(new TranslatableText("block.cauldron.makeInfinite").styled(style -> style.withItalic(true).withColor(Formatting.AQUA)), true);
                LAST_CAULDRON_INFINITE = true;
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        } else if (!Tweaks.CONFIG.infiniteCauldrons && state.get(INFINITE) && !world.isClient) {
            world.setBlockState(pos, state.with(INFINITE, false));
            LAST_CAULDRON_INFINITE = false;
        } else {
            LAST_CAULDRON_INFINITE = state.get(INFINITE);
        }
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CauldronBlock;setLevel(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)V"), index = 2)
    private BlockState updateState(BlockState state) {
        return state.with(INFINITE, LAST_CAULDRON_INFINITE);
    }
}

package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    private static boolean isDirt(Block block) {
        return block == Blocks.DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.PODZOL || block == Blocks.COARSE_DIRT || block == Blocks.MYCELIUM;
    }

    private BlockPos triedToPlantAt;
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    public void plantSaplings(CallbackInfo ci) {
        if (Tweaks.CONFIG.saplingsAutoPlant && this.age > 20) {
            ItemStack stack = this.getStack();
            BlockPos pos = this.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if (this.getBlockPos() != triedToPlantAt && stack.getItem().isIn(ItemTags.SAPLINGS) && stack.getItem() instanceof BlockItem && state.getBlock() == Blocks.AIR && isDirt(world.getBlockState(pos.down()).getBlock())) {
                world.setBlockState(pos, ((BlockItem) stack.getItem()).getBlock().getDefaultState());
                stack.decrement(1);
                triedToPlantAt = this.getBlockPos();
            }
        }
    }
}

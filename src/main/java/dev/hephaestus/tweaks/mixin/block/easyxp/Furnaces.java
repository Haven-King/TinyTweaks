package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.XpBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(AbstractFurnaceBlock.class)
public abstract class Furnaces extends BlockWithEntity implements XpBlock {
    protected Furnaces(Settings settings) {
        super(settings);
    }

    @Redirect(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;method_27354(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Vec3d;)Ljava/util/List;"))
    private List<Recipe<?>> getPlayerLocation(AbstractFurnaceBlockEntity abstractFurnaceBlockEntity, World world, Vec3d vec3d) {
        if (TweaksConfig.Misc.EASY_XP.getValue()) {
            return null;
        } else {
            return abstractFurnaceBlockEntity.method_27354(world, vec3d);
        }
    }

    @Unique
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);

        ServerPlayerEntity player = this.getPlayer(pos);
        BlockEntity blockEntity = this.getBlockEntity(pos);

        if (player != null && blockEntity instanceof AbstractFurnaceBlockEntity && TweaksConfig.Misc.EASY_XP.getValue()) {
            ((AbstractFurnaceBlockEntity) blockEntity).method_27354(world, player.getPos());
        }
    }
}

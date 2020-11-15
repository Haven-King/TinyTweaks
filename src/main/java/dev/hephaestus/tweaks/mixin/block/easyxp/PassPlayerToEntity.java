package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.block.PlayerProvider;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlock.class)
public class PassPlayerToEntity {
	@Inject(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;method_27354(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Vec3d;)Ljava/util/List;"))
	private void setStaticPlayer(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
		PlayerProvider.init();
		PlayerProvider.STATIC.get().setPlayer(((PlayerProvider) state.getBlock()).getPlayer());
	}
}

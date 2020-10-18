package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.block.PlayerProvider;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class Blocks implements PlayerProvider {
	@Unique private final ThreadLocal<ServerPlayerEntity> playerEntity = new ThreadLocal<>();

	@Override
	public ServerPlayerEntity getPlayer() {
		return this.playerEntity.get();
	}

	@Override
	public void setPlayer(ServerPlayerEntity player) {
		this.playerEntity.set(player);
	}

	@Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V"))
	private static void assignPlayerToBlock(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity && Tweaks.CONFIG.easyXp) {
			((PlayerProvider) state.getBlock()).setPlayer((ServerPlayerEntity) entity);
		}
	}

	@Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
	protected void depositXpToPlayer(ServerWorld world, BlockPos pos, int size, CallbackInfo ci) {
		if (this.getPlayer() != null && Tweaks.CONFIG.easyXp) {
			XpUtil.addXp(this.getPlayer(), size);
			this.setPlayer(null);
			ci.cancel();
		}
	}
}

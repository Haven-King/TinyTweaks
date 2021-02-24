package dev.hephaestus.tweaks.mixin.block.easyxp;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.XpBlock;
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

import java.util.HashMap;
import java.util.Map;

@Mixin(Block.class)
public class Blocks implements XpBlock {
	@Unique private final ThreadLocal<Map<BlockPos, ServerPlayerEntity>> players = ThreadLocal.withInitial(HashMap::new);
	@Unique private final ThreadLocal<Map<BlockPos, BlockEntity>> blockEntities = ThreadLocal.withInitial(HashMap::new);

	@Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V"))
	private static void assignPlayerToBlock(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity && TweaksConfig.Misc.EASY_XP.getValue()) {
			((XpBlock) state.getBlock()).add(pos, (ServerPlayerEntity) entity);
		}

		if (blockEntity != null && TweaksConfig.Misc.EASY_XP.getValue()) {
			((XpBlock) state.getBlock()).add(pos, blockEntity);
		}
	}

	@Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
	protected void depositXpToPlayer(ServerWorld world, BlockPos pos, int size, CallbackInfo ci) {
		ServerPlayerEntity player = this.players.get().get(pos);

		if (player != null && TweaksConfig.Misc.EASY_XP.getValue()) {
			XpUtil.addXp(player, size);
			players.get().remove(pos);
			ci.cancel();
		}
	}

	@Override
	public void add(BlockPos pos, ServerPlayerEntity player) {
		this.players.get().put(pos, player);
	}

	@Override
	public void add(BlockPos pos, BlockEntity blockEntity) {
		this.blockEntities.get().put(pos, blockEntity);
	}

	@Override
	public ServerPlayerEntity getPlayer(BlockPos pos) {
		return this.players.get().get(pos);
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos) {
		return this.blockEntities.get().get(pos);
	}
}

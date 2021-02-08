package dev.hephaestus.tweaks.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface XpBlock {
    void add(BlockPos pos, ServerPlayerEntity player);
    void add(BlockPos pos, BlockEntity blockEntity);

    ServerPlayerEntity getPlayer(BlockPos pos);
    BlockEntity getBlockEntity(BlockPos pos);
}

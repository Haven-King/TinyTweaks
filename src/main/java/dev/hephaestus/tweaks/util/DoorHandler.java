package dev.hephaestus.tweaks.util;

import dev.hephaestus.tweaks.mixin.block.DoorBlockAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public interface DoorHandler {
    static void handle(ServerPlayerEntity player) {
        if (player.getMovementSpeed() > 0) {
            DoorHandler handler = (DoorHandler) player;
            BlockPos playerPos = player.getBlockPos();

            Collection<BlockPos> tracking = handler.tracking();

            tracking.removeIf(pos -> {
                if (horizontalDistance(pos, playerPos) > 2 || pos.getManhattanDistance(playerPos) > 2) {
                    BlockState state = player.world.getBlockState(pos);

                    if (state.getBlock() instanceof DoorBlock && state.get(DoorBlock.OPEN)) {
                        DoorBlockAccessor doorBlock = (DoorBlockAccessor) state.getBlock();

                        player.interactionManager.interactBlock(
                                player, player.getServerWorld(), player.getMainHandStack(), Hand.MAIN_HAND, new BlockHitResult(
                                        Vec3d.ofCenter(pos), Direction.UP, pos, true
                                )
                        );

                        player.world.syncWorldEvent(null, player.world.getBlockState(pos).get(DoorBlock.OPEN)
                                ? doorBlock.getCloseSound() : doorBlock.getOpenSound(), pos, 0);
                    }

                    return true;
                }

                return false;
            });

            for (BlockPos mut : BlockPos.iterate(playerPos.north().west(), playerPos.south().east())) {
                BlockState state = player.world.getBlockState(mut);

                if (!tracking.contains(mut) && state.isIn(BlockTags.WOODEN_DOORS) && state.getBlock() instanceof DoorBlock && !state.get(DoorBlock.OPEN)) {
                    Vec3d center = Vec3d.ofCenter(mut);

                    player.interactionManager.interactBlock(
                            player, player.getServerWorld(), player.getMainHandStack(), Hand.MAIN_HAND, new BlockHitResult(
                                    center, Direction.UP, mut, true
                            )
                    );

                    DoorBlockAccessor doorBlock = (DoorBlockAccessor) state.getBlock();

                    player.world.syncWorldEvent(null, state.get(DoorBlock.OPEN)
                            ? doorBlock.getCloseSound() : doorBlock.getOpenSound(), mut, 0);

                    BlockPos pos = state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER
                            ? mut
                            : mut.down();

                    handler.track(pos.toImmutable());
                } else if (state.isIn(BlockTags.WOODEN_DOORS) && state.getBlock() instanceof DoorBlock && state.get(DoorBlock.OPEN)) {
                    handler.track(playerPos.toImmutable());
                }
            }
        }
    }

    static int horizontalDistance(BlockPos pos1, BlockPos pos2) {
        return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getZ() - pos2.getZ());
    }

    /**
     * @param pos the position of the bottom block of the opened door.
     */
    void track(BlockPos pos);

    Collection<BlockPos> tracking();
}

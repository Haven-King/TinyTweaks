package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;

import dev.hephaestus.tweaks.block.LeaveBlockWaterLogGetter;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;

import net.minecraft.text.HoverEvent;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements LeaveBlockWaterLogGetter {

    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (context == EntityShapeContext.ABSENT) {
            return VoxelShapes.fullCube();
        }

        if (state.get(LeavesBlock.PERSISTENT)) {
            return Tweaks.CONFIG.leaves.persistentCollide ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
        } else {
            return Tweaks.CONFIG.leaves.collide ? super.getCollisionShape(state, view, pos, context) : VoxelShapes.empty();
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (Tweaks.CONFIG.leaves.slow && !(entity instanceof ItemEntity)) {
            entity.setVelocity(entity.getVelocity().multiply(Tweaks.CONFIG.leaves.slowAmount));
        }
    }

/*****************************************************Water Logging*****************************************************/

    private static final BooleanProperty WATER_LOGGED = Properties.WATERLOGGED;

    @Shadow @Final public static BooleanProperty PERSISTENT;

    //Reset the default state by getting the default state of leaves and add the water logged property and setting it to false.
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", cancellable = true)
    private void updateBlockProperties(Settings settings, CallbackInfo ci) {
        ((LeavesBlock) (Object) this).setDefaultState(((LeavesBlock) (Object) this).getDefaultState().with(WATER_LOGGED, false));
    }

    //Add water log property.
    @Inject(at = @At("RETURN"), method = "appendProperties(Lnet/minecraft/state/StateManager$Builder;)V")
    private void addWaterLogState(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(WATER_LOGGED);
    }

    //Check if the leaves are water logged and make them drip faster. Why? Because realism.
    @Inject(at = @At("HEAD"), method = "randomDisplayTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V")
    private void makeDripFasterWhenWaterLogged(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.get(WATER_LOGGED)) {
            if (random.nextInt(3) == 1) {
                double d0 = (float) pos.getX() + random.nextFloat();
                double d1 = (double) pos.getY() - 0.05D;
                double d2 = (float) pos.getZ() + random.nextFloat();
                world.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    //Check and waterlog leaves if the position is water.
    @Inject(at = @At("HEAD"), method = "getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;", cancellable = true)
    private void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        if (fluidState.getFluid() == Fluids.WATER)
            cir.setReturnValue(LeavesBlock.updateDistanceFromLogs(((LeavesBlock) (Object) this).getDefaultState().with(PERSISTENT, true), ctx.getWorld(), ctx.getBlockPos()).with(WATER_LOGGED, true));
    }

    //Every other block that water logs, ticks the block for fluids.
    @Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", cancellable = true)
    private void updatePostPlacement(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> cir) {
        if (state.get(WATER_LOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
    }

    //Use this to access the private static water log property in block.
    @Override
    public BooleanProperty getWaterLogProperty() {
        return WATER_LOGGED;
    }
}
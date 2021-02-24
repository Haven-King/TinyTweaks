package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.DoorHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashSet;

@Mixin(ServerPlayerEntity.class)
public abstract class AutoOpenDoors extends Entity implements DoorHandler {
    @Unique private final Collection<BlockPos> tracking = new HashSet<>();

    public AutoOpenDoors(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "playerTick", at = @At(value = "HEAD"))
    private void openDoors(CallbackInfo ci) {
        //noinspection ConstantConditions
        if (((Object) this) instanceof ServerPlayerEntity && TweaksConfig.Misc.AUTOMATIC_DOORS.getValue()) {
            ServerPlayerEntity player = ((ServerPlayerEntity) (Object) this);
            DoorHandler.handle(player);
        }
    }

    @Override
    public void track(BlockPos pos) {
        this.tracking.add(pos);
    }

    @Override
    public Collection<BlockPos> tracking() {
        return this.tracking;
    }
}

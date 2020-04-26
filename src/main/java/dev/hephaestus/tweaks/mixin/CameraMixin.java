package dev.hephaestus.tweaks.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private Entity focusedEntity;

    @Shadow private float cameraY;

    @Inject(method = "updateEyeHeight", at = @At("TAIL"))
    public void updateEyeHeight(CallbackInfo ci) {
        if (this.focusedEntity != null && focusedEntity.getVehicle() instanceof BoatEntity)
            this.cameraY += 0.35F;
    }
}

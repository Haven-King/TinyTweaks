package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowSize) {
        super(dispatcher, model, shadowSize);
    }

    @Inject(method = "getPositionOffset", at = @At("HEAD"), cancellable = true)
    public void moveUp(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, CallbackInfoReturnable<Vec3d> cir) {
        if (Tweaks.CONFIG.betterLilyPads && abstractClientPlayerEntity.getVehicle() instanceof BoatEntity) {
            cir.setReturnValue(super.getPositionOffset(abstractClientPlayerEntity, f).add(new Vec3d(0.0D, Tweaks.LILY_PAD_MOD, 0.0D)));
        }
    }

    @Shadow
    public Identifier getTexture(AbstractClientPlayerEntity entity) {
        return null;
    }
}

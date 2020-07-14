package dev.hephaestus.tweaks.mixin.client.render.entity;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.SoulFire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Unique private Entity renderedEntity;

	@Inject(method = "renderFire", at = @At("HEAD"))
	private void captureEntity(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
		this.renderedEntity = entity;
	}

	@Redirect(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;"))
	private Sprite renderSoulFire(SpriteIdentifier spriteIdentifier) {
		SoulFire.FireTypeModifier.of(renderedEntity).updateFireType();
		if (this.renderedEntity != null && Tweaks.CONFIG.blueSoulFireEffects && SoulFire.FireTypeModifier.of(this.renderedEntity).getFireType() == SoulFire.FireType.SOUL) {
			return SoulFire.getSprite();
		} else {
			return spriteIdentifier.getSprite();
		}
	}
}

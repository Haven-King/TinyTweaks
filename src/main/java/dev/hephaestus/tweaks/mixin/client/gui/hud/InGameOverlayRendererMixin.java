package dev.hephaestus.tweaks.mixin.client.gui.hud;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.SoulFire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
	@Redirect(method = "renderFireOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;"))
	private static Sprite getSoulFireSprite(SpriteIdentifier spriteIdentifier) {
		ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;

		if (playerEntity != null && Tweaks.CONFIG.blueSoulFireEffects) {
			SoulFire.FireTypeModifier.of(playerEntity).updateFireType();

			if (SoulFire.FireTypeModifier.of(playerEntity).getFireType() == SoulFire.FireType.SOUL) {
				return SoulFire.SPRITE_1.getSprite();
			}
		}

		return spriteIdentifier.getSprite();
	}
}

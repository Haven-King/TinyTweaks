package dev.hephaestus.tweaks.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulFire {
	public static final SpriteIdentifier SPRITE_0 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier("block/soul_fire_0"));
	public static final SpriteIdentifier SPRITE_1 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier("block/soul_fire_1"));

	public static Sprite getSprite() {
		return MinecraftClient.getInstance().isInSingleplayer() ? SPRITE_0.getSprite() : SPRITE_1.getSprite();
	}

	public enum FireType {
		NORMAL,
		SOUL
	}

	public interface FireTypeModifier {
		static FireTypeModifier of(Object object) {
			return (FireTypeModifier) object;
		}

		FireType getFireType();
		void updateFireType();
	}
}

package dev.hephaestus.tweaks.mixin.client.render.model;

import dev.hephaestus.tweaks.util.SoulFire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Inject(method = "method_24150(Ljava/util/HashSet;)V", at = @At("TAIL"))
	private static void addSoulFireToHashSet(HashSet<SpriteIdentifier> hashSet, CallbackInfo ci) {
		hashSet.add(SoulFire.SPRITE_0);
		hashSet.add(SoulFire.SPRITE_1);
	}
}

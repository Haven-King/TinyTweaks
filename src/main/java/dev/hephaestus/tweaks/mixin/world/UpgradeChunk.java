package dev.hephaestus.tweaks.mixin.world;

import dev.hephaestus.tweaks.util.CauldronChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(WorldChunk.class)
public abstract class UpgradeChunk implements CauldronChunk {
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ProtoChunk;)V", at = @At("TAIL"))
	private void copyCauldrons(World world, ProtoChunk protoChunk, CallbackInfo ci) {
		for (Map.Entry<BlockPos, Boolean> entry : ((CauldronChunk) protoChunk).getCauldrons()) {
			this.setInfinite(entry.getKey(), entry.getValue());
		}
	}
}

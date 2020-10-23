package dev.hephaestus.tweaks.mixin.world;

import dev.hephaestus.tweaks.util.CauldronChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.ReadOnlyChunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ChunkSerializer.class)
public class SerializeChunkData {
	@Inject(method = "serialize", at = @At("RETURN"))
	private static void serializeCauldrons(ServerWorld world, Chunk chunk, CallbackInfoReturnable<CompoundTag> cir) {
		if (chunk instanceof CauldronChunk && ((CauldronChunk) chunk).getCauldrons().size() > 0) {
			getOrCreate(cir.getReturnValue(), "TinyTweaks").put("InfiniteCauldrons", new LongArrayTag(
					((CauldronChunk) chunk).getCauldrons().stream()
							.filter(Map.Entry::getValue)
							.map(e -> e.getKey().asLong())
							.collect(Collectors.toList())));
		}
	}

	@Inject(method = "deserialize", at = @At("RETURN"))
	private static void deserializeCauldrons(ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir) {
		CauldronChunk chunk = (CauldronChunk) cir.getReturnValue();
		chunk = chunk instanceof ReadOnlyChunk ? (CauldronChunk) ((ReadOnlyChunk) chunk).getWrappedChunk() : chunk;

		for (Long l : tag.getCompound("TinyTweaks").getLongArray("InfiniteCauldrons")) {
			chunk.setInfinite(BlockPos.fromLong(l), true);
		}
	}

	private static CompoundTag getOrCreate(CompoundTag tag, String key) {
		return getOrCreate(tag, key, new CompoundTag());
	}

	@SuppressWarnings("unchecked")
	private static <T extends Tag> T getOrCreate(CompoundTag tag, String key, T newTag) {
		if (tag.contains(key, newTag.getType())) {
			return (T) tag.get(key);
		} else {
			tag.put(key, newTag);
			return newTag;
		}
	}
}

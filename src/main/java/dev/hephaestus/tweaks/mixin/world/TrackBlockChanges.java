package dev.hephaestus.tweaks.mixin.world;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.CauldronChunk;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = {WorldChunk.class, ProtoChunk.class})
public abstract class TrackBlockChanges implements BlockView, CauldronChunk {
	@Unique private final Map<BlockPos, Boolean> cauldrons = new HashMap<>();

	@Inject(method = "setBlockState", at = @At("HEAD"))
	private void captureStateChange(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
		if (this.getBlockState(pos).isOf(Blocks.CAULDRON) && !state.getBlock().is(Blocks.CAULDRON)) {
			cauldrons.remove(pos);
		}
	}

	@Override
	public Collection<Map.Entry<BlockPos, Boolean>> getCauldrons() {
		return this.cauldrons.entrySet();
	}

	@Override
	public boolean isInfinite(BlockPos pos) {
		return Tweaks.CONFIG.infiniteCauldrons && this.cauldrons.getOrDefault(pos, false);
	}

	@Override
	public void setInfinite(BlockPos pos, boolean infinite) {
		this.cauldrons.put(pos, infinite);
	}
}

package dev.hephaestus.tweaks.util;

import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Map;

public interface CauldronChunk {
	boolean isInfinite(BlockPos pos);
	void setInfinite(BlockPos pos, boolean infinite);
	Collection<Map.Entry<BlockPos, Boolean>> getCauldrons();
}

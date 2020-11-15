package dev.hephaestus.tweaks.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class Moistener {
	private static final BiMap<Block, Block> CONVERTIBLE = HashBiMap.create();

	public static BlockState moisten(BlockState blockState) {
		Block block = blockState.getBlock();
		Block newBlock = CONVERTIBLE.getOrDefault(blockState.getBlock(), blockState.getBlock());

		return newBlock.getStateManager().getStates().get(block.getStateManager().getStates().indexOf(blockState));
	}

	public static BlockState dry(BlockState blockState) {
		Block block = blockState.getBlock();
		Block newBlock = CONVERTIBLE.inverse().getOrDefault(blockState.getBlock(), blockState.getBlock());

		return newBlock.getStateManager().getStates().get(block.getStateManager().getStates().indexOf(blockState));
	}

	public static void canMoisten(Block in, Block out) {
		CONVERTIBLE.put(in, out);
	}

	public static boolean canMoisten(Block block) {
		return CONVERTIBLE.containsKey(block) || CONVERTIBLE.inverse().containsKey(block);
	}
}

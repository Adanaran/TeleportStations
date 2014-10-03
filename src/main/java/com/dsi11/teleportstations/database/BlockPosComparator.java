package com.dsi11.teleportstations.database;

import java.util.Comparator;

import net.minecraft.util.BlockPos;

/**
 * A comparator for ChunkCoordinates.
 * <p>
 * Used by the database for sorting purposes.
 * 
 * @author Adanaran
 */
public class BlockPosComparator implements Comparator<BlockPos> {

	/**
	 * Creates a new Comparator.
	 */
	public BlockPosComparator() {
	}

	@Override
	public int compare(BlockPos arg0, BlockPos arg1) {
		return arg0.compareTo(arg1);
	}

}

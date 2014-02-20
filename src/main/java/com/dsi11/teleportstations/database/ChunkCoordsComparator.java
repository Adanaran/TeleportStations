package com.dsi11.teleportstations.database;

import java.util.Comparator;

import net.minecraft.util.ChunkCoordinates;

/**
 * A comparator for ChunkCoordinates.
 * <p>
 * Used by the database for sorting purposes.
 * 
 * @author Adanaran
 */
public class ChunkCoordsComparator implements Comparator<ChunkCoordinates> {

	/**
	 * Creates a new Comparator.
	 */
	public ChunkCoordsComparator() {
	}

	@Override
	public int compare(ChunkCoordinates arg0, ChunkCoordinates arg1) {
		return arg0.compareTo(arg1);
	}

}

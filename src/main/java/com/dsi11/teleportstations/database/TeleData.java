package com.dsi11.teleportstations.database;

import net.minecraft.util.ChunkCoordinates;

/**
 * TeleData is a dataset storing all required data for one teleporter.
 * <p>
 * Used by {@link TPDatabase} for internal saving.
 * 
 * @author Adanaran
 * 
 */
public class TeleData extends ChunkCoordinates {

	private String name;
	private ChunkCoordinates ziel;
	private int meta, worldType;

	/**
	 * Constructor for a new TeleData object without target.
	 * <p>
	 * Stores initial values.
	 * 
	 * @param name
	 *            String name of teleporter
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 * @param meta
	 *            int meta-type of teleporter
	 * @param worldType
	 *            int normal or nether world?
	 */
	public TeleData(String name, int i, int j, int k, int meta, int worldType) {
		super(i, j, k);
		this.name = name;
		this.ziel = null;
		this.meta = meta;
		this.worldType = worldType;
	}

	/**
	 * Constructor for a new TeleData object with target.
	 * <p>
	 * Stores initial values.
	 * 
	 * @param name
	 *            String name of teleporter
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 * @param meta
	 *            int meta-type of teleporter
	 * @param worldType
	 *            int normal or nether world?
	 * @param ziel
	 *            {@link ChunkCoordinates} of the telporter's target
	 */
	public TeleData(String name, int i, int j, int k, int meta, int worldType,
			ChunkCoordinates ziel) {
		super(i, j, k);
		this.name = name;
		this.ziel = ziel;
		this.meta = meta;
		this.worldType = worldType;
	}

	/**
	 * Constructor for a new TeleData object converted from a String.
	 * <p>
	 * Stores initial values.
	 * 
	 * @param line
	 *            String the TeleData.toString of this new teleporter
	 */
	public TeleData(String line) {
		String[] tdString = line.split(";;");
		try {
			this.name = tdString[0];
			this.posX = Integer.parseInt(tdString[1]);
			this.posY = Integer.parseInt(tdString[2]);
			this.posZ = Integer.parseInt(tdString[3]);
			this.meta = Integer.parseInt(tdString[4]);
			this.worldType = Integer.parseInt(tdString[5]);
			if (tdString.length == 9) {
				this.ziel = new ChunkCoordinates(Integer.parseInt(tdString[6]),
						Integer.parseInt(tdString[7]),
						Integer.parseInt(tdString[8]));
			} else if (tdString.length == 7) {
				this.ziel = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the teleporter's target.
	 * 
	 * @return {@link ChunkCoordinates} current target's coordinates
	 */
	public ChunkCoordinates getZiel() {
		return ziel;
	}

	/**
	 * Sets the teleporter's target.
	 * 
	 * @param ziel
	 *            {@link ChunkCoordinates} target coordinates
	 * @return
	 */
	public TeleData setZiel(ChunkCoordinates ziel) {
		this.ziel = ziel;
		return this;
	}

	/**
	 * Gets the teleporters meta value.
	 * 
	 * @return int meta-type of teleporter.
	 */
	public int getMeta() {
		return meta;
	}

	/**
	 * Sets the teleporter's meta value.
	 * 
	 * @param meta
	 *            int teleporter's new meta
	 */
	public TeleData setMeta(int meta) {
		this.meta = meta;
		return this;
	}

	/**
	 * Gets the teleporter's name.
	 * 
	 * @return String name of this teleporter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this teleporter.
	 * 
	 * @param name
	 *            String name of the teleporter
	 * @return
	 */
	public TeleData setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Gets the world type of this teleporter.
	 * 
	 * @return int 0 normal world, -1 nether worlds
	 */
	public int getWorldType() {
		return worldType;
	}

	/**
	 * Converts this TeleData object into a String.
	 * <p>
	 * Overriding object's toString()-method.
	 * 
	 * @return String name$$i$$j$$k$$meta$$null oder
	 *         name$$i$$j$$k$$meta$$ziel.i$$ziel.j$$ziel.k
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(";;").append(posX).append(";;")
				.append(posY).append(";;").append(posZ).append(";;")
				.append(meta).append(";;").append(worldType).append(";;");
		if (ziel != null) {
			builder.append(ziel.posX).append(";;").append(ziel.posY)
					.append(";;").append(ziel.posZ);
		} else {
			builder.append("null");
		}
		return builder.toString();
	}
}

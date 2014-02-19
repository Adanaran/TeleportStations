package com.dsi11.teleportstations.blocks;

import com.dsi11.teleportstations.TeleportStations;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 * Middle block of teleporter.
 * <p>
 * This block doesn't do anything, it's just a blocker to prevent other blocks
 * being placed in a teleporter.
 * 
 * @author Demitreus
 * 
 */
public class BlockTeleMid extends Block {

	/**
	 * Constuctor of middle block of teleporter.
	 * 
	 * @param par1
	 *            int ID
	 */
	public BlockTeleMid() {
		super(Material.portal);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return ((world.getBlock(i, j - 1, k) == TeleportStations.blockTeleporter || world
				.getBlock(i, j - 1, k) == TeleportStations.blockTeleporterAn));
	}

}

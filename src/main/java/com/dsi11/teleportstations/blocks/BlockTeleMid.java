package com.dsi11.teleportstations.blocks;

import com.dsi11.teleportstations.TeleportStations;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
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
	 */
	public BlockTeleMid() {
		super(Material.portal);
	}

	/*@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		//do nothing -- no Icon
	}*/

	/*@Override
	public IIcon getIcon(IBlockAccess IBA, int i,
			int j, int k, int meta) {
		return Blocks.air.getIcon(IBA, i, j, k, meta);
	}*/

	/*@Override
	public IIcon getIcon(int side, int meta) {
		return Blocks.air.getIcon(side, meta);
	}*/

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/*@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return ((world.getBlock(i, j - 1, k) == TeleportStations.blockTeleporter || world
				.getBlock(i, j - 1, k) == TeleportStations.blockTeleporterAn));
	}*/

}

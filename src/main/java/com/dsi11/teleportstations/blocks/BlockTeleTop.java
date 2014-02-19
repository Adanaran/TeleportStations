package com.dsi11.teleportstations.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;

/**
 * Top-block of a teleporter containing a {@link TitleEntityTele}.
 * <p>
 * On this block's side the name and target of the teleporter will be rendered.
 * 
 * @author Demitreus
 */
public class BlockTeleTop extends BlockContainer {

	/**
	 * Constructs the BlockContainer BlockTeleTop.
	 */
	public BlockTeleTop() {
		super(Material.portal);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	// TODO Textures
	// @Override
	// public String getTextureFile() {
	// return "/adanaran/mods/ts/textures/TS.png";
	// }

	// TODO Textures
	// @Override
	// public int getBlockTextureFromSide(int par1) {
	// return par1 > 1 ? 35 : 33 + par1;
	// }

	@Override
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
			int par4, int par5) {
		BlockTeleTarget.deleteTP(par1World, par2, par3 - 2, par4);
	}

	// TODO onBlockDestroyedByExplosion
	// @Override
	// public void onBlockDestroyedByExplosion(World par1World, int par2,
	// int par3, int par4) {
	// BlockTeleTarget.deleteTP(par1World, par2, par3 - 2, par4);
	// super.onBlockDestroyedByExplosion(par1World, par2, par3, par4);
	// }

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return par1World.getBlock(par2, par3 - 1, par4) == TeleportStations.blockTeleMid;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityTeleporter();
	}
}
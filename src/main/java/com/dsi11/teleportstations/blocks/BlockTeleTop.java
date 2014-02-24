package com.dsi11.teleportstations.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Top-block of a teleporter containing a {@link TitleEntityTele}.
 * <p>
 * On this block's side the name and target of the teleporter will be rendered.
 * 
 * @author Demitreus
 */
public class BlockTeleTop extends BlockContainer {
	
	/**
	 * Icon of the block
	 */
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockSide;
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockTop;
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockTopButtom;
	
	/**
	 * Constructs the BlockContainer BlockTeleTop.
	 */
	public BlockTeleTop() {
		super(Material.portal);
	}

	

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister IIR) {
		iconTeleBlockTop = IIR
				.registerIcon("teleportstations:TeleporterTopOben");
		iconTeleBlockTopButtom = IIR
				.registerIcon("teleportstations:TeleporterTopUnten");
		iconTeleBlockSide = IIR
				.registerIcon("teleportstations:TeleporterTopSeite");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {

		switch (side) {
		case 0:
			return iconTeleBlockTopButtom;
		case 1:
			return iconTeleBlockTop;
		default:
			return iconTeleBlockSide;
		}
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
			int par4, int par5) {
		BlockTeleTarget.deleteTP(par1World, par2, par3 - 2, par4);
	}

	@Override
	public void onBlockDestroyedByExplosion(World par1World, int par2,
			int par3, int par4, Explosion explosion) {
		BlockTeleTarget.deleteTP(par1World, par2, par3 - 2, par4);
		super.onBlockDestroyedByExplosion(par1World, par2, par3, par4,
				explosion);
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return par1World.getBlock(par2, par3 - 1, par4) == TeleportStations.blockTeleMid;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityTeleporter();
	}
}
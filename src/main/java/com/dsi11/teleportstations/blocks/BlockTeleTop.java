package com.dsi11.teleportstations.blocks;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Top-block of a teleporter.
 * <p/>
 * On this block's side the name and target of the teleporter will be rendered.
 *
 * @author Demitreus
 */
public class BlockTeleTop extends BlockContainer {

    /**
     * Icon of the block
     */
    /*@SideOnly(Side.CLIENT)
    protected IIcon iconTeleBlockSide;
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockTop;
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockTopButtom;*/

    /**
     * Constructs the BlockContainer BlockTeleTop.
     */
    public BlockTeleTop() {
        super(Material.portal);
    }

	/*@SideOnly(Side.CLIENT)
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
	}*/

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        BlockTeleTarget.deleteTP(worldIn, pos.getX(), pos.getY() - 2, pos.getZ());
    }

    @Override
    public void onBlockDestroyedByExplosion(World par1World, BlockPos pos, Explosion explosion) {
        BlockTeleTarget.deleteTP(par1World, pos.getX(), pos.getY() - 2, pos.getZ());
        super.onBlockDestroyedByExplosion(par1World, pos, explosion);
    }

    /*@Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
        return par1World.getBlock(par2, par3 - 1, par4) == TeleportStations.blockTeleMid;
    }*/

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporter();
    }
}
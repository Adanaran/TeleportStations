package com.dsi11.teleportstations.blocks;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Block for teleportation.
 * <p/>
 * Can teleport the player and can also act as target.
 *
 * @author Demitreus
 */
public class BlockTeleporter extends BlockTeleTarget {

    private boolean isActive;

    /**
     * Constructor.
     */
    public BlockTeleporter(boolean isActive) {
        super();
        if(isActive) {
            this.setBlockUnbreakable();
            this.setUnlocalizedName("TeleporterAn");
        }else {
            this.setUnlocalizedName("Teleporter");
        }
    }

	/*@SideOnly(Side.CLIENT)
    @Override
	public void registerBlockIcons(IIconRegister IIR) {
		super.registerBlockIcons(IIR);
		IIcon[] iconTeleBlockTopActive = TeleportStations.proxy.iconTeleBlockTopActive;
		iconTeleBlockTopActive[0] = IIR
				.registerIcon("teleportstations:TeleporterAn");
		iconTeleBlockTopActive[1] = IIR
				.registerIcon("teleportstations:TeleAnS");
		iconTeleBlockTopActive[2] = IIR
				.registerIcon("teleportstations:TeleAnO");
		iconTeleBlockTopActive[3] = IIR
				.registerIcon("teleportstations:TeleAnN");
		iconTeleBlockTopActive[4] = IIR
				.registerIcon("teleportstations:TeleAnW");
		iconTeleBlockTopActive[5] = IIR
				.registerIcon("teleportstations:TeleAn2OS");
		iconTeleBlockTopActive[6] = IIR
				.registerIcon("teleportstations:TeleAn2WS");
		iconTeleBlockTopActive[7] = IIR
				.registerIcon("teleportstations:TeleAn2WN");
		iconTeleBlockTopActive[8] = IIR
				.registerIcon("teleportstations:TeleAn2ON");
		iconTeleBlockTopActive[9] = IIR
				.registerIcon("teleportstations:TeleAn3S");
		iconTeleBlockTopActive[10] = IIR
				.registerIcon("teleportstations:TeleAn3W");
		iconTeleBlockTopActive[11] = IIR
				.registerIcon("teleportstations:TeleAn3N");
		iconTeleBlockTopActive[12] = IIR
				.registerIcon("teleportstations:TeleAn3O");
		iconTeleBlockTopActive[13] = IIR
				.registerIcon("teleportstations:TeleAn4");
		iconTeleBlockTopActive[14] = IIR
				.registerIcon("teleportstations:TeleAn2NS");
		iconTeleBlockTopActive[15] = IIR
				.registerIcon("teleportstations:TeleAn2OW");
	}*/

	/*@SideOnly(Side.CLIENT)
    @Override
	public IIcon getIcon(int side, int meta) {

		if (side != 1) {
			return iconTeleBlockSide;
		} else if (this.equals(TeleportStations.blockTeleporterAn)) {
			return TeleportStations.proxy.iconTeleBlockTopActive[meta];
		} else {
			return TeleportStations.proxy.iconTeleBlockTop[meta];
		}
	}*/

    @Override
    public int update(World world, int i, int j, int k) {
        if (power(world, i, j, k)) {
            world.setBlockState(new BlockPos(i, j, k), TeleportStations.blockTeleporterAn.getDefaultState());
        } else {
            world.setBlockState(new BlockPos(i, j, k), TeleportStations.blockTeleporter.getDefaultState());
        }
        return super.update(world, i, j, k);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (TeleportStations.db.isTPInDatabase(pos.getX(), pos.getY(), pos.getZ())) {
            playerIn.openGui(TeleportStations.instance, 1, worldIn,
                    pos.getX(), pos.getY(), pos.getZ());
        } else {
            playerIn.openGui(TeleportStations.instance, 0, worldIn,
                    pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos,
                                          Entity entity) {
        int meta = 0; //TODO  world.getBlockMetadata(i, j, k);
        if (power(world, pos.getX(), pos.getY(), pos.getZ())) {
            if (meta == 0 && entity instanceof EntityPlayer) {
                teleportPlayer((EntityPlayer) entity, world, pos.getX(), pos.getY(), pos.getZ());
            } else if (meta > 0 && entity instanceof EntityMinecart) {
                handleMC(world, (EntityMinecart) entity, pos.getX(), pos.getY(), pos.getZ());
            }
        } else {
            super.onEntityCollidedWithBlock(world, pos, entity);
        }
    }

    private void teleportPlayer(EntityPlayer entity, World world, int i, int j,
                                int k) {
        TeleData quelle = TeleportStations.db.getTeleDataByCoords(i, j, k);
        BlockPos ziel = quelle.getTarget();
        TileEntity te = world.getTileEntity(new BlockPos(i, j + 2, k));
        if (te != null
                && te instanceof TileEntityTeleporter
                && (TeleportStations.proxy.isServer() || TeleportStations.proxy
                .isSinglePlayer())) {
            TileEntityTeleporter tet = (TileEntityTeleporter) te;
            if (tet.getTarget() != null) {
                tet.tp(entity);
            }
        }
    }
}

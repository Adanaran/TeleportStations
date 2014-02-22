package com.dsi11.teleportstations.blocks;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.entities.TileEntityTeleTarget;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockRail;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Teleporter-target-block represents valid destination for teleporting.
 * <p>
 * This block doesn't require diamonds to craft, but also can't teleport the
 * player.
 * 
 * @author Demitreus
 * 
 */
public class BlockTeleTarget extends BlockContainer {

	/**
	 * Icon of the block
	 */
	@SideOnly(Side.CLIENT)
	protected IIcon iconTeleBlockSide;
	@SideOnly(Side.CLIENT)
	protected IIcon[] iconTeleBlockTop = new IIcon[16];

	
	/**
	 * Constructor of teleportertarget-Blocks.
	 */
	public BlockTeleTarget() {
		super(Material.iron);
		this.setBlockBounds(0.0f, 0f, 0.0f, 1.0f, 0.125f, 1.0f);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister IIR) {
		iconTeleBlockTop[0] = IIR.registerIcon("teleportstations:TeleporterAus");
		iconTeleBlockTop[1] = IIR.registerIcon("teleportstations:TeleAusS");
		iconTeleBlockTop[2] = IIR.registerIcon("teleportstations:TeleAusO");
		iconTeleBlockTop[3] = IIR.registerIcon("teleportstations:TeleAusN");
		iconTeleBlockTop[4] = IIR.registerIcon("teleportstations:TeleAusW");
		iconTeleBlockTop[5] = IIR.registerIcon("teleportstations:TeleAus2OS");
		iconTeleBlockTop[6] = IIR.registerIcon("teleportstations:TeleAus2WS");
		iconTeleBlockTop[7] = IIR.registerIcon("teleportstations:TeleAus2WN");
		iconTeleBlockTop[8] = IIR.registerIcon("teleportstations:TeleAus2ON");
		iconTeleBlockTop[9] = IIR.registerIcon("teleportstations:TeleAus3S");
		iconTeleBlockTop[10] = IIR.registerIcon("teleportstations:TeleAus3W");
		iconTeleBlockTop[11] = IIR.registerIcon("teleportstations:TeleAus3N");
		iconTeleBlockTop[12] = IIR.registerIcon("teleportstations:TeleAus3O");
		iconTeleBlockTop[13] = IIR.registerIcon("teleportstations:TeleAus4");
		iconTeleBlockTop[14] = IIR.registerIcon("teleportstations:TeleAus2NS");
		iconTeleBlockTop[15] = IIR.registerIcon("teleportstations:TeleAus2OW");
		iconTeleBlockSide = IIR.registerIcon("teleportstations:TeleporterSeite");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {

		if(side != 1){
			return iconTeleBlockSide;	
		}
		return iconTeleBlockTop[meta];
	}

	@Override
	public boolean isOpaqueCube() {
		
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return null;
	}

	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}


	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k,
			Block blockChanged) {
		update(world, i, j, k);
		if (!this.canBlockStay(world, i, j, k)) {
			deleteTP(world, i, j, k);
		}
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return world.getBlock(i, j - 1, k).isBlockNormalCube();
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return canBlockStay(world, i, j, k);
	}

	@Override
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
			int par4, int par5) {
		deleteTP(par1World, par2, par3, par4);
		super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);

	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int i, int j, int k,
			Explosion explosion) {
		deleteTP(world, i, j, k);
		super.onBlockDestroyedByExplosion(world, i, j, k, explosion);
	}

	/**
	 * Deletes a whole teleporter with it's data.
	 * <p>
	 * The dleted Teleporter is removed from the database and all 3 blocks are
	 * filled with air.
	 * 
	 * @param world
	 *            World world
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 */
	protected static void deleteTP(World world, int i, int j, int k) {
		TeleportStations.db.removeTP(i, j, k);
		world.setBlock(i, j + 1, k, Blocks.air, 0, 2);
		world.setBlock(i, j + 2, k, Blocks.air, 0, 2);
		world.setBlock(i, j, k, Blocks.air, 0, 2);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int i, int j, int k,
			EntityLivingBase player, ItemStack itemStack) {
		par1World.setBlock(i, j + 1, k, TeleportStations.blockTeleMid, 0, 2);
		par1World.setBlock(i, j + 2, k, TeleportStations.blockTeleTop, 0, 2);
		if (player instanceof EntityPlayer) {
			((EntityPlayer) player).openGui(TeleportStations.instance, 0,
					par1World, i, j, k);
		}
		update(par1World, i, j, k);
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3,
			int par4, Entity par5Entity) {
		if (par5Entity instanceof EntityMinecart) {
			handleMC(par1World, (EntityMinecart) par5Entity, par2, par3, par4);
		}
	}

	/**
	 * Updates the metadata of a teleporter.
	 * <p>
	 * The new metadate depends on the number and orientation of neighbor blocks
	 * if they are rails. The block is replaced with the new meta.
	 * 
	 * @param world
	 *            World world
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 * @return int new meta value
	 */
	public int update(World world, int i, int j, int k) {

		int meta = 0;
		Block ostId = world.getBlock(i + 1, j, k);
		Block westId = world.getBlock(i - 1, j, k);
		Block suedId = world.getBlock(i, j, k + 1);
		Block nordId = world.getBlock(i, j, k - 1);

		int ostMd = world.getBlockMetadata(i + 1, j, k);
		int westMd = world.getBlockMetadata(i - 1, j, k);
		int suedMd = world.getBlockMetadata(i, j, k + 1);
		int nordMd = world.getBlockMetadata(i, j, k - 1);

		boolean ost = false, west = false, nord = false, sued = false;
		int connects = 0;

		// obfuscated for: isRailBlockAt
		if (BlockRail.func_150049_b_(world, i + 1, j, k) && ostMd == 1) {
			connects++;
			ost = true;
		}
		if (BlockRail.func_150049_b_(world, i - 1, j, k) && westMd == 1) {
			connects++;
			west = true;
		}
		if (BlockRail.func_150049_b_(world, i, j, k - 1) && nordMd == 0) {
			connects++;
			nord = true;
		}
		if (BlockRail.func_150049_b_(world, i, j, k + 1) && suedMd == 0) {
			connects++;
			sued = true;
		}
		switch (connects) {
		case 1: {
			if (ost) {
				meta = 2;
			} else if (west) {
				meta = 4;
			} else if (nord) {
				meta = 3;
			} else if (sued) {
				meta = 1;
			}
			break;
		}
		case 2: {
			if (ost) {
				meta = 15;
				if (sued) {
					meta = 5;
				} else if (nord) {
					meta = 8;
				}
			} else if (west) {
				if (nord) {
					meta = 7;
				} else if (sued) {
					meta = 6;
				}
			} else if (nord) {
				if (sued) {
					meta = 14;
				}
			}
			break;
		}
		case 3: {
			if (!ost) {
				meta = 10;
			} else if (!west) {
				meta = 12;
			} else if (!nord) {
				meta = 9;
			} else if (!sued) {
				meta = 11;
			}
			break;
		}
		case 4: {
			meta = 13;
			break;
		}
		default:
			meta = 0;
		}
		// *~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*
		try {
			TeleportStations.db.updateMeta(i, j, k, meta);
		} catch (Exception e) {
			// Wenn das Gui mal zu lange braucht...
		}
		world.setBlock(i, j, k, world.getBlock(i, j, k), meta, 2);
		return meta;
	}

	/**
	 * Handles interaction with colliding minecarts in rail-mode.
	 * <p>
	 * A incomming minecart is accelerated depending on the teleporter's
	 * meta-data.
	 * 
	 * @param world
	 *            World the world
	 * @param eM
	 *            EntityMinecart the minecart
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 */
	public void handleMC(World world, EntityMinecart eM, int i, int j, int k) {
		double ex = eM.posX, ez = eM.posZ, speed = eM.motionX + eM.motionZ
				+ 0.05;
		TeleData quelle = TeleportStations.db.getTeleDataByCoords(i, j, k);
		ChunkCoordinates ziel = quelle.getZiel();
		if (ziel != null && world.isBlockIndirectlyGettingPowered(i, j, k)) {
			i = ziel.posX;
			j = ziel.posY;
			k = ziel.posZ;
		}
		int teleMeta = world.getBlockMetadata(i, j, k);
		teleMeta -= 1;
		switch (teleMeta) {
		case 0: {// Streckenende im Norden
			eM.setPosition(i + 0.5, j, k + 1.5);
			eM.addVelocity(0, 0, speed);
			break;
		}
		case 1: {// Streckenende im Westen
			eM.setPosition(i + 1.5, j, k + 0.5);
			eM.addVelocity(speed, 0, 0);
			break;
		}
		case 2: {// Streckenende im Sueden
			eM.setPosition(i + 0.5, j, k - 0.5);
			eM.addVelocity(0, 0, -speed);
			break;
		}
		case 3: {// Streckenende im Osten
			eM.setPosition(i - 0.5, j, k + 0.5);
			eM.addVelocity(-speed, 0, 0);
			break;
		}
		case 4: {// Kurve Sued-Ost
			if (k + 0.5 < ez) { // Cart von Sueden
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (k + 0.5 > ez) { // Cart von Norden
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			}
			if (i + 0.5 > ex) { // Cart von Westen
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) { // Cart von Osten
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			}
			break;
		}
		case 5: {// Kurve Sued-West
			if (k + 0.5 < ez) { // Cart von Sueden
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			} else if (k + 0.5 > ez) { // Cart von Norden
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			}
			if (i + 0.5 > ex) { // Cart von Westen
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			} else if (i + 0.5 < ex) { // Cart von Osten
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			}
			break;
		}
		case 6: {// Kurve Nord-West
			if (k + 0.5 < ez) { // Cart von Sueden
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) { // Cart von Norden
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			}
			if (i + 0.5 > ex) { // Cart von Westen
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (i + 0.5 < ex) { // Cart von Osten
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			}
			break;
		}
		case 7: {// Kurve Nord-Ost
			if (k + 0.5 < ez) { // Cart von Sueden
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) { // Cart von Norden
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			}
			if (i + 0.5 > ex) { // Cart von Westen
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) { // Cart von Osten
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			}
			break;
		}
		case 8: {// T ohne Nord
			if (i + 0.5 > ex) {
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) {
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			} else
				eM.setPosition(i + 0.5, j, k + 2);
			eM.addVelocity(0, 0, speed);
			break;
		}
		case 9: {// T ohne Ost
			if (k + 0.5 < ez) {
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) {
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			} else
				eM.setPosition(i - 1.5, j, k + 0.5);
			eM.addVelocity(-speed, 0, 0);
			break;
		}
		case 10: {// T ohne Sued
			if (i + 0.5 > ex) {
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) {
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			} else
				eM.setPosition(i + 0.5, j, k - 1.5);
			eM.addVelocity(0, 0, -speed);
			break;
		}
		case 11: {// T ohne West
			if (k + 0.5 < ez) {
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) {
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			} else
				eM.setPosition(i + 2, j, k + 0.5);
			break;
		}
		case 12: {// Kreuzung
			if (k + 0.5 < ez) { // Cart von Sueden
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) { // Cart von Norden
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			}
			if (i + 0.5 > ex) { // Cart von Westen
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) { // Cart von Osten
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			}
			break;
		}
		case 13: {// Gerade Nord-Sued
			if (k + 0.5 < ez) {
				eM.setPosition(i + 0.5, j, k - 0.5);
				eM.addVelocity(0, 0, -speed);
			} else if (k + 0.5 > ez) {
				eM.setPosition(i + 0.5, j, k + 1.5);
				eM.addVelocity(0, 0, speed);
			}
			break;
		}
		case 14: {// Gerade Ost-West
			if (i + 0.5 > ex) {
				eM.setPosition(i + 1.5, j, k + 0.5);
				eM.addVelocity(speed, 0, 0);
			} else if (i + 0.5 < ex) {
				eM.setPosition(i - 0.5, j, k + 0.5);
				eM.addVelocity(-speed, 0, 0);
			}
			break;
		}
		default: {// Keine Schiene oder Fehler
			// eM.setOnFireFromLava();
		}
		}
	}

	protected boolean power(World world, int i, int j, int k) {
		return world.isBlockIndirectlyGettingPowered(i, j, k);
	}
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityTeleTarget();
	}

}
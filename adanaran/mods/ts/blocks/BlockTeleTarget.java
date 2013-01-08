package adanaran.mods.ts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.entities.TileEntityTeleporter;

/**
 * Teleporter-target-block represents valid destination for teleporting.
 * <p>
 * This block doesn't require diamonds to craft, but also can't teleport the
 * player.
 * 
 * @author Demitreus
 */
public class BlockTeleTarget extends Block {

	/**
	 * Constructor of teleportertarget-block.
	 * 
	 * @param par1
	 *            int ID
	 */
	public BlockTeleTarget(int par1) {
		super(par1, Material.iron);
		this.setBlockBounds(0.0f, 0f, 0.0f, 1.0f, 0.125f, 1.0f);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		return par1 == 1 ? par2 : 32;
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
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return null;
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	@Override
	public String getTextureFile() {
		return "/adanaran/mods/ts/textures/TS.png";
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3,
			int par4, int par5) {
		update(par1World, par2, par3, par4);
		if (!par1World.isBlockNormalCube(par2, par3 - 1, par4)) {
			par1World.setBlock(par2, par3 - 1, par4,
					par1World.getBlockId(par2, par3 - 1, par4));
		}
	}

	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return par1World.isBlockNormalCube(par2, par3 - 1, par4);
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return !(par1World.getBlockId(par2, par3 - 1, par4) == 3001)
				&& !(par1World.getBlockId(par2, par3 - 1, par4) == 3002);
	}

	@Override
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
			int par4, int par5) {
		deleteTP(par1World, par2, par3, par4);
		super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);

	}

	@Override
	public void onBlockDestroyedByExplosion(World par1World, int par2,
			int par3, int par4) {
		deleteTP(par1World, par2, par3, par4);
		super.onBlockDestroyedByExplosion(par1World, par2, par3, par4);
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
		world.setBlock(i, j + 1, k, 0);
		world.setBlock(i, j + 2, k, 0);
		world.setBlock(i, j, k, 0);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLiving par5EntityLiving) {
		par1World.setBlock(par2, par3 + 1, par4, 3004);
		par1World.setBlock(par2, par3 + 2, par4, 3005);
		if (par5EntityLiving instanceof EntityPlayer) {
			((EntityPlayer) par5EntityLiving).openGui(
					TeleportStations.instance, 0, par1World, par2, par3, par4);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3,
			int par4, Entity par5Entity) {
		if (par5Entity instanceof EntityMinecart) {
			System.out.print("MINECART");
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
	protected int update(World world, int i, int j, int k) {

		int meta = 0;
		int ostId = world.getBlockId(i + 1, j, k);
		int westId = world.getBlockId(i - 1, j, k);
		int suedId = world.getBlockId(i, j, k + 1);
		int nordId = world.getBlockId(i, j, k - 1);

		int ostMd = world.getBlockMetadata(i + 1, j, k);
		int westMd = world.getBlockMetadata(i - 1, j, k);
		int suedMd = world.getBlockMetadata(i, j, k + 1);
		int nordMd = world.getBlockMetadata(i, j, k - 1);

		boolean ost = false, west = false, nord = false, sued = false;
		int connects = 0;

		if (BlockRail.isRailBlockAt(world, i + 1, j, k)
				&& (ostMd == 1 || ostMd == 7 || ostMd == 8 || ostMd == 3)) {
			connects++;
			ost = true;
		}
		if (BlockRail.isRailBlockAt(world, i - 1, j, k)
				&& (westMd == 1 || westMd == 6 || westMd == 9 || westMd == 2)) {
			connects++;
			west = true;
		}
		if (BlockRail.isRailBlockAt(world, i, j, k - 1)
				&& (nordMd == 0 || nordMd == 7 || nordMd == 6 || nordMd == 5)) {
			connects++;
			nord = true;
		}
		if (BlockRail.isRailBlockAt(world, i, j, k + 1)
				&& (suedMd == 0 || suedMd == 9 || suedMd == 8 || suedMd == 4)) {
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
		world.setBlockAndMetadataWithNotify(i, j, k, world.getBlockId(i, j, k),
				meta);
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
		TileEntity quelle = world.getBlockTileEntity(i, j+2, k);
		TileEntityTeleporter ziel = null;
		if( quelle instanceof TileEntityTeleporter){
			ziel = ((TileEntityTeleporter) quelle).getZiel();
		}
		if (ziel != null) {
			i = ziel.xCoord;
			j = ziel.yCoord - 2;
			k = ziel.zCoord;
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
}

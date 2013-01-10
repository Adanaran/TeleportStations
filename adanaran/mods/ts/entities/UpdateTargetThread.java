package adanaran.mods.ts.entities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import adanaran.mods.ts.TeleportStations;

/**
 * A Thread used to update the teleporter's tile-entities.
 * <p>
 * Waits until the entities are created.
 * 
 * @author Adanaran
 */
public class UpdateTargetThread extends Thread {

	private TileEntityTeleporter tileEntity;
	private int tx;
	private int ty;
	private int tz;
	private World world;

	/**
	 * Creates a new Thread.
	 * 
	 * @param tileEntity
	 *            TileEntityTeleporter the teleporter being updated
	 * @param tx
	 *            int z-coordinate of target
	 * @param ty
	 *            int z-coordinate of target
	 * @param tz
	 *            int z-coordinate of target
	 */
	public UpdateTargetThread(TileEntityTeleporter tileEntity, int tx, int ty,
			int tz) {
		this.tileEntity = tileEntity;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		if (!TeleportStations.proxy.isServer()
				&& !TeleportStations.proxy.isSinglePlayer()) {
			this.world = tileEntity.worldObj;
		} else {
			this.world = TeleportStations.proxy.getWorld(tileEntity
					.getWorldType());
		}
	}

	@Override
	public void run() {
		while (world != null && world.getBlockTileEntity(tx, ty, tz) == null) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		updateTargetIntern();
	}

	private void updateTargetIntern() {
		TileEntity te = world.getBlockTileEntity(tx, ty, tz);
		if (te instanceof TileEntityTeleporter) {
			tileEntity.setTarget((TileEntityTeleporter) te);
		}
	}
}

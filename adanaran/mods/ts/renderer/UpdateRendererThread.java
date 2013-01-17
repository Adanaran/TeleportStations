package adanaran.mods.ts.renderer;

import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.database.TeleData;
import adanaran.mods.ts.entities.TileEntityTele;

/**
 * A Thread used to update the teleporter's tile-entities.
 * <p>
 * Waits until the entities are created.
 * 
 * @author Adanaran
 * 
 */
public class UpdateRendererThread extends Thread {

	private TeleData td;

	/**
	 * Creates a new Thread.
	 * 
	 * @param td
	 *            TeleData dataobject of teleporter being updated
	 */
	public UpdateRendererThread(TeleData td) {
		this.td = td;
	}

	@Override
	public void run() {
		System.out.println("Trying to update Renderer at " + td.posX + "|"
				+ td.posY + "|" + td.posZ);
		while (TeleportStations.proxy.getWorld().getBlockTileEntity(td.posX,
				td.posY + 1, td.posZ) == null) {
			try {
				System.out.println("Waiting for TileEntity...");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		updateRendererIntern(td);
	}

	private void updateRendererIntern(TeleData td) {
		System.out.println("Updating.");
		((TileEntityTele) TeleportStations.proxy.getWorld().getBlockTileEntity(
				td.posX, td.posY + 1, td.posZ)).setNameAndTarget(td.getName(),
				TeleportStations.db.getNameByCoords(td.getZiel()));
	}
}

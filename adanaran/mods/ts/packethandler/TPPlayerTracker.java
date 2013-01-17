package adanaran.mods.ts.packethandler;

import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.database.TPDatabase;
import adanaran.mods.ts.database.TPFileHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

/**
 * PlayerTracker for teleporter mod.
 * <p>
 * Loads the database at login.
 * 
 * @author Adanaran
 */
public class TPPlayerTracker implements IPlayerTracker {

	private static TPDatabase db;
	private static TPFileHandler fh;

	/**
	 * Creates a new TPPlayerTracker.
	 * 
	 * @param db
	 *            TPDatabase the database
	 * @param fh
	 *            TPFileHandler the fileHandler
	 */
	public TPPlayerTracker(TPDatabase db, TPFileHandler fh) {
		this.db = db;
		this.fh = fh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.IPlayerTracker#onPlayerLogin(net.minecraft.src.
	 * EntityPlayer)
	 */
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		System.out.println("Player logging in at");
		if (TeleportStations.proxy.isSinglePlayer()) {
			if (player.getEntityName().equals(
					Minecraft.getMinecraft().getIntegratedServer()
							.getServerOwner())) {
				System.out.println("local world");
				db.clearDB();
				fh.readFromFile();
			} else {
				System.out.println("remote world");
				db.sendDB(player);
			}
		}
		if (TeleportStations.proxy.isServer()) {
			System.out.println("remote dedicated server");
			fh.readFromFile();
			db.sendDB(player);
		}
		System.out.println("... login handling... finished");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.IPlayerTracker#onPlayerLogout(net.minecraft.src.
	 * EntityPlayer)
	 */
	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.IPlayerTracker#onPlayerChangedDimension(net.minecraft
	 * .src.EntityPlayer)
	 */
	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.IPlayerTracker#onPlayerRespawn(net.minecraft.src.
	 * EntityPlayer)
	 */
	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}

}

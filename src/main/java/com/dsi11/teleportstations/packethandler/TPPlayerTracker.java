package com.dsi11.teleportstations.packethandler;

import java.util.logging.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TPDatabase;
import com.dsi11.teleportstations.database.TPFileHandler;
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

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		TeleportStations.logger.log(Level.FINEST, "Player logging in at");
		if (TeleportStations.proxy.isSinglePlayer()) {
			if (player.getEntityName().equals(
					Minecraft.getMinecraft().getIntegratedServer()
							.getServerOwner())) {
				TeleportStations.logger.log(Level.FINEST, "local world");
				db.clearDB();
				fh.readFromFile();
			} else {
				TeleportStations.logger.log(Level.FINEST, "remote world");
				db.sendDB(player);
			}
		}
		if (TeleportStations.proxy.isServer()) {
			TeleportStations.logger.log(Level.FINEST, "remote dedicated server");
			fh.readFromFile();
			db.sendDB(player);
		}
		TeleportStations.logger.log(Level.FINEST, "... login handling... finished");
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}
}

package com.dsi11.teleportstations.network;

import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.Database;
import com.dsi11.teleportstations.database.FileHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * PlayerTracker for teleporter mod.
 * <p>
 * Loads the database at login.
 * 
 * @author Adanaran
 */
public class PlayerTracker {

	private static Database db;
	private static FileHandler fh;

	/**
	 * Creates a new TPPlayerTracker.
	 * 
	 * @param db
	 *            TPDatabase the database
	 * @param fh
	 *            TPFileHandler the fileHandler
	 */
	public PlayerTracker(Database db, FileHandler fh) {
		this.db = db;
		this.fh = fh;
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent evt) {
		EntityPlayer player = evt.player;
		TeleportStations.logger.log(Level.TRACE, "Player logging in at");
		if (TeleportStations.proxy.isSinglePlayer()) {
			if (player
					.getGameProfile()
					.getName()
					.equals(Minecraft.getMinecraft().getIntegratedServer()
							.getServerOwner())) {
				TeleportStations.logger.log(Level.TRACE, "local world");
				db.clearDB();
				fh.readFromFile();
			} else {
				TeleportStations.logger.log(Level.TRACE, "remote world");
				db.sendDBToPlayer((EntityPlayerMP)player);
			}
		}
		if (TeleportStations.proxy.isServer()) {
			TeleportStations.logger.log(Level.TRACE, "remote dedicated server");
			fh.readFromFile();
			db.sendDBToPlayer((EntityPlayerMP)player);
		}
		TeleportStations.logger.log(Level.TRACE,
				"... login handling... finished");
	}
}

package com.dsi11.teleportstations.database;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.network.PacketHandler;

import cpw.mods.fml.relauncher.Side;

/**
 * Database for {@link#TeleportStations}.
 * <p>
 * Used to store the teleporters.
 * 
 * @author Adanaran
 */
public class Database {
	private TreeMap<ChunkCoordinates, TeleData> db = new TreeMap<ChunkCoordinates, TeleData>(
			new ChunkCoordsComparator());
	private PacketHandler packetHandler;

	/**
	 * Creates a new Database.
	 * <p>
	 * Does nothing else.
	 */
	public Database(PacketHandler packetHandler) {
		this.packetHandler = packetHandler;
	}

	/**
	 * Used from TPAddPacket, FileHander, this.
	 * 
	 * @param teleData
	 *            TeleData
	 */
	public void addTeleDataToDatabaseWithOutNotification(TeleData teleData) {
		this.db.put(new ChunkCoordinates(teleData.posX, teleData.posY,
				teleData.posZ), teleData);
	}

	/**
	 * Used from Packet.
	 * 
	 * @param teleData
	 *            TeleData
	 */
	public void addTeleDataToDatabaseWithNotificationAtServer(TeleData teleData) {
		addTeleDataToDatabaseWithOutNotification(teleData);
		packetHandler.SendTPAddPacket(teleData, Side.CLIENT);
		TeleportStations.fh.writeToFile();
	}

	/**
	 * GuiEditTeleName
	 * 
	 * @param teleData
	 */
	public void addTeleDataToDatabaseWithNotificationAtClient(TeleData teleData) {
		addTeleDataToDatabaseWithOutNotification(teleData);
		packetHandler.SendTPAddPacket(teleData, Side.SERVER);
	}

	/**
	 * Block
	 * 
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public boolean isTPInDatabase(int i, int j, int k) {
		return db.containsKey(new ChunkCoordinates(i, j, k));
	}

	/**
	 * Block
	 * 
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public TeleData getTeleDataByCoords(int i, int j, int k) {
		return getTeleDataByCoords(new ChunkCoordinates(i, j, k));
	}

	/**
	 * this
	 * 
	 * @param chunkCoordinates
	 * @return
	 */
	public TeleData getTeleDataByCoords(ChunkCoordinates chunkCoordinates) {
		return db.get(chunkCoordinates);
	}

	/**
	 * Block
	 * 
	 * @param i
	 * @param j
	 * @param k
	 */
	public void removeTP(int i, int j, int k) {
		ChunkCoordinates coords = new ChunkCoordinates(i, j, k);
		TeleData td = db.remove(coords);
		if (TeleportStations.proxy.isServer()
				|| TeleportStations.proxy.isSinglePlayer()) {
			packetHandler.SendTPRemovePacket(td, Side.CLIENT);
			deleteReferencesAfterTPRemoved(coords);
			TeleportStations.fh.writeToFile();
		}
	}

	/**
	 * Block
	 * 
	 * @param i
	 * @param j
	 * @param k
	 * @param meta
	 */
	public void updateMeta(int i, int j, int k, int meta) {
		ChunkCoordinates coords = new ChunkCoordinates(i, j, k);
		TeleData td = db.get(coords);
		td.setMeta(meta);
		db.put(coords, td);
		TeleportStations.fh.writeToFile();
	}

	/**
	 * FileHandler
	 * 
	 * @return
	 */
	public TreeMap<ChunkCoordinates, TeleData> getDB() {
		return (TreeMap<ChunkCoordinates, TeleData>) db.clone();
	}

	/**
	 * FileHandler
	 */
	public void clearDB() {
		db.clear();
	}

	/**
	 * GuiEditTeleName
	 * 
	 * @return
	 */
	public LinkedList getAllNames() {
		LinkedList<String> names = new LinkedList();
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			names.add(entry.getValue().getName());
		}
		return names;
	}

	/**
	 * TileEntityTeleporter
	 * 
	 * @param chunkCoordinates
	 * @return
	 */
	public TeleData getZielByCoords(ChunkCoordinates chunkCoordinates) {
		TeleData teleData = db.get(chunkCoordinates);
		if (teleData.getZiel() == null) {
			return null;
		}
		TeleData target = db.get(teleData.getZiel());
		return target;
	}

	/**
	 * GuiEditTeleTarget
	 * 
	 * @param chunkCoordinates
	 * @param chunkCoordinates2
	 */
	public void changeTarget(ChunkCoordinates self, ChunkCoordinates target) {
		TeleData td = db.get(self);
		td.setZiel(target);
		db.put(self, td);
		if (!TeleportStations.proxy.isSinglePlayer()) {
			packetHandler.sendTPUpdatePacket(td, Side.SERVER);
		} else {
			TeleportStations.fh.writeToFile();
		}
	}

	/**
	 * TPPlayerTracker
	 * 
	 * @param player
	 */
	public void sendDBToPlayer(EntityPlayerMP player) {
		packetHandler.sendTPDatabasePacket(db, player);
	}

	/**
	 * TPRemovePacket
	 * 
	 * @param teleData
	 */
	public void removeTeleDataFromDatabaseWithOutNotification(TeleData teleData) {
		TeleData td = db.remove(new ChunkCoordinates(teleData.posX,
				teleData.posY, teleData.posZ));
		deleteReferencesAfterTPRemoved(td);
	}

	/**
	 * this only serverside
	 * 
	 * @param coords
	 */
	private void deleteReferencesAfterTPRemoved(ChunkCoordinates coords) {
		TeleportStations.logger
				.log(Level.TRACE, "Removing references in DB...");
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			if (entry.getValue().getZiel().equals(coords)) {
				TeleportStations.logger.log(Level.TRACE,
						"Deleting reference to removed teleporter.");
				entry.getValue().setZiel(null);
			}
		}
		TeleportStations.logger.log(Level.TRACE, "All references removed.");
	}

	/**
	 * this only serverside
	 * 
	 * @param coords
	 */
	private void deleteReferencesAfterMetaChange(ChunkCoordinates coords) {
		TeleportStations.logger.log(Level.TRACE,
				"Checking for references in DB...");
		TeleData td = db.get(coords);
		int meta = td.getMeta();
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			TeleData refTD = entry.getValue();
			int refMeta = refTD.getMeta();
			ChunkCoordinates refZiel = refTD.getZiel();
			if (refZiel == coords) {
				if ((meta > 0 && refMeta == 0) || (meta == 0 && refMeta > 0)) {
					refTD.setZiel(null);
					TeleportStations.logger.log(Level.TRACE,
							"Removed wrong reference");
				}
			}
		}
		TeleportStations.fh.writeToFile();
		TeleportStations.logger.log(Level.TRACE,
				"All wrong references deleted.");
	}

	/**
	 * Packet, this
	 * 
	 * @param teleData
	 */
	public void updateTeleDataInDataBaseWithoutNotification(TeleData teleData) {
		ChunkCoordinates coords = new ChunkCoordinates(teleData.posX,
				teleData.posY, teleData.posZ);
		db.put(coords, teleData);
	}

	/**
	 * Packet
	 * 
	 * @param teleData
	 */
	public void updateTeleDataInDataBaseWithNotificationAtServer(
			TeleData teleData) {
		updateTeleDataInDataBaseWithoutNotification(teleData);
		packetHandler.sendTPUpdatePacket(teleData, Side.CLIENT);
		TeleportStations.fh.writeToFile();
	}

	/**
	 * Packet
	 * 
	 * @param dataBase
	 */
	public void receiveDB(TreeMap<ChunkCoordinates, TeleData> dataBase) {
		this.db = dataBase;
	}
}

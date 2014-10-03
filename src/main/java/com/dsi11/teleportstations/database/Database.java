package com.dsi11.teleportstations.database;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.network.PacketHandler;

/**
 * Database for {@link#TeleportStations}.
 * <p>
 * Used to store the teleporters.
 * 
 * @author Adanaran
 */
public class Database {
	private TreeMap<BlockPos, TeleData> db = new TreeMap<BlockPos, TeleData>(
			new BlockPosComparator());
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
		this.db.put(teleData.getPosition(), teleData);
	}

	/**
	 * Used from Packet.
	 * 
	 * @param teleData
	 *            TeleData
	 */
	public void addTeleDataToDatabaseWithNotificationAtServer(TeleData teleData) {
		addTeleDataToDatabaseWithOutNotification(teleData);
		packetHandler.sendTPAddMessage(teleData, Side.CLIENT);
		TeleportStations.fh.writeToFile();
	}

	/**
	 * GuiEditTeleName
	 * 
	 * @param teleData
	 */
	public void addTeleDataToDatabaseWithNotificationAtClient(TeleData teleData) {
		addTeleDataToDatabaseWithOutNotification(teleData);
		packetHandler.sendTPAddMessage(teleData, Side.SERVER);
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
		return db.containsKey(new BlockPos(i, j, k));
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
		return getTeleDataByCoords(new BlockPos(i, j, k));
	}

	/**
	 * this
	 * 
	 * @param BlockPos
	 * @return
	 */
	public TeleData getTeleDataByCoords(BlockPos BlockPos) {
		return db.get(BlockPos);
	}

	/**
	 * Block
	 * 
	 * @param i
	 * @param j
	 * @param k
	 */
	public void removeTP(int i, int j, int k) {
		BlockPos coords = new BlockPos(i, j, k);
		TeleData td = db.remove(coords);
		if (TeleportStations.proxy.isServer()
				|| TeleportStations.proxy.isSinglePlayer()) {
			packetHandler.sendTPRemoveMessage(td, Side.CLIENT);
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
		BlockPos coords = new BlockPos(i, j, k);
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
	public TreeMap<BlockPos, TeleData> getDB() {
		return (TreeMap<BlockPos, TeleData>) db.clone();
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
		for (Map.Entry<BlockPos, TeleData> entry : db.entrySet()) {
			names.add(entry.getValue().getName());
		}
		return names;
	}

	/**
	 * TileEntityTeleporter
	 * 
	 * @param blockPos
	 * @return
	 */
	public TeleData getZielByCoords(BlockPos blockPos) {
		TeleData teleData = db.get(blockPos);
		if (teleData == null || teleData.getTarget() == null) {
			return null;
		}
		TeleData target = db.get(teleData.getTarget());
		return target;
	}

	/**
	 * GuiEditTeleTarget
	 * 
	 * @param self
	 * @param target
	 */
	public void changeTarget(BlockPos self, BlockPos target) {
		TeleData td = db.get(self);
		td.setTarget(target);
		db.put(self, td);
		if (!TeleportStations.proxy.isSinglePlayer()) {
			packetHandler.sendTPUpdateMessage(td, Side.SERVER);
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
		packetHandler.sendTPDatabaseMessage(db, player);
	}

	/**
	 * TPRemovePacket
	 * 
	 * @param teleData
	 */
	public void removeTeleDataFromDatabaseWithOutNotification(TeleData teleData) {
		TeleData td = db.remove(teleData.getPosition());
		deleteReferencesAfterTPRemoved(td.getPosition());
	}

	/**
	 * this only serverside
	 *
     * @param coords
     */
	private void deleteReferencesAfterTPRemoved(BlockPos coords) {
		TeleportStations.logger
				.log(Level.TRACE, "Removing references in DB...");
		for (Map.Entry<BlockPos, TeleData> entry : db.entrySet()) {
			TeleData value = entry.getValue();
			if(value== null){
				throw new RuntimeException("value null");
			}
			BlockPos target = value.getTarget();
			if(target != null && target.equals(coords)){
				TeleportStations.logger.log(Level.TRACE,
						"Deleting reference to removed teleporter.");
				entry.getValue().setTarget(null);
			}
		}
		TeleportStations.logger.log(Level.TRACE, "All references removed.");
	}

	/**
	 * this only serverside
	 * 
	 * @param coords
	 */
	private void deleteReferencesAfterMetaChange(BlockPos coords) {
		TeleportStations.logger.log(Level.TRACE,
				"Checking for references in DB...");
		TeleData td = db.get(coords);
		int meta = td.getMeta();
		for (Map.Entry<BlockPos, TeleData> entry : db.entrySet()) {
			TeleData refTD = entry.getValue();
			int refMeta = refTD.getMeta();
			BlockPos refZiel = refTD.getTarget();
			if (refZiel == coords) {
				if ((meta > 0 && refMeta == 0) || (meta == 0 && refMeta > 0)) {
					refTD.setTarget(null);
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
		BlockPos coords = teleData.getPosition();
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
		packetHandler.sendTPUpdateMessage(teleData, Side.CLIENT);
		TeleportStations.fh.writeToFile();
	}

	/**
	 * Packet
	 *
     * @param dataBase
     */
	public void receiveDB(TreeMap<BlockPos, TeleData> dataBase) {
		this.db = dataBase;
	}

	/**
	 * 
	 * @param start
	 * @param targetToSet
	 * @return
	 */
	public boolean wouldTPRoundtripOccur(TeleData start,
			BlockPos targetToSet) {
		while (targetToSet != null) {
			if (targetToSet.equals(start)) {
				return true;
			}
            TeleData td = TeleportStations.db.getZielByCoords(targetToSet);
            if(td!=null) {
                targetToSet = td.getPosition();
            }
		}
		return false;
	}
}

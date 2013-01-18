package adanaran.mods.ts.database;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.packethandler.TPPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Database for {@link#TeleportStations}.
 * <p>
 * Used to store the teleporters.
 * 
 * @author Adanaran
 * 
 */
public class TPDatabase {
	private static TreeMap<ChunkCoordinates, TeleData> db = new TreeMap<ChunkCoordinates, TeleData>(
			new ChunkCoordsComparator());

	/**
	 * Creates a new Database.
	 * <p>
	 * Does nothing else.
	 */
	public TPDatabase() {
	}

	/**
	 * Adds a teleporter to the database.
	 * 
	 * @param name
	 *            String teleporter's name
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 * @param meta
	 *            int meta
	 * @param dim
	 *            int world dimension
	 */
	@SideOnly(Side.CLIENT)
	public void addNewTP(String name, int x, int y, int z, int meta, int dim) {
		TeleData td = new TeleData(name, x, y, z, meta, dim);
		db.put(new ChunkCoordinates(x, y, z), td);
		if (!TeleportStations.proxy.isSinglePlayer()) {
			PacketDispatcher.sendPacketToServer(TPPacketHandler
					.getNewAddTPPacket(td));
		} else {
			TeleportStations.fh.writeToFile();
			TPPacketHandler.sendPacketToClients(TPPacketHandler
					.getNewAddTPPacket(td));
		}
	}

	/**
	 * Adds a teleporter to the database.
	 * <p>
	 * Not for use outside of {@link TPFileHandler}.
	 * 
	 * @param td
	 */
	public void addTP(TeleData td) {
		if (TeleportStations.proxy.isServer()
				|| TeleportStations.proxy.isSinglePlayer()) {
			System.out.println("Received  " + td + " from flatfile database");
			db.put(new ChunkCoordinates(td.posX, td.posY, td.posZ), td);
			System.out.println("Groesse der Datenbank: " + db.size()
					+ " Eintraege.");
		}
	}

	/**
	 * Removes a teleporter from the database.
	 * 
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 */
	public void removeTP(int x, int y, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
		TeleData td = db.remove(coords);
		if (TeleportStations.proxy.isServer()
				|| TeleportStations.proxy.isSinglePlayer()) {
			TPPacketHandler.sendPacketToClients(TPPacketHandler
					.getNewRemoveTPPacket(td));
			deleteReferencesAfterTPRemoved(coords);
		}
	}

	/**
	 * Changes a teleporter's target in the database.
	 * 
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 * @param coords
	 *            ChunkCoordinates coordinates of the new target
	 */
	@SideOnly(Side.CLIENT)
	public void changeTarget(int x, int y, int z, ChunkCoordinates ziel) {
		ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
		TeleData td = db.get(coords);
		td.setZiel(ziel);
		db.put(coords, td);
		if (!TeleportStations.proxy.isSinglePlayer()) {
			PacketDispatcher.sendPacketToServer(TPPacketHandler
					.getNewAddTPPacket(td));
		} else {
			TeleportStations.fh.writeToFile();
		}
	}

	/**
	 * Clears the database.
	 * <p>
	 * Use with caution.
	 */
	public void clearDB() {
		db.clear();
	}

	/**
	 * Send the database to all connected players.
	 * 
	 * @param player
	 *            EntityPlayer the player the database should be send to
	 */
	public void sendDB(EntityPlayer player) {
		System.out.print("Datenbanksendeauftrag registriert, Empfaenger: "
				+ player.getEntityName() + ", DB-size: " + db.size());
		if (db.size() > 0) {
			System.out.println();
			PacketDispatcher.sendPacketToPlayer(
					TPPacketHandler.getNewDatabaseSyncPacket(this.getDB()),
					(Player) player);
		}else{
			System.out.println(" Database empty, not sending.");
		}
	}

	/**
	 * Processes changed teleporters from server.
	 * 
	 * @param td
	 *            TeleData the changed dataobject
	 */
	public void receiveChangedTPFromServer(TeleData td) {
		db.put(new ChunkCoordinates(td.posX, td.posY, td.posZ), td);
	}

	/**
	 * Processes changed teleporters from clients.
	 * 
	 * @param td
	 *            TeleData the changed data-object
	 */
	public void receiveChangedTPFromClient(TeleData td) {
		db.put(new ChunkCoordinates(td.posX, td.posY, td.posZ), td);
		TPPacketHandler.sendPacketToClients(TPPacketHandler
				.getNewAddTPPacket(td));
		TeleportStations.fh.writeToFile();
	}

	/**
	 * Processes removed teleporters from servers.
	 * 
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 */
	public void receiveRemovedTPFromServer(int x, int y, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
		db.remove(coords);
		deleteReferencesAfterTPRemoved(coords);
	}

	/**
	 * Gets the names of all teleporters.
	 * 
	 * @return LinkedList list of all teleporter names
	 */
	public LinkedList getAllNames() {
		LinkedList<String> names = new LinkedList();
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			names.add(entry.getValue().getName());
		}
		return names;
	}

	/**
	 * Gets access to the internal database.
	 * 
	 * @return TreeMap<ChunkCoordinates, TeleData> the internal database
	 */
	public static TreeMap<ChunkCoordinates, TeleData> getDB() {
		return (TreeMap<ChunkCoordinates, TeleData>) db.clone();
	}

	/**
	 * Gets a teleporters name.
	 * 
	 * @param coords
	 *            ChunkCoordinates of the teleporter
	 * @return String the teleporter's name
	 */
	public String getNameByCoords(ChunkCoordinates coords) {
		return db.get(coords).getName();
	}

	/**
	 * Gets the teleporter's name for the given coordinates.
	 * 
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 * @return String name of the teleporter at given coordinates or
	 *         {@code null}
	 */
	public String getNameByCoords(int x, int y, int z) {
		return getNameByCoords(new ChunkCoordinates(x, y, z));
	}

	/**
	 * Gets the TeleData object for the given coordinates.
	 * 
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 * @return TeleData dataobject at given coordinates or {@code null}
	 */
	public TeleData getTeleDataByCoords(int x, int y, int z) {
		return db.get(new ChunkCoordinates(x, y, z));
	}

	/**
	 * Gets a teleporters target.
	 * 
	 * @param coords
	 *            ChunkCoordinates of the teleporter
	 * @return ChunkCoordinates the teleporter's target
	 */
	public ChunkCoordinates getZielByCoords(ChunkCoordinates coords) {
		return db.get(coords).getZiel();
	}

	/**
	 * Checks if teleporter at given coordinates is listed in the database.
	 * 
	 * @param x
	 *            int the x coordinate
	 * @param y
	 *            int the y coordinate
	 * @param z
	 *            int the z coordinate
	 * @return true if in database
	 */
	public boolean isTPInDatabase(int x, int y, int z) {
		return db.containsKey(new ChunkCoordinates(x, y, z));
	}

	/**
	 * Updates the meta of the teleporter at given coordinates.
	 * 
	 * @param x
	 *            int the x coordinate
	 * @param y
	 *            int the y coordinate
	 * @param z
	 *            int the z coordinate
	 * @param meta
	 *            int the new meta value
	 */
	public void updateMeta(int x, int y, int z, int meta) {
		ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
		TeleData td = db.get(coords);
		td.setMeta(meta);
		db.put(coords, td);
		TeleportStations.fh.writeToFile();
	}

	private void deleteReferencesAfterTPRemoved(ChunkCoordinates coords) {
		System.out.println("Checking for references in DB...");
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			if (entry.getValue().getZiel() == coords) {
				System.out.println("Deleting reference to removed teleporter.");
				entry.getValue().setZiel(null);
			}
		}
		TeleportStations.fh.writeToFile();
		System.out.println("All references deleted.");
	}

	private void deleteReferencesAfterMetaChange(ChunkCoordinates coords) {
		System.out.println("Checking for references in DB...");
		TeleData td = db.get(coords);
		int meta = td.getMeta();
		for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
			TeleData refTD = entry.getValue();
			int refMeta = refTD.getMeta();
			ChunkCoordinates refZiel = refTD.getZiel();
			if (refZiel == coords) {
				if ((meta > 0 && refMeta == 0) || (meta == 0 && refMeta > 0)) {
					refTD.setZiel(null);
				}
			}
		}
		TeleportStations.fh.writeToFile();
		System.out.println("All wrong references deleted.");
	}
}

package adanaran.mods.ts.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import adanaran.mods.ts.TeleportStations;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

/**
 * The FileHandler for teleporter mod.
 * <p>
 * Saves and reads the database from a file related to the current world.
 * 
 * @author Adanaran
 * 
 */
public class TPFileHandler {

	private TPDatabase db;
	private File file;
	private String worldName = "";
	private boolean worldHasOldDatabase = false;
	private boolean read = false;

	/**
	 * Creates a new TPFileHandler.
	 * 
	 * @param db
	 *            TPDatabase the database object
	 */
	public TPFileHandler(TPDatabase db) {
		this.db = db;
	}

	/**
	 * Saves the current database to a file.
	 * 
	 * @return true if successful
	 */
	public boolean writeToFile() {
		System.out.println("writeToFile");
		updateFile();
		BufferedWriter writer = null;
		if (file != null) {
			// if (!read) {
			// System.out.println("World has changed!");
			// readFromFile();
			// }
			if (read) {
				try {
					System.out.println("Writing flatfile database to disk...");
					writer = new BufferedWriter(new FileWriter(file));
					for (Map.Entry<ChunkCoordinates, TeleData> entry : TeleportStations.db
							.getDB().entrySet()) {
						System.out.println("Writing: "
								+ entry.getValue().toString());
						writer.write(entry.getValue().toString());
						writer.newLine();
					}
					System.out.println("Flatfile database write finished. ID: "
							+ TeleportStations.db.getDB().toString());
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
						}
					}
				}
				return true;
			} else {
				System.out
						.println("Database was not read before saving - nearly impossible -.-");
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * Reads the database for actual world.
	 * 
	 * @return true if successful
	 */
	public boolean readFromFile() {
		updateFile();
		return readFromFileWithOutCheck();
	}

	private void updateFile() {
		System.out.println("Checking world sync...");
		if (!worldName.equals(TeleportStations.proxy.getWorldName())
				|| file == null) {
			System.out.println("World out of sync! Resync!");
			worldName = TeleportStations.proxy.getWorldName();
//			worldName = TeleportStations.proxy.getWorld().getSaveHandler().getSaveDirectoryName();
			System.out.println("Worldname: " + worldName);
			if (!TeleportStations.proxy.isServer()) {
				file = new File(TeleportStations.dir + "/saves/"
						+ worldName.replaceAll("\\.",	"_") + "/TPDatabase.txt");
				
			} else {
				
				file = new File(TeleportStations.dir +"/" + worldName + "/TPDatabase.txt");
			}
			System.out.println("Filepath: " + file.getAbsolutePath());
			if (!file.exists()) {
				try {
					file.createNewFile();
					worldHasOldDatabase = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (TeleportStations.proxy.isServer()) {
					readFromFileWithOutCheck();
				}
				worldHasOldDatabase = true;
			}
			read = false;
		}
	}

	private boolean readFromFileWithOutCheck() {
		if (!read) {
			System.out.println("Looking for file...");
			BufferedReader reader = null;
			TeleData td;
			if (file != null) {
				String line;
				System.out.println("Reading new data");
				try {
					System.out.println("Reading file data...");
					reader = new BufferedReader(new FileReader(file));
					db.clearDB();
					while ((line = reader.readLine()) != null) {
						System.out.println("Read data: " + line);
						td = new TeleData(line);
						int meta = td.getMeta();
						db.addTP(td);
					}
					System.out.println("Flatfile database read finished.");
					read = true;
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
						}
					}
				}
				return false;
			}
		}
		return false;
	}
}

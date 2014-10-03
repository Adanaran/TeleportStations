package com.dsi11.teleportstations.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.minecraft.util.BlockPos;
import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;

/**
 * The FileHandler for teleporter mod.
 * <p>
 * Saves and reads the database from a file related to the current world.
 * 
 * @author Adanaran
 * 
 */
public class FileHandler {

	private Database db;
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
	public FileHandler(Database db) {
		this.db = db;
	}

	/**
	 * Saves the current database to a file.
	 * 
	 * @return true if successful
	 */
	public boolean writeToFile() {
		TeleportStations.logger.log(Level.INFO, "Saving database to disk");
		updateFile();
		BufferedWriter writer = null;
		if (file != null) {
			if (read) {
				try {
					TeleportStations.logger.log(Level.DEBUG,
							"Writing flatfile database to disk...");
					writer = new BufferedWriter(new FileWriter(file));
					for (Map.Entry<BlockPos, TeleData> entry : TeleportStations.db
							.getDB().entrySet()) {
						TeleportStations.logger.log(Level.TRACE, "Writing: "
								+ entry.getValue().toString());
						writer.write(entry.getValue().toString());
						writer.newLine();
					}
					TeleportStations.logger.log(Level.INFO,
							"Flatfile database write finished. ID: "
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
				TeleportStations.logger
						.log(Level.FATAL,
								"Database was not read before saving - nearly impossible -.-");
				return false;
			}
		} else {
			TeleportStations.logger.log(Level.FATAL,
					"Ouch - something went terribly wrong");
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
		TeleportStations.logger.log(Level.INFO, "Checking world sync...");
		if (!worldName.equals(TeleportStations.proxy.getWorldName())
				|| file == null) {
			TeleportStations.logger.log(Level.DEBUG,
					"World out of sync! Resync!");
			worldName = TeleportStations.proxy.getWorldName();
			TeleportStations.logger.log(Level.TRACE, "Worldname: " + worldName);
			if (!TeleportStations.proxy.isServer()) {
				file = new File("saves/" + worldName.replaceAll("\\.", "_")
						+ "/TPDatabase.txt");

			} else {
				file = new File(worldName + "/TPDatabase.txt");
			}
			TeleportStations.logger.log(Level.TRACE,
					"Filepath: " + file.getAbsolutePath());
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
			TeleportStations.logger.log(Level.INFO,
					"Reading database from file...");
			BufferedReader reader = null;
			TeleData td;
			if (file != null) {
				String line;
				TeleportStations.logger.log(Level.DEBUG, "Reading new data");
				try {
					reader = new BufferedReader(new FileReader(file));
					db.clearDB();
					while ((line = reader.readLine()) != null) {
						TeleportStations.logger.log(Level.TRACE, "Read data: "
								+ line);
						td = new TeleData(line);
						int meta = td.getMeta();
						db.addTeleDataToDatabaseWithOutNotification(td);
					}
					TeleportStations.logger.log(Level.INFO,
							"Finished reading database from file");
					read = true;
					return true;
				} catch (Exception e) {
					TeleportStations.logger.log(Level.FATAL, "Reader crashed: "
							+ e.getMessage());
					// e.printStackTrace();
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

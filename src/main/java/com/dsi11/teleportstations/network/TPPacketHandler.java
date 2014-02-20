package com.dsi11.teleportstations.network;

import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.network.packets.AbstractPacket;
import com.dsi11.teleportstations.network.packets.AbstractTeleDataPacket;
import com.dsi11.teleportstations.network.packets.TPAddPacket;
import com.dsi11.teleportstations.network.packets.TPDatabasePacket;
import com.dsi11.teleportstations.network.packets.TPRemovePacket;
import com.dsi11.teleportstations.network.packets.TPUpdatePacket;

import cpw.mods.fml.relauncher.Side;

/**
 * PacketHandler for teleporter mod.
 * <p>
 * Handles all send and received packets.
 * 
 * @author Adanaran
 */
public class TPPacketHandler {

	public void SendTPAddPacket(TeleData payload, Side targetSide) {
		TPAddPacket packet = new TPAddPacket(payload);
		send(packet, targetSide);
	}

	public void SendTPRemovePacket(TeleData payload, Side targetSide) {
		TPRemovePacket packet = new TPRemovePacket(payload);
		send(packet, targetSide);
	}

	public void sendTPUpdatePacket(TeleData payload, Side targetSide) {
		TPUpdatePacket packet = new TPUpdatePacket(payload);
		send(packet, targetSide);
	}

	public void sendTPDatabasePacket(
			TreeMap<ChunkCoordinates, TeleData> database, EntityPlayerMP player) {
		TPDatabasePacket packet = new TPDatabasePacket(database);
		send(packet, player);
	}

	private void send(AbstractPacket packet, Side targetSide) {
		switch (targetSide) {
		case CLIENT:
			TeleportStations.packetPipeline.sendToAll(packet);
			break;
		case SERVER:
			TeleportStations.packetPipeline.sendToServer(packet);
			break;
		}
	}

	private void send(AbstractPacket packet, EntityPlayerMP player) {
		TeleportStations.packetPipeline.sendTo(packet, player);
	}

}

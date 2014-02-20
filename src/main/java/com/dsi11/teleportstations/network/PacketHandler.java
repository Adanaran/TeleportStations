package com.dsi11.teleportstations.network;

import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.network.packets.AbstractPacket;
import com.dsi11.teleportstations.network.packets.AbstractTeleDataPacket;
import com.dsi11.teleportstations.network.packets.AddPacket;
import com.dsi11.teleportstations.network.packets.DatabasePacket;
import com.dsi11.teleportstations.network.packets.RemovePacket;
import com.dsi11.teleportstations.network.packets.UpdatePacket;

import cpw.mods.fml.relauncher.Side;

/**
 * PacketHandler for teleporter mod.
 * <p>
 * Handles all send and received packets.
 * 
 * @author Adanaran
 */
public class PacketHandler {

	public void SendTPAddPacket(TeleData payload, Side targetSide) {
		AddPacket packet = new AddPacket(payload);
		send(packet, targetSide);
	}

	public void SendTPRemovePacket(TeleData payload, Side targetSide) {
		RemovePacket packet = new RemovePacket(payload);
		send(packet, targetSide);
	}

	public void sendTPUpdatePacket(TeleData payload, Side targetSide) {
		UpdatePacket packet = new UpdatePacket(payload);
		send(packet, targetSide);
	}

	public void sendTPDatabasePacket(
			TreeMap<ChunkCoordinates, TeleData> database, EntityPlayerMP player) {
		DatabasePacket packet = new DatabasePacket(database);
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

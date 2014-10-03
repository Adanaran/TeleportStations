package com.dsi11.teleportstations.network;

import java.util.TreeMap;

import net.minecraft.entity.player.EntityPlayerMP;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.network.message.AddMessage;
import com.dsi11.teleportstations.network.message.DatabaseMessage;
import com.dsi11.teleportstations.network.message.RemoveMessage;
import com.dsi11.teleportstations.network.message.UpdateMessage;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;


/**
 * PacketHandler for teleporter mod.
 * <p>
 * Handles all send and received packets.
 * 
 * @author Adanaran
 */
public class PacketHandler {

	public void sendTPAddMessage(TeleData payload, Side targetSide) {
		AddMessage message = new AddMessage(payload);
		send(message, targetSide);
	}

	public void sendTPRemoveMessage(TeleData payload, Side targetSide) {
		RemoveMessage message = new RemoveMessage(payload);
		send(message, targetSide);
	}

	public void sendTPUpdateMessage(TeleData payload, Side targetSide) {
		UpdateMessage message = new UpdateMessage(payload);
		send(message, targetSide);
	}

	public void sendTPDatabaseMessage(
			TreeMap<BlockPos, TeleData> database, EntityPlayerMP player) {
		DatabaseMessage message = new DatabaseMessage(database);
		send(message, player);
	}

	private void send(IMessage message, Side targetSide) {
		switch (targetSide) {
		case CLIENT:
			TeleportStations.network.sendToAll(message);
			break;
		case SERVER:
			TeleportStations.network.sendToServer(message);
			break;
		}
	}

	private void send(IMessage message, EntityPlayerMP player) {
		TeleportStations.network.sendTo(message, player);
	}

}

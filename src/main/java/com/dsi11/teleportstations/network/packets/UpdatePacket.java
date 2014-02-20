package com.dsi11.teleportstations.network.packets;

import net.minecraft.entity.player.EntityPlayer;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

public class UpdatePacket extends AbstractTeleDataPacket {

	public UpdatePacket(TeleData teleData) {
		super(teleData);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		TeleportStations.db
				.updateTeleDataInDataBaseWithoutNotification(teleData);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		TeleportStations.db
				.updateTeleDataInDataBaseWithNotificationAtServer(teleData);
	}

}

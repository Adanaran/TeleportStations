package com.dsi11.teleportstations.network.packets;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

/**
 * @author Adanaran
 * 
 */
public class TPAddPacket extends AbstractTeleDataPacket {

	public TPAddPacket(TeleData teleData) {
		super(teleData);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		TeleportStations.db.addTeleDataToDatabaseWithOutNotification(teleData);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		TeleportStations.db.addTeleDataToDatabaseWithNotificationAtServer(teleData);
	}

}

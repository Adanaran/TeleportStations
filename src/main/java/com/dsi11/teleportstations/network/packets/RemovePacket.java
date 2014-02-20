package com.dsi11.teleportstations.network.packets;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class RemovePacket extends AbstractTeleDataPacket {

	public RemovePacket(TeleData teleData) {
		super(teleData);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub
		TeleportStations.db
				.removeTeleDataFromDatabaseWithOutNotification(teleData);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		throw new RuntimeException("This packet is not allowed at server side.");
	}

}

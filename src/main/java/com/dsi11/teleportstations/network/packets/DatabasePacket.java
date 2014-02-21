package com.dsi11.teleportstations.network.packets;

import java.util.Map;
import java.util.TreeMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import cpw.mods.fml.common.network.ByteBufUtils;

public class DatabasePacket extends AbstractPacket {

	private TreeMap<ChunkCoordinates, TeleData> dataBase;

	public DatabasePacket() {
	}

	public DatabasePacket(TreeMap<ChunkCoordinates, TeleData> dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		for (Map.Entry<ChunkCoordinates, TeleData> entry : dataBase.entrySet()) {
			String line = entry.getValue().toString();
			ByteBufUtils.writeUTF8String(buffer, line);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.dataBase = new TreeMap<ChunkCoordinates, TeleData>();
		while (buffer.isReadable()) {
			String line = ByteBufUtils.readUTF8String(buffer);
			TeleData teleData = new TeleData(line);
			dataBase.put(new ChunkCoordinates(teleData.posX, teleData.posY,
					teleData.posZ), teleData);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		TeleportStations.db.receiveDB(this.dataBase);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		throw new RuntimeException("This packet is not allowed at server side.");
	}

}

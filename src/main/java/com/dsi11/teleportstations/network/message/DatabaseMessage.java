package com.dsi11.teleportstations.network.message;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.util.BlockPos;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DatabaseMessage extends TSMessage {

	private TreeMap<BlockPos, TeleData> dataBase;

	public DatabaseMessage() {
	}

	public DatabaseMessage(TeleData teleData) {
		throw new RuntimeException("DataBaseMessage does not use teleData.");
	}

	public DatabaseMessage(TreeMap<BlockPos, TeleData> dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.dataBase = new TreeMap<BlockPos, TeleData>();
		while (buffer.isReadable()) {
			String line = ByteBufUtils.readUTF8String(buffer);
			TeleData teleData = new TeleData(line);
			dataBase.put(new BlockPos(teleData.getX(), teleData.getY(),
					teleData.getZ()), teleData);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		for (Map.Entry<BlockPos, TeleData> entry : dataBase.entrySet()) {
			String line = entry.getValue().toString();
			ByteBufUtils.writeUTF8String(buffer, line);
		}
	}

	public static class ServerHandler implements
            IMessageHandler<DatabaseMessage, IMessage> {

		@Override
		public IMessage onMessage(DatabaseMessage message, MessageContext ctx) {
			throw new RuntimeException("This packet is not allowed at server side.");
		}
	}

	public static class ClientHandler implements
			IMessageHandler<DatabaseMessage, IMessage> {

		@Override
		public IMessage onMessage(DatabaseMessage message, MessageContext ctx) {
			TeleportStations.db.receiveDB(message.dataBase);
			return null; // no response in this case
		}
	}
}
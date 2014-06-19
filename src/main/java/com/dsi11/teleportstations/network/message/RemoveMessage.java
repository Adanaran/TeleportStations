package com.dsi11.teleportstations.network.message;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RemoveMessage extends TSMessage {

	public RemoveMessage() {
	}

	public RemoveMessage(TeleData teleData) {
		super(teleData);
	}

	public static class ServerHandler implements
			IMessageHandler<RemoveMessage, IMessage> {

		@Override
		public IMessage onMessage(RemoveMessage message, MessageContext ctx) {
			throw new RuntimeException("This packet is not allowed at server side.");
		}
	}

	public static class ClientHandler implements
			IMessageHandler<RemoveMessage, IMessage> {

		@Override
		public IMessage onMessage(RemoveMessage message, MessageContext ctx) {
			TeleportStations.db
			.removeTeleDataFromDatabaseWithOutNotification(message.teleData);
			return null; // no response in this case
		}
	}
}
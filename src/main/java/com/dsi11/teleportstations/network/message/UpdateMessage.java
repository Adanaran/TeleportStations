package com.dsi11.teleportstations.network.message;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class UpdateMessage extends TSMessage {

	public UpdateMessage() {
	}

	public UpdateMessage(TeleData teleData) {
		super(teleData);
	}

	public static class ServerHandler implements
			IMessageHandler<UpdateMessage, IMessage> {

		@Override
		public IMessage onMessage(UpdateMessage message, MessageContext ctx) {
			TeleportStations.db
			.updateTeleDataInDataBaseWithNotificationAtServer(message.teleData);
			return null; // no response in this case
		}
	}

	public static class ClientHandler implements
			IMessageHandler<UpdateMessage, IMessage> {

		@Override
		public IMessage onMessage(UpdateMessage message, MessageContext ctx) {
			TeleportStations.db
			.updateTeleDataInDataBaseWithoutNotification(message.teleData);
			return null; // no response in this case
		}
	}
}
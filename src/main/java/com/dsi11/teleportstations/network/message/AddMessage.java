package com.dsi11.teleportstations.network.message;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AddMessage extends TSMessage {

	public AddMessage() {
	}

	public AddMessage(TeleData teleData) {
		super(teleData);
	}

	public static class ServerHandler implements
            IMessageHandler<AddMessage, IMessage> {

		@Override
		public IMessage onMessage(AddMessage message, MessageContext ctx) {
			TeleportStations.db
					.addTeleDataToDatabaseWithNotificationAtServer(message.teleData);
			return null; // no response in this case
		}
	}

	public static class ClientHandler implements
			IMessageHandler<AddMessage, IMessage> {

		@Override
		public IMessage onMessage(AddMessage message, MessageContext ctx) {
			TeleportStations.db
					.addTeleDataToDatabaseWithOutNotification(message.teleData);
			return null; // no response in this case
		}
	}
}
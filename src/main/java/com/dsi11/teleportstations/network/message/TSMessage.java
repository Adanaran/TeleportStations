package com.dsi11.teleportstations.network.message;

import net.minecraft.util.ChunkCoordinates;

import com.dsi11.teleportstations.database.TeleData;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class TSMessage implements IMessage {

	protected TeleData teleData;

	public TSMessage() {
	}

	public TSMessage(TeleData teleData) {
		this.teleData = teleData;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int meta = buffer.readInt();
		int worldType = buffer.readInt();

		int t_x = buffer.readInt();
		int t_y = buffer.readInt();
		int t_z = buffer.readInt();

		String name = ByteBufUtils.readUTF8String(buffer);

		if (t_y >= 0) {
			ChunkCoordinates target = new ChunkCoordinates(t_x, t_y, t_z);
			this.teleData = new TeleData(name, x, y, z, meta, worldType, target);
		} else {
			this.teleData = new TeleData(name, x, y, z, meta, worldType);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(teleData.posX);
		buffer.writeInt(teleData.posY);
		buffer.writeInt(teleData.posZ);
		buffer.writeInt(teleData.getMeta());
		buffer.writeInt(teleData.getWorldType());

		ChunkCoordinates ziel = teleData.getZiel();
		if (ziel == null) {
			ziel = new ChunkCoordinates(0, -1, 0);
		}

		buffer.writeInt(ziel.posX);
		buffer.writeInt(ziel.posY);
		buffer.writeInt(ziel.posZ);

		ByteBufUtils.writeUTF8String(buffer, teleData.getName());
	}
}

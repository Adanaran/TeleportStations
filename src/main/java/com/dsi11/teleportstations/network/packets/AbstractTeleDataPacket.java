package com.dsi11.teleportstations.network.packets;

import com.dsi11.teleportstations.database.TeleData;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * AbstractPacket class. Should be the parent of all packets wishing to use the
 * PacketPipeline.
 * 
 * @author sirgingalot
 */
public abstract class AbstractTeleDataPacket extends AbstractPacket {

	protected TeleData teleData;

	public AbstractTeleDataPacket(TeleData teleData) {
		this.teleData = teleData;
	}

	/**
	 * Encode the packet data into the ByteBuf stream. Complex data sets may
	 * need specific data handlers (See
	 * @link{cpw.mods.fml.common.network.ByteBuffUtils})
	 * 
	 * @param ctx
	 *            channel context
	 * @param buffer
	 *            the buffer to encode into
	 */
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
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

	/**
	 * Decode the packet data from the ByteBuf stream. Complex data sets may
	 * need specific data handlers (See
	 * @link{cpw.mods.fml.common.network.ByteBuffUtils})
	 * 
	 * @param ctx
	 *            channel context
	 * @param buffer
	 *            the buffer to decode from
	 */
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
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

	
}
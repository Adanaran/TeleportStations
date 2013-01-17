package adanaran.mods.ts.packethandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.database.TeleData;
import adanaran.mods.ts.entities.TileEntityTele;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * PacketHandler for teleporter mod.
 * <p>
 * Handles all send and received packets.
 * 
 * @author Adanaran
 * 
 */
public class TPPacketHandler implements IPacketHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IPacketHandler#onPacketData(net.minecraft
	 * .src.NetworkManager, net.minecraft.src.Packet250CustomPayload,
	 * cpw.mods.fml.common.network.Player)
	 */
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		System.out.println("Packet empfangen, Channel: " + packet.channel);
		switch (packet.channel) {
		case "tpChange":
			tpChanged(packet);
			break;
		case "tpRemove":
			tpRemoved(packet);
			break;
		case "tpDB":
			dbReceived(packet);
			break;
		case "tpTileEntity":
			tpTEread(packet);
			break;
		default:
			break;
		}
	}

	private void tpTEread(Packet250CustomPayload packet) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		String s = dat.readLine();
		World world = TeleportStations.proxy.getWorld();
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityTele) {
			TileEntityTele tet = (TileEntityTele) te;
			String[] nt = convertString(s).split(";;;");
			tet.setNameAndTarget(nt[0], nt[1]);
		}

	}

	private void dbReceived(Packet250CustomPayload packet) {
		String message = (readPacket(packet));// convertString
		System.out.println("Received database: " + message);
		String[] m = message.split(";;;");
		System.out.println("Received " + m.length + " teleporters.");
		for (int i = 0; i < m.length; i++) {
			System.out.println("Processing " + m[i] + ".");
			TeleportStations.db.receiveChangedTPFromServer(new TeleData(m[i]));
		}
		System.out.println("Database readin finished.");
	}

	private void tpRemoved(Packet250CustomPayload packet) {
		TeleData td = new TeleData((readPacket(packet)));// convertString
		TeleportStations.db.receiveRemovedTPFromServer(td.posX, td.posY,
				td.posZ);
	}

	private void tpChanged(Packet250CustomPayload packet) {

		if (TeleportStations.proxy.isServer()
				|| TeleportStations.proxy.isSinglePlayer()) {
			System.out
					.println("Being (LAN-) Server, distributing Packet to Database");
			TeleportStations.db.receiveChangedTPFromClient(new TeleData(
					(readPacket(packet))));// convertString
		} else {
			System.out
					.println("Being Client w/o internal server, distributing Packet to Database");
			TeleportStations.db.receiveChangedTPFromServer(new TeleData(
					(readPacket(packet))));// convertString
		}
	}

	private String readPacket(Packet250CustomPayload packet) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					bis));
			char[] charArray = reader.readLine().toCharArray();
			StringBuilder message = new StringBuilder();
			for (int i = 1; i < charArray.length; i += 2) {
				message.append(charArray[i]);
			}
			return message.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String convertString(String lineIn) {
		System.out.println("lineIn: " + lineIn);
		StringBuilder lineOut = new StringBuilder();
		for (int i = 1; i < lineIn.length(); i += 2) {
			lineOut.append(lineIn.charAt(i));
		}
		System.out.println("lineOut: " + lineOut);
		return lineOut.toString();
	}

	/**
	 * Creates a new packet for an added teleporter.
	 * 
	 * @param td
	 *            TeleData the dataobject to be sent
	 * @return Packet250CustomPayload the packet to be sent for updating
	 */
	public static Packet250CustomPayload getNewAddTPPacket(TeleData td) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeChars(td.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "tpChange";
		packet.data = bos.toByteArray();
		packet.length = packet.data.length;
		packet.isChunkDataPacket = true;
		return packet;
	}

	/**
	 * Creates a new packet for an removed teleporter.
	 * 
	 * @param td
	 *            TeleData the dataobject to be sent
	 * @return Packet250CustomPayload the packet to be sent for updating
	 */
	public static Packet250CustomPayload getNewRemoveTPPacket(TeleData td) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeChars(td.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "tpRemove";
		packet.data = bos.toByteArray();
		packet.length = packet.data.length;
		packet.isChunkDataPacket = true;
		return packet;
	}

	/**
	 * Creates a new packet for database sync.
	 * 
	 * @param db
	 *            TreeMap<ChunkCoordinates,TeleData> the database to be sent
	 * 
	 * @return Packet250CustomPayload the packet to be sent for updating
	 */
	public static Packet250CustomPayload getNewDatabaseSyncPacket(
			TreeMap<ChunkCoordinates, TeleData> db) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		System.out.println("Building database packet, size: " + TeleportStations.db
				.getDB().size());
		try {
			StringBuilder m = new StringBuilder();
			for (Map.Entry<ChunkCoordinates, TeleData> entry : db.entrySet()) {
				System.out.println("writing TP " + entry.getValue()
						+ " to packet");
				m.append(entry.getValue().toString()).append(";;;");
			}
			m.delete(m.length() - 3, m.length());
			dos.writeChars(m.toString());
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "tpDB";
		packet.data = bos.toByteArray();
		packet.length = packet.data.length;
		packet.isChunkDataPacket = true;
		return packet;
	}

	/**
	 * Creates a new packet for an added teleporter.
	 * 
	 * @param tileEntityTele
	 *            TileEntityTele the tileEntity to be sent
	 * @return Packet250CustomPayload the packet to be sent for updating
	 */
	public static Packet250CustomPayload getPacket(TileEntityTele tileEntityTele) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntityTele.xCoord;
		int y = tileEntityTele.yCoord;
		int z = tileEntityTele.zCoord;
		String s = tileEntityTele.getName() + ";;;"
				+ tileEntityTele.getTarget();

		try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeChars(s);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "tpTileEntity";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}

	public static void sendPacketToClients(Packet packet) {
		Player player;
		MinecraftServer server = TeleportStations.proxy.getServer();
		String[] clients = server.getAllUsernames();
		List list = server.getServerConfigurationManager(server).playerEntityList;
		for (int i = 0; i < list.size(); i++) {
			player = (Player) list.get(i);
			if (player instanceof EntityPlayerMP) {
				EntityPlayer playerMP = (EntityPlayer) player;
				if (!playerMP.username.equals(server.getServerOwner())) {
					((EntityPlayerMP) player).playerNetServerHandler
							.sendPacketToPlayer(packet);
				}
			}
		}
	}
}

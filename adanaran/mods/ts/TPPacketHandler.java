package adanaran.mods.ts;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import adanaran.mods.ts.entities.TileEntityTeleporter;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * PacketHandler for teleporter mod.
 * <p>
 * Handles all received packets.
 * 
 * @author Adanaran
 */
public class TPPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		System.out.println("Packet empfangen, Channel: " + packet.channel);
		switch (packet.channel) {
		case "tpname":
			tpName(packet);
			break;
		case "tptarget":
			tpTarget(packet);
			break;
		default:
			break;
		}
	}

	private void tpTarget(Packet250CustomPayload packet) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int dim = dat.readInt();
		int tx = dat.readInt();
		int ty = dat.readInt();
		int tz = dat.readInt();
		System.out.println("Received target packet for te at " + x + ", " + y
				+ ", " + z+ " request for target te at " + tx+ ", " + ty
				+ ", " + tz);
		World world = TeleportStations.proxy.getWorld(dim);
		System.out.println("World: " + world);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		TileEntity tet = world.getBlockTileEntity(tx, ty, tz);
		if (te instanceof TileEntityTeleporter
				&& tet instanceof TileEntityTeleporter) {
			System.out.println("Setting TP(" + x + "|" + (y-2) + "|" + z
					+ ") target to " + tet);
			TileEntityTeleporter tetp = (TileEntityTeleporter) te;
			TileEntityTeleporter tettp = (TileEntityTeleporter) tet;
			tetp.setTarget(tettp);
		}
		world.markBlockForUpdate(x, y, z);
	}

	private void tpName(Packet250CustomPayload packet) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int dim = dat.readInt();
		String s = dat.readLine();
		System.out.println("Received name packet for tp at " + x + ", " + y
				+ ", " + z);
		World world = TeleportStations.proxy.getWorld(dim);
		System.out.println("World: " + world);
		TileEntity te = world.getBlockTileEntity(x, y + 2, z);
		StringBuilder name = new StringBuilder();
		for (int i = 1; i < s.length(); i += 2) {
			name.append(s.charAt(i));
		}
		if (te instanceof TileEntityTeleporter) {
			System.out.println("Setting TP(" + x + "|" + (y) + "|" + z
					+ ") name to " + name.toString());
			TileEntityTeleporter tetp = (TileEntityTeleporter) te;
			tetp.setName(name.toString());
		}
		world.markBlockForUpdate(x, y + 2, z);
	}
}
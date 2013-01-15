/**
 * 
 */
package adanaran.mods.ts;

import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import adanaran.mods.ts.blocks.BlockTeleTop;
import adanaran.mods.ts.entities.TileEntityTeleporter;

import com.google.common.collect.Lists;

/**
 * 
 * @author Adanaran
 */
public class LoadCallback implements OrderedLoadingCallback {
	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		System.out.println("loading tickets");
		for (Ticket ticket : tickets) {
			int tpX = ticket.getModData().getInteger("tpX");
			int tpY = ticket.getModData().getInteger("tpY");
			int tpZ = ticket.getModData().getInteger("tpZ");
			TileEntityTeleporter tet = (TileEntityTeleporter) world
					.getBlockTileEntity(tpX, tpY, tpZ);
			tet.forceChunkLoading(ticket);
		}
		System.out.println("loading tickets completed");
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world,
			int maxTicketCount) {
		System.out.println("loading tickets");
		List<Ticket> validTickets = Lists.newArrayList();
		for (Ticket ticket : tickets) {
			int tpX = ticket.getModData().getInteger("tpX");
			int tpY = ticket.getModData().getInteger("tpY");
			int tpZ = ticket.getModData().getInteger("tpZ");
			int blockId = world.getBlockId(tpX, tpY, tpZ);
			if (blockId == TeleportStations.blockTeleTop.blockID) {
				validTickets.add(ticket);
				TileEntityTeleporter tet = (TileEntityTeleporter) world
						.getBlockTileEntity(tpX, tpY, tpZ);
				tet.forceChunkLoading(ticket);
			}
		}
		System.out.println("loading tickets completed");
		return validTickets;
	}
}
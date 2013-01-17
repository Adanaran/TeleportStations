package adanaran.mods.ts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * The common proxy for teleporter mod.
 * <p>
 * Used for server-side.
 * 
 * @author Adanaran
 * 
 */
public class CommonProxy implements IGuiHandler {

	/**
	 * Registers render information.
	 * <p>
	 * Only used client-side.
	 */
	public void registerRenderInformation() {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	/**
	 * Registers the TileEntitySpecialRenderer.
	 * <p>
	 * Unused server-side.
	 */
	public void registerTESpRenderer() {
	}

	/**
	 * Gets the current world name.
	 * 
	 * @return String the current world name
	 */
	public String getWorldName() {
		return MinecraftServer.getServer().worldServers[0].getSaveHandler().getSaveDirectoryName();
	}

	/**
	 * Gets the current world.
	 * 
	 * @return World the current world
	 */
	public World getWorld() {
		return MinecraftServer.getServer().worldServers[0].provider.worldObj;
	}

	/**
	 * Determines if client or dedicated server.
	 * 
	 * @return true if client
	 */
	public boolean isSinglePlayer() {
		return false;
	}

	/**
	 * Determines if dedicated server or not.
	 * 
	 * @return true if dedicated server
	 */
	public boolean isServer() {
		return true;
	}

	/**
	 * Gets the server instance.
	 * 
	 * @return MineccraftServer the server instance
	 */
	public MinecraftServer getServer() {
		return MinecraftServer.getServer();
	}
}

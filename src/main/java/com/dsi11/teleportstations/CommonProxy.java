package com.dsi11.teleportstations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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

	public final IIcon[] iconTeleBlockTop = new IIcon[16];
	public final IIcon[] iconTeleBlockTopActive = new IIcon[16];
	
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
	 * Gets the current world name.
	 * 
	 * @return String the current world name
	 */
	public String getWorldName() {
		return MinecraftServer.getServer().worldServers[0].getSaveHandler()
				.getWorldDirectoryName();
	}

	/**
	 * Gets the current world.
	 * 
	 * @param dim
	 *            int the dimension of the world
	 * 
	 * @return World the current world
	 */
	public World getWorld(int dim) {
		return DimensionManager.getWorld(dim);
	}
	
	/**
	 * Gets the client world.
	 * 
	 * @return World the client world (null at server side)
	 */
    public World getClientWorld()
    {
        return null;
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

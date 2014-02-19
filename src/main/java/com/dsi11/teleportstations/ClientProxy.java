package com.dsi11.teleportstations;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import com.dsi11.teleportstations.entities.EntitySpawnPearl;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import com.dsi11.teleportstations.gui.GUIEditTeleName;
import com.dsi11.teleportstations.gui.GUIEditTeleTarget;
import com.dsi11.teleportstations.renderer.RenderSpawnPearl;
import com.dsi11.teleportstations.renderer.TileEntityTeleRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * The client proxy for teleporter mod.
 * <p>
 * Used for client-side.
 * 
 * @author Adanaran
 * 
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture("/adanaran/mods/ts/textures/TS.png");
		MinecraftForgeClient
				.preloadTexture("/adanaran/mods/ts/textures/TeleporterFrame.png");
		MinecraftForgeClient.preloadTexture("/adanaran/mods/ts/textures/TPGUI.png");
		MinecraftForgeClient.preloadTexture("/adanaran/mods/ts/textures/Frame.png");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleporter.class,
				new TileEntityTeleRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpawnPearl.class, new RenderSpawnPearl(38));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return ID == 0 ? new GUIEditTeleName(world, x, y, z, world.getBlockId(
				x, y, z) == 3002 ? "Teleporter" : "Teleporterziel")
				: new GUIEditTeleTarget(world, x, y, z);
	}

	@Override
	public String getWorldName() {
		if (Minecraft.getMinecraft().isSingleplayer()) {
			return Minecraft.getMinecraft().getIntegratedServer()
					.getWorldName();
		} else {
			return super.getWorldName();
		}
	}

	@Override
	public World getWorld(int dim) {
			return DimensionManager.getWorld(dim);
	}

	@Override
	public boolean isSinglePlayer() {
		return Minecraft.getMinecraft().isSingleplayer();
	}

	@Override
	public boolean isServer() {
		return false;
	}

	@Override
	public MinecraftServer getServer() {
		return Minecraft.getMinecraft().getIntegratedServer();
	}

}

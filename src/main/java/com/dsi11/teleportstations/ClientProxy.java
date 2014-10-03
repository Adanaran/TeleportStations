package com.dsi11.teleportstations;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.dsi11.teleportstations.entities.EntitySpawnPearl;
import com.dsi11.teleportstations.entities.TileEntityTeleTarget;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import com.dsi11.teleportstations.gui.GUIEditTeleName;
import com.dsi11.teleportstations.gui.GUIEditTeleTarget;
import com.dsi11.teleportstations.renderer.RenderSpawnPearl;
import com.dsi11.teleportstations.renderer.TileEntityTeleRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

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
		ClientRegistry.bindTileEntitySpecialRenderer(
                TileEntityTeleporter.class, new TileEntityTeleRenderer(0));
		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityTeleTarget.class, new TileEntityTeleRenderer(2));
		/*RenderingRegistry.registerEntityRenderingHandler(
                EntitySpawnPearl.class, new RenderSpawnPearl());*/ // TODO Rendering
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		String t = world.getBlockState(new BlockPos(x,y,z)).getBlock() == TeleportStations.blockTeleporter ? "Teleporter"
				: "Teleporterziel";
		return ID == 0 ? new GUIEditTeleName(world, x, y, z, t)
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
        // TODO return DimensionManager.getWorld(dim);
        return null;
	}

	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
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

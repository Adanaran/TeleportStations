package adanaran.mods.ts;

import adanaran.mods.ts.entities.TileEntityTele;
import adanaran.mods.ts.gui.GUIEditTeleName;
import adanaran.mods.ts.gui.GUIEditTeleTarget;
import adanaran.mods.ts.renderer.TileEntityTeleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;

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
	public World getWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public boolean isSinglePlayer() {
		return Minecraft.getMinecraft().isSingleplayer();
	}

	@Override
	public void registerTESpRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTele.class,
				new TileEntityTeleRenderer());
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

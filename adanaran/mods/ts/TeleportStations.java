package adanaran.mods.ts;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import adanaran.mods.ts.blocks.BlockTeleMid;
import adanaran.mods.ts.blocks.BlockTeleTarget;
import adanaran.mods.ts.blocks.BlockTeleTop;
import adanaran.mods.ts.blocks.BlockTeleporter;
import adanaran.mods.ts.entities.EntitySpawnPearl;
import adanaran.mods.ts.entities.TileEntityTeleporter;
import adanaran.mods.ts.items.ItemSpawnPearl;
import adanaran.mods.ts.items.ItemTeleporter;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Mainclass of Minecraft Mod Teleport Stations.
 * <p>
 * Registers blocks and all other needed modules.
 * 
 * @author Adanaran
 * @author Demitreus
 */
@Mod(modid = "TeleportStations", name = "Teleport Stations", version = "1.0")
@NetworkMod(channels = { "tpname", "tptarget" }, versionBounds = "[1.0,)", clientSideRequired = true, serverSideRequired = false, packetHandler = TPPacketHandler.class)
public class TeleportStations {

	// The mod instance
	@Instance
	public static TeleportStations instance;
	// The sided Proxy instance
	@SidedProxy(clientSide = "adanaran.mods.ts.ClientProxy", serverSide = "adanaran.mods.ts.CommonProxy")
	public static CommonProxy proxy;
	// The blocks
	public static BlockTeleTarget blockTeleTarget;
	public static BlockTeleporter blockTeleporter;
	public static BlockTeleporter blockTeleporterAn;
	public static BlockTeleMid blockTeleMid;
	public static BlockTeleTop blockTeleTop;
	// The items
	public static ItemTeleporter itemTele;
	public static ItemSpawnPearl itemSpawnPearl;
	// The IDs
	private static int idTeleTar;
	private static int idTele;
	private static int idTeleAn;
	private static int idTeleMid;
	private static int idTeleTop;
	private static int idHandTele;
	private static int idSpawnPearl;

	/**
	 * The pre-initialization method.
	 * <p>
	 * Registers the (client-)GUI-handler.
	 * 
	 * @param event
	 *            FMLPreInitializationEvent
	 */
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		Configuration cfg = new Configuration(
				event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			idTeleTar = cfg.getBlock("TeleTarget", 3001).getInt(3001);
			idTele = cfg.getBlock("Teleporter", 3002).getInt(3002);// 3002;
			idTeleAn = cfg.getBlock("TeleporterOn", 3003).getInt(3003);// 3003;
			idTeleMid = cfg.getBlock("TeleMid", 3004).getInt(3004);// 3004;
			idTeleTop = cfg.getBlock("TeleTop", 3005).getInt(3005);// 3005;
			idHandTele = cfg.getBlock("MobileTele", 3006).getInt(3006);// 3006;
			idSpawnPearl = cfg.getBlock("SpawnPearl", 3007).getInt(3007);// 3007;
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e,
					"Failed loading TeleportStations configuration");
		} finally {
			cfg.save();
		}
	}

	/**
	 * The load method.
	 * <p>
	 * Registers the blocks and initializes other modules.
	 * 
	 * @param evt
	 *            FMLInitializationEvent
	 */
	@Init
	public void load(FMLInitializationEvent evt) {
		registerBlockTeleTarget(idTeleTar);
		registerBlockTeleporter(idTele, idTeleAn);
		registerBlockTeleMid(idTeleMid);
		registerBlockTeleTop(idTeleTop);
		registerHandtele(idHandTele);
		registerSpawnPearl(idSpawnPearl);
		proxy.registerRenderInformation();
	}

	private void registerSpawnPearl(int i) {
		itemSpawnPearl = new ItemSpawnPearl(i);
		itemSpawnPearl.setCreativeTab(CreativeTabs.tabTransport);
		EntityRegistry.registerModEntity(EntitySpawnPearl.class, "Spawnpearl",
				3, this, 164, 10, true);
		LanguageRegistry.addName(itemSpawnPearl, "Spawnpearl");
		GameRegistry
				.addRecipe(new ItemStack(itemSpawnPearl),
						new Object[] { "K", "E", Character.valueOf('E'),
								Item.enderPearl, Character.valueOf('K'),
								Item.compass });
		itemSpawnPearl.setItemName("Spawnpearl");
	}

	private void registerHandtele(int i) {
		itemTele = new ItemTeleporter(i);
		itemTele.setCreativeTab(CreativeTabs.tabTransport);
		LanguageRegistry.addName(itemTele, "Handteleporter");
	}

	/**
	 * The post-init method.
	 * <p>
	 * Does nothing.
	 * 
	 * @param evt
	 *            FMLPostInitializationEvent
	 */
	@PostInit
	public void modsLoaded(FMLPostInitializationEvent evt) {
	}

	private void registerBlockTeleTarget(int id) {
		blockTeleTarget = new BlockTeleTarget(id);
		blockTeleTarget.setBlockName("Teleporterziel");
		blockTeleTarget.setCreativeTab(CreativeTabs.tabTransport);
		GameRegistry.registerBlock(blockTeleTarget,
				blockTeleTarget.getBlockName());
		LanguageRegistry.addName(blockTeleTarget, "Teleporterziel");
		LanguageRegistry.instance().addNameForObject(blockTeleTarget, "de_DE",
				"Teleporterziel");
		GameRegistry.addRecipe(new ItemStack(blockTeleTarget), new Object[] {
				"DOD", "ORO", "DOD", Character.valueOf('D'), Block.glass,
				Character.valueOf('O'), Block.obsidian, Character.valueOf('R'),
				Item.redstone });
	}

	private void registerBlockTeleporter(int id, int idAn) {
		blockTeleporter = new BlockTeleporter(id);
		blockTeleporterAn = new BlockTeleporter(idAn);
		blockTeleporter.setBlockName("Teleporter");
		blockTeleporterAn.setBlockUnbreakable();
		blockTeleporter.setCreativeTab(CreativeTabs.tabTransport);
		GameRegistry.registerBlock(blockTeleporter,
				blockTeleporter.getBlockName());
		GameRegistry.registerBlock(blockTeleporterAn,
				blockTeleporterAn.getBlockName());
		LanguageRegistry.addName(blockTeleporter, "Teleporter");
		GameRegistry.addRecipe(new ItemStack(blockTeleporter), new Object[] {
				"DOD", "ORO", "DOD", Character.valueOf('D'), Item.diamond,
				Character.valueOf('O'), Block.obsidian, Character.valueOf('R'),
				Item.redstone });
	}

	private void registerBlockTeleTop(int id) {
		blockTeleTop = new BlockTeleTop(id);
		GameRegistry.registerBlock(blockTeleTop, "Teleporterdeckel");
		LanguageRegistry.addName(blockTeleTop, "Teleporterdeckel");
		blockTeleTop.setBlockUnbreakable().setBlockBounds(0.01f, 0.5f, 0.01f,
				0.99f, 1f, 0.99f);
		GameRegistry.registerTileEntity(TileEntityTeleporter.class,
				"TileEntityTeleporter");
	}

	private void registerBlockTeleMid(int id) {
		blockTeleMid = new BlockTeleMid(id);
		GameRegistry.registerBlock(blockTeleMid, "Teleportermitte");
		LanguageRegistry.addName(blockTeleMid, "Teleportermitte");
		blockTeleMid.setBlockUnbreakable().setBlockBounds(0.5f, 1.5F, 0.5f,
				0.5f, 1.5f, 0.5f);
	}
}

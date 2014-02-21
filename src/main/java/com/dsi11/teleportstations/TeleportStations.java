package com.dsi11.teleportstations;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import com.dsi11.teleportstations.blocks.BlockTeleMid;
import com.dsi11.teleportstations.blocks.BlockTeleTarget;
import com.dsi11.teleportstations.blocks.BlockTeleTop;
import com.dsi11.teleportstations.blocks.BlockTeleporter;
import com.dsi11.teleportstations.database.Database;
import com.dsi11.teleportstations.database.FileHandler;
import com.dsi11.teleportstations.entities.EntitySpawnPearl;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import com.dsi11.teleportstations.items.ItemSpawnPearl;
import com.dsi11.teleportstations.items.ItemTeleporter;
import com.dsi11.teleportstations.network.PacketPipeline;
import com.dsi11.teleportstations.network.PacketHandler;
import com.dsi11.teleportstations.network.PlayerTracker;
import com.dsi11.teleportstations.network.packets.AddPacket;
import com.dsi11.teleportstations.network.packets.DatabasePacket;
import com.dsi11.teleportstations.network.packets.RemovePacket;
import com.dsi11.teleportstations.network.packets.UpdatePacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
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
@Mod(modid = "TeleportStations", name = "Teleport Stations", version = "0.1")
public class TeleportStations {

	// The mod instance
	@Instance
	public static TeleportStations instance;
	// The sided Proxy instance
	@SidedProxy(clientSide = "com.dsi11.teleportstations.ClientProxy", serverSide = "com.dsi11.teleportstations.CommonProxy")
	public static CommonProxy proxy;
	// The database
	public static Database db;
	// The filehandler
	public static FileHandler fh;
	// The playertracker
	public static PlayerTracker pt;
	// The blocks
	public static BlockTeleTarget blockTeleTarget;
	public static BlockTeleporter blockTeleporter;
	public static BlockTeleporter blockTeleporterAn;
	public static BlockTeleMid blockTeleMid;
	public static BlockTeleTop blockTeleTop;
	// The items
	public static ItemTeleporter itemTele;
	public static ItemSpawnPearl itemSpawnPearl;
	// The Minecraft dir
	public static String dir;
	// The Logger
	public static Logger logger;
	// PacketPipeline
	public static final PacketPipeline packetPipeline = new PacketPipeline();

	@EventHandler
	public void initialise(FMLInitializationEvent evt) {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		logger = evt.getModLog();
	}

	/**
	 * The load method.
	 * <p>
	 * Registers the blocks and initializes other modules.
	 * 
	 * @param evt
	 *            FMLInitializationEvent
	 */
	@EventHandler
	public void load(FMLInitializationEvent evt) {
		logger.log(Level.TRACE, "Registering blocks and items");
		registerBlockTeleTarget();
		registerBlockTeleporter();
		registerBlockTeleMid();
		registerBlockTeleTop();
		registerSpawnPearl();
		registerHandtele();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		proxy.registerRenderInformation();
		db = new Database(new PacketHandler());
		fh = new FileHandler(db);
		pt = new PlayerTracker(db, fh);
		FMLCommonHandler.instance().bus().register(pt);
		packetPipeline.initialise();
		// register packets
		packetPipeline.registerPacket(AddPacket.class);
		packetPipeline.registerPacket(UpdatePacket.class);
		packetPipeline.registerPacket(RemovePacket.class);
		packetPipeline.registerPacket(DatabasePacket.class);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		packetPipeline.postInitialise();
	}

	private void registerSpawnPearl() {
		itemSpawnPearl = new ItemSpawnPearl();
		itemSpawnPearl.setCreativeTab(CreativeTabs.tabTransport)
				.setUnlocalizedName("Spawnpearl");
		EntityRegistry.registerModEntity(EntitySpawnPearl.class, "Spawnpearl",
				3, this, 164, 10, true);
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(itemSpawnPearl, "Spawnpearl");
		GameRegistry.registerItem(itemSpawnPearl,
				itemSpawnPearl.getUnlocalizedName());
		GameRegistry.addRecipe(new ItemStack(itemSpawnPearl),
				new Object[] { "K", "E", Character.valueOf('E'),
						Items.ender_pearl, Character.valueOf('K'),
						Items.compass });
	}

	private void registerHandtele() {
		itemTele = new ItemTeleporter();
		itemTele.setCreativeTab(CreativeTabs.tabTransport).setUnlocalizedName(
				"Handteleporter");
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(itemTele, "Handteleporter");
		GameRegistry.registerItem(itemTele, itemTele.getUnlocalizedName());
		GameRegistry.addRecipe(new ItemStack(itemTele), new Object[] { "EPE",
				"EKE", "ETE", Character.valueOf('E'), Items.iron_ingot,
				Character.valueOf('T'), blockTeleporter,
				Character.valueOf('P'), itemSpawnPearl, Character.valueOf('K'),
				Items.coal });
	}

	private void registerBlockTeleTarget() {
		blockTeleTarget = new BlockTeleTarget();
		blockTeleTarget.setBlockName("Teleporterziel");
		blockTeleTarget.setCreativeTab(CreativeTabs.tabTransport);
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(blockTeleTarget, "Teleporterziel");
		// TODO localization auf assets umstellen!
		LanguageRegistry.instance().addNameForObject(blockTeleTarget, "de_DE",
				"Teleporterziel");
		GameRegistry.registerBlock(blockTeleTarget,
				blockTeleTarget.getUnlocalizedName());

		GameRegistry.addRecipe(new ItemStack(blockTeleTarget), new Object[] {
				"DOD", "ORO", "DOD", Character.valueOf('D'), Blocks.glass,
				Character.valueOf('O'), Blocks.obsidian,
				Character.valueOf('R'), Items.redstone });
	}

	private void registerBlockTeleporter() {
		blockTeleporter = new BlockTeleporter();
		blockTeleporterAn = new BlockTeleporter();
		blockTeleporter.setBlockName("Teleporter");
		blockTeleporterAn.setBlockUnbreakable();
		blockTeleporter.setCreativeTab(CreativeTabs.tabTransport);
		GameRegistry.registerBlock(blockTeleporter,
				blockTeleporter.getUnlocalizedName());
		GameRegistry.registerBlock(blockTeleporterAn,
				blockTeleporterAn.getUnlocalizedName());
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(blockTeleporter, "Teleporter");
		GameRegistry.addRecipe(new ItemStack(blockTeleporter), new Object[] {
				"DOD", "ORO", "DOD", Character.valueOf('D'), Items.diamond,
				Character.valueOf('O'), Blocks.obsidian,
				Character.valueOf('R'), Items.redstone });
	}

	private void registerBlockTeleTop() {
		blockTeleTop = new BlockTeleTop();
		GameRegistry.registerBlock(blockTeleTop, "Teleporterdeckel");
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(blockTeleTop, "Teleporterdeckel");
		blockTeleTop.setBlockUnbreakable().setBlockBounds(0.01f, 0.5f, 0.01f,
				0.99f, 1f, 0.99f);
		GameRegistry.registerTileEntity(TileEntityTeleporter.class,
				"TileEntityTeleporter");
	}

	private void registerBlockTeleMid() {
		blockTeleMid = new BlockTeleMid();
		GameRegistry.registerBlock(blockTeleMid, "Teleportermitte");
		// TODO localization auf assets umstellen!
		LanguageRegistry.addName(blockTeleMid, "Teleportermitte");
		blockTeleMid.setBlockUnbreakable().setBlockBounds(0.5f, 1.5F, 0.5f,
				0.5f, 1.5f, 0.5f);
	}
}
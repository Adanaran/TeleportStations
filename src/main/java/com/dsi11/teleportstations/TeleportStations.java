package com.dsi11.teleportstations;

import com.dsi11.teleportstations.blocks.BlockTeleMid;
import com.dsi11.teleportstations.blocks.BlockTeleTarget;
import com.dsi11.teleportstations.blocks.BlockTeleTop;
import com.dsi11.teleportstations.blocks.BlockTeleporter;
import com.dsi11.teleportstations.database.Database;
import com.dsi11.teleportstations.database.FileHandler;
import com.dsi11.teleportstations.entities.EntitySpawnPearl;
import com.dsi11.teleportstations.entities.TileEntityTeleTarget;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;
import com.dsi11.teleportstations.items.ItemSpawnPearl;
import com.dsi11.teleportstations.items.ItemTeleporter;
import com.dsi11.teleportstations.network.PacketHandler;
import com.dsi11.teleportstations.network.PlayerTracker;
import com.dsi11.teleportstations.network.message.AddMessage;
import com.dsi11.teleportstations.network.message.DatabaseMessage;
import com.dsi11.teleportstations.network.message.RemoveMessage;
import com.dsi11.teleportstations.network.message.UpdateMessage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Mainclass of Minecraft ExampleMod Teleport Stations.
 * <p/>
 * Registers blocks and all other needed modules.
 *
 * @author Adanaran
 * @author Demitreus
 */
@Mod(modid = "TeleportStations", name = "Teleport Stations", version = "0.3")
public class TeleportStations {

    // The mod instance
    @Mod.Instance("TeleportStations")
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
    // The Logger
    public static Logger logger;
    // PacketPipeline
    public static SimpleNetworkWrapper network;
    public static final ResourceLocation tileEntityTexture = new ResourceLocation("teleportstations", "textures/model/Frame.png");
    public static final ResourceLocation guiTexture = new ResourceLocation("teleportstations", "textures/gui/TPGUI.png");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.log(Level.TRACE, "Registering blocks and items");
        registerBlockTeleMid();
        registerBlockTeleTarget();
        registerBlockTeleporter();
        registerBlockTeleTop();
        registerSpawnPearl();
        registerHandtele();

        network = NetworkRegistry.INSTANCE.newSimpleChannel("teleportStations");
        network.registerMessage(AddMessage.ServerHandler.class, AddMessage.class, 0, Side.SERVER);
        network.registerMessage(AddMessage.ClientHandler.class, AddMessage.class, 1, Side.CLIENT);

        network.registerMessage(RemoveMessage.ServerHandler.class, RemoveMessage.class, 2, Side.SERVER);
        network.registerMessage(RemoveMessage.ClientHandler.class, RemoveMessage.class, 3, Side.CLIENT);

        network.registerMessage(UpdateMessage.ServerHandler.class, UpdateMessage.class, 4, Side.SERVER);
        network.registerMessage(UpdateMessage.ClientHandler.class, UpdateMessage.class, 5, Side.CLIENT);

        network.registerMessage(DatabaseMessage.ServerHandler.class, DatabaseMessage.class, 6, Side.SERVER);
        network.registerMessage(DatabaseMessage.ClientHandler.class, DatabaseMessage.class, 7, Side.CLIENT);
    }

    /**
     * The load method.
     * <p/>
     * Registers the blocks and initializes other modules.
     *
     * @param evt FMLInitializationEvent
     */
    @SuppressWarnings("UnusedParameters")
    @Mod.EventHandler
    public void load(FMLInitializationEvent evt) {

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.registerRenderInformation();
        db = new Database(new PacketHandler());
        fh = new FileHandler(db);
        pt = new PlayerTracker(db, fh);
        FMLCommonHandler.instance().bus().register(pt);
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
        GameRegistry.addRecipe(new ItemStack(itemSpawnPearl), "K", "E", 'E', Items.ender_pearl, 'K', Items.compass);
    }

    private void registerHandtele() {
        itemTele = new ItemTeleporter();
        itemTele.setCreativeTab(CreativeTabs.tabTransport).setUnlocalizedName(
                "Handteleporter");
        // TODO localization auf assets umstellen!
        LanguageRegistry.addName(itemTele, "Handteleporter");
        GameRegistry.registerItem(itemTele, itemTele.getUnlocalizedName());
        GameRegistry.addRecipe(new ItemStack(itemTele), "EPE", "EKE", "ETE", 'E', Items.iron_ingot, 'T',
                blockTeleporter, 'P', itemSpawnPearl, 'K', Items.coal);
    }

    private void registerBlockTeleTarget() {
        blockTeleTarget = new BlockTeleTarget();
        blockTeleTarget.setCreativeTab(CreativeTabs.tabTransport);
        // TODO localization auf assets umstellen!
        LanguageRegistry.addName(blockTeleTarget, "Teleporterziel");
        // TODO localization auf assets umstellen!
        LanguageRegistry.instance().addNameForObject(blockTeleTarget, "de_DE",
                "Teleporterziel");
        GameRegistry.registerBlock(blockTeleTarget,
                blockTeleTarget.getUnlocalizedName());

        GameRegistry.addRecipe(new ItemStack(blockTeleTarget), "DOD", "ORO", "DOD", 'D', Blocks.glass, 'O',
                Blocks.obsidian, 'R', Items.redstone);
        GameRegistry.registerTileEntity(TileEntityTeleTarget.class, "TileEntityTeleTarget");
    }

    private void registerBlockTeleporter() {
        blockTeleporter = new BlockTeleporter(false);
        blockTeleporterAn = new BlockTeleporter(true);
        blockTeleporter.setCreativeTab(CreativeTabs.tabTransport);
        GameRegistry.registerBlock(blockTeleporter,
                blockTeleporter.getUnlocalizedName());
        GameRegistry.registerBlock(blockTeleporterAn,
                blockTeleporterAn.getUnlocalizedName());
        // TODO localization auf assets umstellen!
        LanguageRegistry.addName(blockTeleporter, "Teleporter");
        GameRegistry.addRecipe(new ItemStack(blockTeleporter), "DOD", "ORO", "DOD", 'D', Items.diamond, 'O',
                Blocks.obsidian, 'R', Items.redstone);
    }

    private void registerBlockTeleTop() {
        blockTeleTop = new BlockTeleTop();
        GameRegistry.registerBlock(blockTeleTop, "Teleporterdeckel");
        // TODO localization auf assets umstellen!
        LanguageRegistry.addName(blockTeleTop, "Teleporterdeckel");
        GameRegistry.registerTileEntity(TileEntityTeleporter.class,
                "TileEntityTeleporter");
    }

    private void registerBlockTeleMid() {
        blockTeleMid = new BlockTeleMid();
        GameRegistry.registerBlock(blockTeleMid, "Teleportermitte");
        // TODO localization auf assets umstellen!
        LanguageRegistry.addName(blockTeleMid, "Teleportermitte");
    }
}
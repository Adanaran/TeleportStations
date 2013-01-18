package adanaran.mods.ts.items;

import java.util.logging.Level;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import adanaran.mods.ts.TeleportStations;

/**
 * Handteleporter zur mobilen Teleportation.
 * <p>
 * Client File
 * 
 * @author Demitreus
 * 
 */

public class ItemTeleporter extends Item implements ICommandSender {
	private static ChunkCoordinates target;
	private boolean porting = false;

	public ItemTeleporter(int par1) {
		super(par1);
		maxStackSize = 1;
		setMaxDamage(200);
	}

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		// par2World.worldProvider.worldType; //Ergibt 0 bei normaler Welt, -1
		// bei Netherwelt :D
		if (par4 > 71950) {
			target = null;
			par3EntityPlayer.openGui(TeleportStations.instance, 1, par2World,
					-1, -1, -1);
		} else if (par4 < 71950 && target != null && !par2World.isRemote) {
			par3EntityPlayer.setPositionAndUpdate(target.posX + 0.5,
					target.posY, target.posZ + 0.5);
			par1ItemStack.damageItem(1, par3EntityPlayer);
			par2World.playSoundAtEntity(par3EntityPlayer, "portal.portal",
					1.0F, 1.0F);
		}
	}

	public static void setTarget(ChunkCoordinates tarCoords) {
		target = tarCoords;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 0x11940;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.block;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack,
				getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public int getIconFromDamage(int par1) {
		if (ModLoader.getMinecraftInstance().thePlayer.getItemInUseDuration() <= 50) {
			return 36;
		} else {
			return 37;
		}
	}

	@Override
	public String getTextureFile() {
		return "/adanaran/mods/ts/textures/TS.png";
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	public int getItemEnchantability() {
		return 1;
	}

	/**
	 * Teleports the player to the target that is set.
	 * 
	 * @param entity
	 *            EntityPlayer to be teleported
	 */
	public void tp(EntityPlayer entity) {
		if (!porting && target != null) {
			porting = true;
			ICommandManager cm = TeleportStations.proxy.getServer()
					.getCommandManager();
			cm.executeCommand(
					this,
					new StringBuilder("/tp ").append(entity.getEntityName())
							.append(" ").append(target.posX + 0.5).append(" ")
							.append(target.posY - 2).append(" ")
							.append(target.posZ + 0.5).toString());
			TeleportStations.logger.log(Level.FINE,
					"teleported " + entity.getEntityName() + " from "
							+ entity.posX + "|" + entity.posY + "|"
							+ entity.posZ + " to " + target.posX + "|"
							+ (target.posY - 2) + "|" + target.posZ);
		}
		porting = false;
	}

	@Override
	public String getCommandSenderName() {
		return "Teleport Stations";
	}

	@Override
	public void sendChatToPlayer(String var1) {
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return true;
	}

	@Override
	public String translateString(String var1, Object... var2) {
		return var1;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}
}

package com.dsi11.teleportstations.items;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.dsi11.teleportstations.TeleportStations;

/**
 * Handteleporter zur mobilen Teleportation.
 * <p>
 * Client File
 * 
 * @author Demitreus
 * 
 */

public class ItemTeleporter extends Item implements ICommandSender {
	private static BlockPos target;
	private boolean porting = false;
	/*@SideOnly(Side.CLIENT)
	private IIcon itemTeleIcon;
	@SideOnly(Side.CLIENT)
	private IIcon itemTeleIconActive;*/

	public ItemTeleporter() {
		super();
		maxStackSize = 1;
		setMaxDamage(200);
	}

	/*@Override
	public void registerIcons(IIconRegister IIR) {
		itemTeleIcon = IIR.registerIcon("teleportstations:ItemTele");
		itemTeleIconActive = IIR.registerIcon("teleportstations:ItemTeleAn");
	}*/

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		// par2World.worldProvider.worldType; //Ergibt 0 bei normaler Welt, -1
		// bei Netherwelt :D
		if (par4 > 71950) {
			target = null;
			par3EntityPlayer.openGui(TeleportStations.instance, 1, par2World,
					-1, -1, -1);
		} else if (par4 < 71950 && target != null && !par2World.isRemote) {
//			par3EntityPlayer.setPositionAndUpdate(target.posX + 0.5,
//					target.posY, target.posZ + 0.5);
//			par1ItemStack.damageItem(1, par3EntityPlayer);
//			par2World.playSoundAtEntity(par3EntityPlayer, "portal.portal",
//					1.0F, 1.0F);
			tp(par3EntityPlayer);
		}
	}

	public static void setTarget(BlockPos tarCoords) {
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
		return EnumAction.BLOCK;
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

	/*@Override
	public IIcon getIconFromDamage(int par1) {
		if (Minecraft.getMinecraft().thePlayer.getItemInUseDuration() <= 50) {
			return itemTeleIcon;
		} else {
			return itemTeleIconActive;
		}
	}*/

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
			TeleportStations.logger.log(Level.INFO, "Target: x " + target.getX()
					+ " y " + target.getY() + " z " + target.getZ());
			ICommandManager cm = TeleportStations.proxy.getServer()
					.getCommandManager();
			String command = new StringBuilder("/tp ")
					.append(entity.getGameProfile().getName()).append(" ")
					.append(target.getX() + 0.5).append(" ")
					.append(target.getY()).append(" ")
					.append(target.getZ() + 0.5).toString();
			TeleportStations.logger.log(Level.INFO, "Executing command "
					+ command);
			cm.executeCommand(this, command);
			porting = false;
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return true;
	}

    @Override
    public BlockPos getPosition() {
        return null;
    }

    @Override
    public Vec3 getPositionVector() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
	public void addChatMessage(IChatComponent var1) {
	}

	@Override
	public World getEntityWorld() {
		return null;
	}

    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }

    @Override
    public boolean sendCommandFeedback() {
        return false;
    }

    @Override
    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {

    }
}

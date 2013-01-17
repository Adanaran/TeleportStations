package adanaran.mods.ts.items;

import adanaran.mods.ts.TeleportStations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * Handteleporter zur mobilen Teleportation.
 * <p>
 * Client File
 * 
 * @author Demitreus
 * 
 */

public class ItemTeleporter extends Item {
	private static ChunkCoordinates ziel;

	public ItemTeleporter(int par1) {
		super(par1);
		maxStackSize = 1;
		setMaxDamage(200);
	}

	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		// System.out.println(par2World.worldProvider.worldType); //Ergibt 0 bei
		// normaler Welt, -1 bei Netherwelt :D
		if (par4 > 71950) {
			ziel = null;
			par3EntityPlayer.openGui(TeleportStations.instance, 1, par2World,
					-1, -1, -1);
		} else if (par4 < 71950 && ziel != null && !par2World.isRemote) {
			par3EntityPlayer.setPositionAndUpdate(ziel.posX + 0.5,
					ziel.posY, ziel.posZ + 0.5);
			par1ItemStack.damageItem(1, par3EntityPlayer);
			par2World.playSoundAtEntity(par3EntityPlayer, "portal.portal",
					1.0F, 1.0F);
		}
	}

	public static void setTarget(ChunkCoordinates zielcc) {
		ziel = zielcc;
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
		// TODO BlockTeleTarget.updateCC(par2World);
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
}


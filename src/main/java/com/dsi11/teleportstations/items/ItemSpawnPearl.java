package com.dsi11.teleportstations.items;

import com.dsi11.teleportstations.entities.EntitySpawnPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 
 * @author Demitreus Client File
 */
public class ItemSpawnPearl extends Item {

	public ItemSpawnPearl() {
		super();
		maxStackSize = 16;
		// TODO: this.iconIndex = 38;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		if (par3EntityPlayer.ridingEntity != null) {
			return par1ItemStack;
		}

		par1ItemStack.stackSize--;
		par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F,
				0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!par2World.isRemote) {
			par2World.spawnEntityInWorld(new EntitySpawnPearl(par2World,
					par3EntityPlayer));
		}
		return par1ItemStack;
	}

	// TODO textures
	// @Override
	// public String getTextureFile() {
	// return "/adanaran/mods/ts/textures/TS.png";
	// }
}
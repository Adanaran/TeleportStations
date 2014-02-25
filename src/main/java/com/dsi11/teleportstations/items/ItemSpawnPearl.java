package com.dsi11.teleportstations.items;

import com.dsi11.teleportstations.entities.EntitySpawnPearl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * 
 * @author Demitreus Client File
 */
public class ItemSpawnPearl extends Item {

	@SideOnly(Side.CLIENT)
	private IIcon pearlIcon;

	public ItemSpawnPearl() {
		super();
		maxStackSize = 16;
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

	@Override
	public void registerIcons(IIconRegister IIR) {
		pearlIcon = IIR.registerIcon("teleportstations:Spawnpearl");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player,
			ItemStack usingItem, int useRemaining) {
		return pearlIcon;
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return pearlIcon;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
		return pearlIcon;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pearlIcon;
	}
	
	
	
	

}
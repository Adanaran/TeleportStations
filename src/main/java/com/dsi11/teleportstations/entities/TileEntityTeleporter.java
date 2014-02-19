package com.dsi11.teleportstations.entities;

import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.packethandler.TPPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * Tileentity for teleporters, placed in top of it.
 * <p>
 * TileEntity that stores name and target as stringarray for rendering at the
 * top of the teleporter.
 * 
 * @author Demitreus
 */
public class TileEntityTeleporter extends TileEntity implements ICommandSender {

	/**
	 * An array of two strings storing the name and the current target of the
	 * teleporter.
	 */
	public String[] nameAndTarget = new String[] { "", "" };
	// To prevent multiple command execution per teleport
	private boolean porting = false;

	/**
	 * Updates the Entity, called automatically.
	 * 
	 * The entity looks up name and target in database and stores it.
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		try {
			nameAndTarget[0] = TeleportStations.db.getNameByCoords(this.xCoord,
					this.yCoord - 2, this.zCoord);
			nameAndTarget[1] = TeleportStations.db
					.getNameByCoords(TeleportStations.db
							.getZielByCoords(new ChunkCoordinates(this.xCoord,
									this.yCoord - 2, this.zCoord)));
		} catch (Exception e) {
			// Can cause Nullpointer, can be ignored
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 * 
	 * Necessary?
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("Name", this.nameAndTarget[0]);
		par1NBTTagCompound.setString("Ziel", this.nameAndTarget[1]);
	}

	/**
	 * Reads a tile entity from NBT.
	 * 
	 * Necessary?
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.nameAndTarget[0] = par1NBTTagCompound.getString("Name");
		this.nameAndTarget[1] = par1NBTTagCompound.getString("Ziel");
	}

	/**
	 * Sets name and target of teleporter in tileentity name stringarray.
	 * 
	 * @param String
	 *            name
	 * @param String
	 *            target
	 */

	public void setNameAndTarget(String name, String target) {
		nameAndTarget[0] = name;
		nameAndTarget[1] = target;
	}

	/**
	 * Gets the teleporter's name.
	 * 
	 * @return String name
	 */

	public String getName() {
		this.updateEntity();
		return nameAndTarget[0];
	}

	/**
	 * Gets the teleporter's target.
	 * 
	 * @return String target
	 */

	public String getTarget() {
		return nameAndTarget[1];
	}

	@Override
	public int getBlockMetadata() {
		return super.getBlockMetadata();
	}

	@Override
	public Block getBlockType() {
		return super.getBlockType();
	}

	/**
	 * Teleports the player to the target that is set.
	 * 
	 * @param entity
	 *            EntityPlayer to be teleported
	 */
	public void tp(EntityPlayer entity) {
		ChunkCoordinates ziel = TeleportStations.db
				.getZielByCoords(new ChunkCoordinates(this.xCoord,
						this.yCoord - 2, this.zCoord));
		if (!porting && ziel != null) {
			porting = true;
			MinecraftServer server = TeleportStations.proxy.getServer();
			if (server != null) {
				TeleportStations.logger.log(Level.INFO, "Ziel: x " + ziel.posX
						+ " y " + ziel.posY + " z " + ziel.posZ);
				ICommandManager commandManager = server.getCommandManager();
				String command = new StringBuilder("/tp ")
						.append(entity.getGameProfile().getName()).append(" ")
						.append(ziel.posX + 0.5).append(" ")
						.append(ziel.posY + entity.getEyeHeight()).append(" ")
						.append(ziel.posZ + 0.5).toString();
				TeleportStations.logger.log(Level.INFO, "Executing command "
						+ command);
				commandManager.executeCommand(this, command);
			}
		}
		porting = false;
	}

	@Override
	public String getCommandSenderName() {
		return "Teleport Stations";
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return true;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public IChatComponent func_145748_c_() {
		return null;
	}

	@Override
	public void addChatMessage(IChatComponent var1) {
	}

	@Override
	public World getEntityWorld() {
		return this.worldObj;
	}
}

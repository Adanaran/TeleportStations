package com.dsi11.teleportstations.entities;

import org.apache.logging.log4j.Level;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;

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
public class TileEntityTeleTarget extends TileEntity {


}

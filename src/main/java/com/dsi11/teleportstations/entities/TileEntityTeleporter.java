package com.dsi11.teleportstations.entities;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import net.minecraft.block.Block;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

/**
 * Tileentity for teleporters, placed in top of it.
 * <p/>
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
    private String[] nameAndTarget = new String[]{"", ""};
    // To prevent multiple command execution per teleport
    private boolean porting = false;

    /**
     * Updates the Entity, called automatically.
     * <p/>
     * The entity looks up name and target in database and stores it.
     */
    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        TeleData self = TeleportStations.db.getTeleDataByCoords(this.pos.getX(),
                this.pos.getY() - 2, this.pos.getZ());
        TeleData target = TeleportStations.db
                .getZielByCoords(new BlockPos(this.pos.getX(),
                        this.pos.getY() - 2, this.pos.getZ()));
        if (self != null) {
            nameAndTarget[0] = self.getName();
        } else {
            nameAndTarget[0] = "";
        }
        if (target != null) {
            nameAndTarget[1] = target.getName();
        } else {
            nameAndTarget[1] = "";
        }
    }

    /**
     * Writes a tile entity to NBT.
     * <p/>
     * Necessary?
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Name", this.nameAndTarget[0]);
        par1NBTTagCompound.setString("Ziel", this.nameAndTarget[1]);
    }

    /**
     * Reads a tile entity from NBT.
     * <p/>
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
     * @param name   name
     * @param target target
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
        // this.updateEntity();
        return nameAndTarget[0];
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
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
     * @param entity EntityPlayer to be teleported
     */
    public void tp(EntityPlayer entity) {
        TeleData ziel = TeleportStations.db
                .getZielByCoords(new BlockPos(this.pos.getX(),
                        this.pos.getY() - 2, this.pos.getZ()));
        if (!porting && ziel != null) {
            porting = true;
            MinecraftServer server = TeleportStations.proxy.getServer();
            if (server != null) {
                TeleportStations.logger.log(Level.INFO, "Ziel: x " + ziel.getPosition().getX()
                        + " y " + ziel.getPosition().getY() + " z " + ziel.getPosition().getZ());
                ICommandManager commandManager = server.getCommandManager();
                String command = new StringBuilder("/tp ")
                        .append(entity.getGameProfile().getName()).append(" ")
                        .append(ziel.getPosition().getX() + 0.5).append(" ").append(ziel.getPosition().getY())
                        .append(" ").append(ziel.getPosition().getZ() + 0.5).toString();
                TeleportStations.logger.log(Level.INFO, "Executing command "
                        + command);
                commandManager.executeCommand(this, command);
            }
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
    public void addChatMessage(IChatComponent var1) {
    }

    @Override
    public World getEntityWorld() {
        return this.worldObj;
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

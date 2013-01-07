package adanaran.mods.ts.entities;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import adanaran.mods.ts.TeleportStations;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * TileEntity for TeleportStations
 * <p>
 * Stores the data of one teleporter.
 * 
 * @author Adanaran
 */
public class TileEntityTeleporter extends TileEntity implements ICommandSender {

	// Data storage
	private String name = "";
	private TileEntityTeleporter target = null;
	private int meta = 0;
	private int worldType;
	// To prevent multiple command execution per teleport
	private boolean porting = false;

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		Packet132TileEntityData packet = new Packet132TileEntityData(xCoord,
				yCoord, zCoord, 5, nbt);
		return packet;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.customParam1);
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setString("Name", name.equals("") ? "knochkeinname"
				: name);
		par1NBTTagCompound.setString("Ziel", target == null ? "knochkeinziel"
				: "khateinziel");
		if (target != null) {
			par1NBTTagCompound.setInteger("target_x", target.xCoord);
			par1NBTTagCompound.setInteger("target_y", target.yCoord);
			par1NBTTagCompound.setInteger("target_z", target.zCoord);
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		name = par1NBTTagCompound.getString("Name");
		String s = par1NBTTagCompound.getString("Ziel");
		if (s.equals("khateinziel")) {
			int tx = par1NBTTagCompound.getInteger("target_x");
			int ty = par1NBTTagCompound.getInteger("target_y");
			int tz = par1NBTTagCompound.getInteger("target_z");
			new UpdateTargetThread(this, tx, ty, tz).start();
		} else {
			target = null;
		}
	}

	/**
	 * Updates the entity.
	 * 
	 * @param worldType int world type
	 */
	@SideOnly(Side.CLIENT)
	public void update(int worldType) {
		this.worldType = worldType;
	}

	/**
	 * Gets the teleporter's name.
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the teleporter's target.
	 * 
	 * @return
	 */
	public TileEntityTeleporter getTarget() {
		return this.target;
	}

	/**
	 * Gets the teleporter's meta.
	 * 
	 * @return int meta value
	 */
	public int getMeta() {
		return meta;
	}

	/**
	 * Gets the teleporter's worldtype.
	 * 
	 * @return int the world type
	 */
	public int getWorldType() {
		return worldType;
	}

	/**
	 * Sets the teleporter's name. 
	 * 
	 * @param name String the name to be set 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the teleporter's target.
	 * 
	 * @param target {@link#TileEntityTeleporter} the target to be set
	 */
	public void setTarget(TileEntityTeleporter target) {
		this.target = target;
	}

	/**
	 * Sets the teleporter's meta 
	 * 
	 * @param meta int the meta to be set
	 */
	public void setMeta(int meta) {
		this.meta = meta;
	}

	/**
	 * Gets the teleporter's target's name.
	 * 
	 * @return String target name
	 */
	public String getTargetName() {
		if (target != null) {
			return target.getName();
		} else
			return "";
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
			System.out.println("Executing tp command");
			cm.executeCommand(
					this,
					new StringBuilder("/tp ").append(entity.getEntityName())
							.append(" ").append(target.xCoord + 0.5)
							.append(" ")
							.append(target.yCoord - 2 /*+ entity.getEyeHeight()*/)
							.append(" ").append(target.zCoord + 0.5).toString());
		}
		porting = false;
	}

	/**
	 * Converts the data of this tile entity into a String.
	 * <p>
	 * Overriding object's toString()-method.
	 * 
	 * @return String name;;i;;j;;k;;meta;;null oder
	 *         name;;i;;j;;k;;meta;;target.i;;target.j;;target.k
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(";;").append(this.xCoord).append(";;")
				.append(this.yCoord).append(";;").append(this.yCoord)
				.append(";;").append(meta).append(";;").append(worldType)
				.append(";;");
		if (target != null) {
			builder.append(target.xCoord).append(";;").append(target.yCoord)
					.append(";;").append(target.zCoord);
		} else {
			builder.append("null");
		}
		return builder.toString();
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
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TileEntityTeleporter)) {
			return false;
		} else {
			TileEntityTeleporter te = (TileEntityTeleporter) obj;
			return this.xCoord == te.xCoord && this.yCoord == te.yCoord
					&& this.zCoord == te.zCoord;
		}
	}
}

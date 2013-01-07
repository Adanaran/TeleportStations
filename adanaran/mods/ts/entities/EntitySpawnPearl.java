package adanaran.mods.ts.entities;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * 
 * @author Demitreus
 * Client File
 */
public class EntitySpawnPearl extends EntityThrowable {

	public EntitySpawnPearl(World par1World) {
		super(par1World);
	}

	public EntitySpawnPearl(World par1World, EntityLiving par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	public EntitySpawnPearl(World par1World, double par2, double par4,
			double par6) {
		super(par1World, par2, par4, par6);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		World theWorld = ModLoader.getMinecraftInstance().theWorld;
		EntityPlayer thePlayer = ModLoader.getMinecraftInstance().thePlayer;
		Minecraft mc = ModLoader.getMinecraftInstance();
		InventoryPlayer inv = thePlayer.inventory;

		if (par1MovingObjectPosition.entityHit != null) {
			if (!par1MovingObjectPosition.entityHit.attackEntityFrom(
					DamageSource.causeThrownDamage(this, getThrower()), 0))
				;
		}

		for (int i = 0; i < 32; i++) {
			worldObj.spawnParticle("portal", posX, posY + rand.nextDouble()
					* 2D, posZ, rand.nextGaussian(), 0.0D, rand.nextGaussian());
		}

		if (!worldObj.isRemote) {
			if (getThrower() != null) {
				ChunkCoordinates SC = thePlayer.getBedLocation();
				if (SC == null) {
					SC = theWorld.getSpawnPoint();
					while(SC.posY < 66 || theWorld.getBlockId(SC.posX, SC.posY, SC.posZ) != 0 || theWorld.getBlockId(SC.posX, SC.posY + 1, SC.posZ) != 0){
						SC.posY++;
					}
				}
				thePlayer.setPositionAndUpdate(SC.posX + 0.5, SC.posY + 0, SC.posZ + 0.5);
			}
			setDead();
		}
	}
}


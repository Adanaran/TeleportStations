package com.dsi11.teleportstations.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.items.ItemSpawnPearl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for Spawnpearl-Entity.
 * <p>
 * Renders the flying pearl with some effects.
 * 
 * @author Demitreus
 */
@SideOnly(Side.CLIENT)
public class RenderSpawnPearl extends Render {

	/**
	 * Creates a new Renderer.
	 * 
	 * @param par1
	 *            int position in texturefile
	 */
	public RenderSpawnPearl() {
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4,
			double par6, float par8, float par9) {

		IIcon iicon = TeleportStations.itemSpawnPearl.getIconFromDamage(0);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(par1Entity);
		Tessellator var10 = Tessellator.instance;
		this.func_77026_a(var10, iicon);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	private void func_77026_a(Tessellator par1Tessellator, IIcon iicon) {
		float f = 0;
		float f1 = 1;
		float f2 = 0;
		float f3 = 1;
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F,
				0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par1Tessellator.addVertexWithUV((double) (0.0F - f5),
				(double) (0.0F - f6), 0.0D, (double) f, (double) f3);
		par1Tessellator.addVertexWithUV((double) (f4 - f5),
				(double) (0.0F - f6), 0.0D, (double) f1, (double) f3);
		par1Tessellator.addVertexWithUV((double) (f4 - f5), (double) (f4 - f6),
				0.0D, (double) f1, (double) f2);
		par1Tessellator.addVertexWithUV((double) (0.0F - f5),
				(double) (f4 - f6), 0.0D, (double) f, (double) f2);
		par1Tessellator.draw();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return new ResourceLocation("teleportstations:textures/items/Spawnpearl.png");
	}
}

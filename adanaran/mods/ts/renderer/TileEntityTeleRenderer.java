package adanaran.mods.ts.renderer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import adanaran.mods.ts.entities.TileEntityTeleporter;

//TODO JAVADOC!!!

/**
 * 
 * @author Demitreus
 *
 */
public class TileEntityTeleRenderer extends TileEntitySpecialRenderer {

	private ModelTeleporter teleModel = new ModelTeleporter();

	public TileEntityTeleRenderer() {
	}

	
	@Override
	public void renderTileEntityAt(TileEntity entityTele, double var2,
			double var4, double var6, float var8) {
		String tName = ((TileEntityTeleporter) entityTele).getName();
		String tTarget = ((TileEntityTeleporter) entityTele).getTargetName();
		tTarget = tTarget.equals("") ? "" : "( " + tTarget + " )";

		GL11.glPushMatrix();
		GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 1.5f,
				(float) var6 + 0.5F);
		GL11.glScalef(1f, 1f, 1f);

		this.bindTextureByName("/adanaran/mods/ts/textures/Frame.png");
		teleModel.render();

		for (int i = 0; i < 4; i++) {
			drawText(i, tName, tTarget);
		}

		GL11.glPopMatrix();

	}

	private void drawText(int i, String tName, String tTarget) {
		GL11.glPushMatrix();
		float var10 = 0.6666667F;
		float var12 = 0.016666668F * var10;
		float[] xy = { 0, 0.5f, 0, -0.5f };
		FontRenderer fR = this.getFontRenderer();

		GL11.glTranslatef(xy[i], -0.6F, xy[i + (i != 3 ? 1 : -1)]);
		GL11.glScalef(var12, -var12, var12);
		GL11.glRotatef(i * 90, 0, 1, 0);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * var12);
		GL11.glDepthMask(false);

		fR.drawString(tName, -fR.getStringWidth(tName) / 2, 0, 0);
		fR.drawString(tTarget, -fR.getStringWidth(tTarget) / 2, 20, 0);

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}

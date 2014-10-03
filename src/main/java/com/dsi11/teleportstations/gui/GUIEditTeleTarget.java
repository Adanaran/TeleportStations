package com.dsi11.teleportstations.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
import com.dsi11.teleportstations.items.ItemTeleporter;

/**
 * GUI for changing targets.
 * 
 * @author Demitreus
 */
public class GUIEditTeleTarget extends GuiScreen {

	protected int width = 176, height = 166, listHeight = 0, scrollY = 0,
			scrollHeight = 0;
	private BlockPos[] zieldb;
	private StringBuilder[] zielNames;
	private int selected;
	private boolean isScrolling = false;
	private TeleData self;
	private int metacheck;
	private int zlSize;
	private TreeMap<BlockPos, TeleData> zielliste;
	private int x, y, z;
	private World world;

	/**
	 * Creates a new GUI.
	 * 
	 * @param world
	 *            World the world the player is in
	 * @param x
	 *            int x-coordinate
	 * @param y
	 *            int y-coordinate
	 * @param z
	 *            int z-coordinate
	 */
	public GUIEditTeleTarget(World world, int x, int y, int z) {
		this.world = world;
		isScrolling = false;
		this.x = x;
		this.y = y;
		this.z = z;
		selected = -1;
		// metacheck = y == -1 ? -1 : world.getBlockState(new BlockPos(x,y,z)).;
		TeleportStations.logger
				.log(Level.TRACE, "Teleportermeta: " + metacheck);
		zielliste = (TreeMap<BlockPos, TeleData>) TeleportStations.db
				.getDB();
		self = zielliste.remove(new BlockPos(x, y, z));
		if (self == null) {
			self = new TeleData("(mobile);;0;;0;;0;;0;;0;;null");
		}
		for (Iterator<Map.Entry<BlockPos, TeleData>> entry = zielliste
				.entrySet().iterator(); entry.hasNext();) {
			TeleData LName = entry.next().getValue();
			if (LName.getWorldType() != world.provider.getDimensionId()
					|| (metacheck <= 0 && LName.getMeta() > 0)
					|| (metacheck > 0 && LName.getMeta() == 0)) {
				entry.remove();
			}
		}
		zlSize = zielliste.size();
		if (zlSize > 0) {
			listHeight = 14 * ((zlSize + 1)) - 139;
			scrollHeight = (int) ((139D / (double) (listHeight + 139)) * 139D);
			if (scrollHeight <= 0 || scrollHeight >= 139) {
				scrollHeight = 139;
			}
			zieldb = new BlockPos[zlSize];
			zielNames = new StringBuilder[zlSize];
			int i = 0;
			for (Entry<BlockPos, TeleData> entry : zielliste.entrySet()) {
				TeleData LName = entry.getValue();
				TeleportStations.logger.log(Level.TRACE, LName.getMeta()
						+ " meta Lname");
				zieldb[i] = entry.getKey();
				zielNames[i] = new StringBuilder(LName.getName());
				TeleportStations.logger.log(Level.TRACE, i + " benannt: "
						+ zielNames[i].toString());
				if (LName.getTarget() != null) {
                    BlockPos tZiel = LName.getTarget();
					String ttName = TeleportStations.db.getTeleDataByCoords(
							tZiel.getX(), tZiel.getY(), tZiel.getZ()).getName();
					if (ttName != null) {
						zielNames[i].append(" (" + ttName + ")");
					}
				}
				i++;
			}
			try {
				sortArrays();
			} catch (NullPointerException npe) {
				// falls mal die Daten nicht so ganz stimmen
				// duerfte nicht vorkommen
				npe.printStackTrace();
			}
		}
	}

	@Override
	public void onGuiClosed() {
		if (!zielliste.isEmpty() && selected != -1 && zieldb[selected] != null) {
			if (metacheck == -1) {
				ItemTeleporter.setTarget(zieldb[selected]);
			} else {
				if (!TeleportStations.db.wouldTPRoundtripOccur(self,
						zieldb[selected])) {
					TeleportStations.db.changeTarget(new BlockPos(
							self.getX(), self.getY(), self.getZ()), zieldb[selected]);
					TeleportStations.logger.log(Level.TRACE,
							"Changed target at " + x + "|" + y + "|" + z
									+ " to " + zieldb[selected].getX() + "|"
									+ (zieldb[selected].getY()) + "|"
									+ zieldb[selected].getZ());
				}
			}
		} else {
			if (metacheck == -1) {
				ItemTeleporter.setTarget(null);
			} else {
                TeleportStations.db.changeTarget(self.getPosition(), null);
            }
        }
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int l = width - width >> 1;
		int i1 = height - height >> 1;
		i -= l;
		j -= i1;
		if (k == 0 && i >= 10 && i < 165 && j >= 20 && j < 159) {
			for (int j1 = 0; j1 < zielliste.size(); j1++) {
				if (!mouseInRadioButton(i, j, j1)) {
					continue;
				}
				selected = j1;
				break;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
        try {
            handleKeyboardInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int k = width - width >> 1;
		int l = height - height >> 1;
		int i1 = 0;
		mc.renderEngine.bindTexture(TeleportStations.guiTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int j1 = k;
		int l1 = l;
		drawTexturedModalRect(j1, l1, 0, 0, width, height);
		for (int j2 = 0; j2 < zlSize; j2++) {
			int k1 = k + 10;
			int i2 = (l + 14 * j2 + 20) - scrollY;
			if (i2 >= (height - height >> 1) + 20 && i2 + 9 < l + 159) {
				drawTexturedModalRect(k1, i2, 176 + (selected != j2 ? 0 : 8),
						0, 8, 9);
			}
		}
		if (scrollHeight != 139) {
			drawScrollBar();
		}
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef(k, l, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);
		c();
		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
		GL11.glPopMatrix();
		if (scrollHeight != 139) {
			i -= k;
			j -= l;
			if (Mouse.isButtonDown(0)) {
				if (i >= 165 && i < 170 && j >= 20 && j < 159) {
					isScrolling = true;
				}
			} else {
				isScrolling = false;
			}
			if (isScrolling) {
				scrollY = ((j - 20) * listHeight) / (139 - (scrollHeight >> 1));
				if (scrollY < 0) {
					scrollY = 0;
				}
				if (scrollY > listHeight) {
					scrollY = listHeight;
				}
			}
			int k2 = Mouse.getDWheel();
			if (k2 < 0) {
				scrollY += 14;
				if (scrollY > listHeight) {
					scrollY = listHeight;
				}
			} else if (k2 > 0) {
				scrollY -= 14;
				if (scrollY < 0) {
					scrollY = 0;
				}
			}
		}
	}

	private boolean mouseInRadioButton(int i, int j, int k) {
		int l = 10;
		int i1 = (14 * k + 20) - scrollY;
		return i >= l - 1 && i < l + 9 && j >= i1 - 1 && j < i1 + 10;
	}

	private void c() {
		fontRendererObj.drawString("Teleporter " + self.getName(), 8, 6,
				0x404040);
		for (int i = 0; i < zlSize; i++) {
			int j = 20;
			int k = (14 * i + 20) - scrollY;
			if (k >= 20 && k + 9 < 159) {
				fontRendererObj.drawString(zielNames[i].toString(), j, k,
						0xdddddd);
				if (TeleportStations.db.wouldTPRoundtripOccur(self, zieldb[i])) {
					fontRendererObj.drawString(zielNames[i].toString()
							+ " (Loop detected!)", j, k, 0xcc0000);
				}
			}
		}
	}

	private void drawScrollBar() {
		int i = (width - width >> 1) + 163;
		if (listHeight == 0) {
			listHeight = 1;
		}
		int j = (height - height >> 1) + 19 + (scrollY * (139 - scrollHeight))
				/ listHeight;
		int k = j;
		drawTexturedModalRect(i, k, 176, 9, 5, 1);
		for (k++; k < (j + scrollHeight) - 1; k++) {
			drawTexturedModalRect(i, k, 176, 10, 5, 1);
		}
		drawTexturedModalRect(i, k, 176, 11, 5, 1);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (mc.gameSettings.keyBindInventory.isPressed()
				|| i == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void sortArrays() {
		if (zielNames.length > 1) {
			for (int i1 = zielNames.length; i1 > 0; i1--) {
				for (int j = 0; j < i1 - 1; j++) {
					if ((zielNames[j].toString()
							.compareToIgnoreCase(zielNames[j + 1].toString())) > 0) {
						StringBuilder z1 = zielNames[j];
                        BlockPos db = zieldb[j];
						zielNames[j] = zielNames[j + 1];
						zieldb[j] = zieldb[j + 1];
						zielNames[j + 1] = z1;
						zieldb[j + 1] = db;
					}
				}
			}
		}
	}
}
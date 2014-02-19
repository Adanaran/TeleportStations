package com.dsi11.teleportstations.gui;

import java.util.LinkedList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.blocks.BlockTeleTarget;
import com.dsi11.teleportstations.entities.TileEntityTeleporter;

/**
 * GUI to name and rename a teleporter.
 * <p>
 * Parts of GUI are copied from {@link#GuiEditSign}. The GUI is called on
 * placing a teleporter/-target.
 * 
 * @author Demitreus
 */
public class GUIEditTeleName extends GuiScreen {
	private World world;
	private int updateCounter, i, j, k;
	private String type = "";
	// private TileEntityTeleporter tet;
	private String name = "";
	private static final char[] allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	private static LinkedList namenListe;

	/**
	 * Creates a new GUI.
	 * 
	 * @param world
	 *            World the world the player is in
	 * @param i
	 *            int x-coordinate
	 * @param j
	 *            int y-coordinate
	 * @param k
	 *            int z-coordinate
	 * @param type
	 *            String type
	 */
	public GUIEditTeleName(World world, int i, int j, int k, String type) {
		this.world = world;
		this.i = i;
		this.j = j;
		this.k = k;
		this.type = type;
		// tet = (TileEntityTeleporter) world.getBlockTileEntity(i, j + 2, k);
	}

	@Override
	public void initGui() {
		namenListe = TeleportStations.db.getAllNames();
		// TODO Controls
		// controlList.clear();
		// Keyboard.enableRepeatEvents(true);
		// controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120,
		// "Done"));
	}

	@Override
	public void updateScreen() {
		updateCounter++;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 0 && name.length() > 0
				&& !namenListe.contains(name)) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (i == 28 && name.length() > 0 && !namenListe.contains(name)) {
			mc.displayGuiScreen(null);
		}
		if (i == 14 && name.length() > 0) {
			name = name.substring(0, name.length() - 1);
		}

		// TODO bitte prüfen, allowedCharacters jetzt char[]
		for (int j = 0; j < allowedCharacters.length; j++) {
			if (allowedCharacters[j] == c && c != ';' && name.length() < 20) {
				name += c;
				break;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, type, width / 2, 40, 0xffffff);
		drawCenteredString(fontRendererObj, name, width / 2, 80, 0xffff00);
		super.drawScreen(i, j, f);
	}

	@Override
	public void onGuiClosed() {
		TeleportStations.db.addNewTP(name, i, j, k,
				world.getBlockMetadata(i, j, k), world.provider.dimensionId);
	}
}

package com.dsi11.teleportstations.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import com.dsi11.teleportstations.TeleportStations;
import com.dsi11.teleportstations.database.TeleData;
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
	private TileEntityTeleporter tet;
	private String name = "";
	private boolean nameInUse = false;
	
	private static final ArrayList<Character> allowedCharacters = new ArrayList(
			Arrays.asList(ChatAllowedCharacters.allowedCharactersArray));
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
		tet = (TileEntityTeleporter) world.getTileEntity(new BlockPos(i, j + 2, k));
	}

	@Override
	public void initGui() {
		namenListe = TeleportStations.db.getAllNames();
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120,
				"Done"));
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
				&& !nameInUse) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (Character.valueOf(';').equals(c)) {
			return;
		}
		if (i == 28 && name.length() > 0 && !nameInUse) {
			mc.displayGuiScreen(null);
		}
		if (i == 14 && name.length() > 0) {
			name = name.substring(0, name.length() - 1);
		}
		if (Character.isSpaceChar(c) || Character.isLetterOrDigit(c)
				|| allowedCharacters.contains(c)
				|| allowedCharacters.contains(c)) {
			name += c;
		}
		nameInUse = namenListe.contains(name);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, type, width / 2, 40, 0xffffff);
		drawCenteredString(fontRendererObj, name, width / 2, 80, 0xffff00);
		if(nameInUse){
			drawCenteredString(fontRendererObj, "Teleporter existiert bereits", width / 2, 120, 0xffff00);
		}
		super.drawScreen(i, j, f);
	}

	@Override
	public void onGuiClosed() {
/*		TeleportStations.db
				.addTeleDataToDatabaseWithNotificationAtClient(new TeleData(
						name, i, j, k, world.getBlockMetadata(i, j, k),
						world.provider.getDimensionId()));*/
		tet.setNameAndTarget(name, "");
	}
}

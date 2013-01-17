package adanaran.mods.ts.gui;

import java.util.LinkedList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import adanaran.mods.ts.TeleportStations;
import adanaran.mods.ts.entities.TileEntityTele;


/**
 * GUI to name and rename a teleporter.
 * <p>
 * Parts of GUI are copied from {@link#GuiEditSign}.
 * The GUI is called on placing a teleporter/-target or if 
 * a activated teleporter hasn't a name (error).
 * 
 * @author Demitreus
 */
public class GUIEditTeleName extends GuiScreen {
	private World world;
	private int updateCounter, i, j, k;
	private String type = "";
	private TileEntityTele tet;
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	private static LinkedList namenListe;

	
	
	public GUIEditTeleName(World world, int i, int j, int k, String type) {
		System.out.println("new gui");
		this.world = world;
		this.i = i;
		this.j = j;
		this.k = k;
		this.type = type;
		tet = (TileEntityTele) world.getBlockTileEntity(i, j + 2, k);
	}

	@Override
	public void initGui() {
		namenListe = TeleportStations.db.getAllNames();
		controlList.clear();
		Keyboard.enableRepeatEvents(true);
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120,
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
		if (guibutton.id == 0 && tet.nameAndTarget[0].length() > 0
				&& !namenListe.contains(tet.nameAndTarget[0])) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (i == 28 && tet.nameAndTarget[0].length() > 0 && !namenListe.contains(tet.nameAndTarget[0])) {
			mc.displayGuiScreen(null);
		}
		if (i == 14 && tet.nameAndTarget[0].length() > 0) {
			tet.nameAndTarget[0] = tet.nameAndTarget[0].substring(0, tet.nameAndTarget[0].length() - 1);
		}
		if (allowedCharacters.indexOf(c) >= 0 && c != ';' && tet.nameAndTarget[0].length() < 20) {
			tet.nameAndTarget[0] += c;
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, type, width / 2, 40, 0xffffff);
		drawCenteredString(fontRenderer, tet.nameAndTarget[0], width / 2, 80, 0xffff00);
		super.drawScreen(i, j, f);
	}

	@Override
	public void onGuiClosed() {
		TeleportStations.db.addTP(tet.nameAndTarget[0], i, j, k,
				world.getBlockMetadata(i, j, k), world.getWorldInfo().getDimension());
	}
}

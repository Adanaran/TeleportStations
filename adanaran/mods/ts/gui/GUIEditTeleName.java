package adanaran.mods.ts.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import adanaran.mods.ts.entities.TileEntityTeleporter;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GUI to name and rename a teleporter.
 * <p>
 * Parts of GUI are copied from {@link#GuiEditSign}. The GUI is called on
 * placing a teleporter/-target or if a activated teleporter hasn't a name
 * (error).
 * 
 * @author Demitreus
 */
public class GUIEditTeleName extends GuiScreen {
	private World world;
	private int updateCounter, i, j, k;
	private String type = "";
	private TileEntityTeleporter tet;
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	private static LinkedList namenListe;

	public GUIEditTeleName(World world, int i, int j, int k, String type) {
		System.out.println("new gui");
		this.world = world;
		this.i = i;
		this.j = j;
		this.k = k;
		this.type = type;
		tet = (TileEntityTeleporter) world.getBlockTileEntity(i, j + 2, k);
		namenListe = this.createListNames(world);
	}

	@Override
	public void initGui() {

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
		if (guibutton.id == 0 && tet.getName().length() > 0
				&& !namenListe.contains(tet.getName())) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (i == 28 && tet.getName().length() > 0
				&& !namenListe.contains(tet.getName())) {
			mc.displayGuiScreen(null);
		}
		if (i == 14 && tet.getName().length() > 0) {
			tet.setName(tet.getName().substring(0, tet.getName().length() - 1));
		}
		if (allowedCharacters.indexOf(c) >= 0 && c != ';'
				&& tet.getName().length() < 20) {
			tet.setName(tet.getName() + c);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, type, width / 2, 40, 0xffffff);
		drawCenteredString(fontRenderer, tet.getName(), width / 2, 80, 0xffff00);
		super.drawScreen(i, j, f);
	}

	@Override
	public void onGuiClosed() {
		tet.update(world.getWorldInfo().getDimension());

		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 DataOutputStream dos = new DataOutputStream(bos);
		 try {
		 dos.writeInt(i);
		 dos.writeInt(j);
		 dos.writeInt(k);
		 dos.writeInt(world.getWorldInfo().getDimension());
		 dos.writeChars(tet.getName());
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		 Packet250CustomPayload packet = new Packet250CustomPayload();
		 packet.channel = "tpname";
		 packet.data = bos.toByteArray();
		 packet.length = packet.data.length;
		 packet.isChunkDataPacket = true;
		 PacketDispatcher.sendPacketToServer(packet);

//		NetClientHandler var1 = this.mc.getSendQueue();
//		if (var1 != null) {
//			var1.addToSendQueue(tet.getDescriptionPacket());
//		}
	}

	private LinkedList createListNames(World world) {
		LinkedList v = new LinkedList();
		ArrayList list = (ArrayList) world.loadedTileEntityList;
		ListIterator iterator = list.listIterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj instanceof TileEntityTeleporter) {
				TileEntityTeleporter te = (TileEntityTeleporter) obj;
				if (!te.equals(tet)) {
					v.add(te.getName());
				}
			}
		}
		return v;
	}
}

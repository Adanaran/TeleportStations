package adanaran.mods.ts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 * Middle block of teleporter.
 * <p>
 * This block doesn't do anything, it's just a blocker to prevent other blocks
 * being placed in a teleporter.
 * 
 * @author Demitreus
 * 
 */
public class BlockTeleMid extends Block {

	/**
	 * Constuctor of middle block of teleporter.
	 * 
	 * @param par1 int ID
	 */
	public BlockTeleMid(int par1) {
		super(par1, Material.portal);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		//block cannot exist without teleporter beneath
		return ((world.getBlockId(i, j - 1, k) == 3001 || world.getBlockId(i,
				j - 1, k) == 3002));
	}

}

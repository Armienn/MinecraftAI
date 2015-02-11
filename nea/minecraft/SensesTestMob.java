package nea.minecraft;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SensesTestMob {
	World worldObj;
	public long lastupdate = 0;
	public double posX;
	public double posY;
	public double posZ;
	
	public SensesTestMob(World worldIn){
		worldObj = worldIn;
	}
	
	public Block GetBlockAtRelativePosition(double x, double y, double z){
		return GetBlockAt(posX+x, posY+y, posZ+z);
	}
	
	private Block GetBlockAt(double x, double y, double z){
		return worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
}

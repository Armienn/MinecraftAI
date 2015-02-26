package nea.minecraft.utility;

import net.minecraft.entity.item.EntityItem;

public class ItemInfo {
	public String itemtype;
	public double posX;
	public double posY;
	public double posZ;
	
	public ItemInfo(EntityItem item){
		posX = item.posX;
		posY = item.posY;
		posZ = item.posZ;
		itemtype = item.getName();
	}
}

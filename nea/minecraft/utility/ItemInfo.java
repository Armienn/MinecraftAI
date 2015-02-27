package nea.minecraft.utility;

import net.minecraft.entity.item.EntityItem;

public class ItemInfo {
	public int id;
	public String itemType;
	public double posX;
	public double posY;
	public double posZ;
	
	public ItemInfo(EntityItem item){
		id = item.getEntityId();
		posX = item.posX;
		posY = item.posY;
		posZ = item.posZ;
		itemType = item.getName();
	}
}

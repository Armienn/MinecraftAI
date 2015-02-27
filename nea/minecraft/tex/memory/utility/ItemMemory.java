package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

import nea.minecraft.utility.ItemInfo;

public class ItemMemory {
	public int id;
	public String itemType;
	public long appearTime;
	public double appearPosX;
	public double appearPosY;
	public double appearPosZ;
	ArrayList<Movement> movements = new ArrayList<Movement>();
	public long disappearTime = 0;
	
	public long lastUpdate;
	
	public ItemMemory(ItemInfo item, long time){
		id = item.id;
		itemType = item.itemType;
		appearTime = time;
		appearPosX = item.posX;
		appearPosY = item.posY;
		appearPosZ = item.posZ;
		lastUpdate = time;
	}
	
	public void Update(ItemInfo item, long time){
		// TODO
	}
}

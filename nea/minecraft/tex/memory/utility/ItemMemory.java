package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

import nea.minecraft.utility.ItemInfo;

public class ItemMemory {
	public int id;
	public String itemtype;
	public long appeartime;
	public long appearposX;
	public long appearposY;
	public long appearposZ;
	ArrayList<Movement> movements = new ArrayList<Movement>();
	
	public boolean awaitingUpdate = false;
	
	public ItemMemory(ItemInfo item){
		// TODO
	}
}

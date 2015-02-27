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
		double deltaX = item.posX - CurrentPosX();
		double deltaY = item.posY - CurrentPosY();
		double deltaZ = item.posZ - CurrentPosZ();
		
		double delta = Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaZ);
		if(delta > 0.0001){
			if(movements.size() > 0){
				Movement prevmove = movements.get(movements.size()-1);
				if( prevmove.endTime < lastUpdate ){ // if last movement ended before last update
					movements.add(new Movement(lastUpdate,CurrentPosX(),CurrentPosY(),CurrentPosZ(),time,item.posX,item.posY,item.posZ));
				}
				else { // if last movement was still ongoing last update
					prevmove.endTime = time;
					prevmove.endPosX = item.posX;
					prevmove.endPosY = item.posY;
					prevmove.endPosZ = item.posZ;
				}
			}
			else {
				movements.add(new Movement(lastUpdate,CurrentPosX(),CurrentPosY(),CurrentPosZ(),time,item.posX,item.posY,item.posZ));
			}
		}
		lastUpdate = time;
	}
	
	public double CurrentPosX(){
		if(movements.size() > 0){
			return movements.get(movements.size()-1).endPosX;
		}
		else{
			return appearPosX;
		}
	}
	
	public double CurrentPosY(){
		if(movements.size() > 0){
			return movements.get(movements.size()-1).endPosY;
		}
		else{
			return appearPosY;
		}
	}
	
	public double CurrentPosZ(){
		if(movements.size() > 0){
			return movements.get(movements.size()-1).endPosZ;
		}
		else{
			return appearPosZ;
		}
	}
}

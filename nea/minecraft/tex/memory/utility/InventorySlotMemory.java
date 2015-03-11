package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

public class InventorySlotMemory {
	ArrayList<EntityMemory> slot = new ArrayList<EntityMemory>();
	
	public InventorySlotMemory(){
		
	}
	
	public InventorySlotMemory(EntityMemory entity){
		if(entity != null){
			slot.add(entity);
		}
	}
	
	public EntityMemory GetCurrentItem(){
		for(EntityMemory entity : slot){
			if(entity.disappearTime == 0)
				return entity;
		}
		return null;
	}
	
	public void Add(EntityMemory entity){
		EntityMemory current = GetCurrentItem();
		if(current == null){
			slot.add(entity);
		}
		else{
			throw new RuntimeException("Inventory slot should be empty before trying to put something in it");
		}
	}
	
	public void Update(EntityMemory entity, long time){
		EntityMemory current = GetCurrentItem();
		if(entity == null){
			if(current != null){
				current.disappearTime = time;
			}
		}
		else{
			if(current != null){
				current.Update(entity, time);
			}
			else{
				slot.add(entity);
			}
		}
	}
}

package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.memory.utility.ItemMemory;
import nea.minecraft.utility.ItemInfo;

public class ShortTermMemory {
	TexBrain brain;
	ArrayList<ItemMemory> itemmemories = new ArrayList<ItemMemory>();
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	public boolean CurrentlyExists(ItemInfo item){
		// TODO
		return false;
	}
	
	public void Add(ItemInfo item){
		// TODO
	}
	
	public void Update(ItemInfo item){
		if(CurrentlyExists(item)){
			//Update
		}
		else{
			itemmemories.add(new ItemMemory(item));
		}
	}

	public void MarkItemsForUpdate() {
		for(ItemMemory item : itemmemories){
			//if not away already
				item.awaitingUpdate = true;
		}
	}

	public void RemoveUnupdatedItems() {
		// TODO
	}
}

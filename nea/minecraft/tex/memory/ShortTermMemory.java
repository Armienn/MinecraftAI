package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.memory.utility.ItemMemory;
import nea.minecraft.utility.ItemInfo;

public class ShortTermMemory {
	TexBrain brain;
	long lastUpdate;
	/// Actual memory : ///
	// memory of nearby items:
	ArrayList<ItemMemory> itemmemories = new ArrayList<ItemMemory>();
	// memory of own position:
	// memory of own actions:
	// memory of inventory:
	/// Memory end ///
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	public ItemMemory GetMemoryOfItem(ItemInfo item){
		for(ItemMemory t : itemmemories){
			if(t.id == item.id && t.disappearTime == 0)
				return t;
		}
		return null;
	}
	
	public void Update(ItemInfo item){
		ItemMemory t = GetMemoryOfItem(item);
		if(t != null){
			t.Update(item, lastUpdate);
		}
		else{
			itemmemories.add(new ItemMemory(item, lastUpdate));
		}
	}

	public void StartUpdate(long time) {
		lastUpdate = time;
	}

	public void RemoveUnupdatedItems() {
		for(ItemMemory item : itemmemories){
			if( item.disappearTime == 0  //if not away already
					&& item.lastUpdate < lastUpdate){ //if not updated in the current iteration
				item.disappearTime = item.lastUpdate;
			}
		}
	}
}

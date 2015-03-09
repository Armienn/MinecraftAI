package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.utility.EntityMemory;

public class ShortTermMemory {
	TexBrain brain;
	long lastUpdate;
	/// Actual memory : ///
	ArrayList<EntityMemory> entitymemories = new ArrayList<EntityMemory>();
	// memory of nearby items:
	//ArrayList<ItemMemory> itemmemories = new ArrayList<ItemMemory>();
	// memory of own position:
	// memory of own actions:
	// memory of inventory:
	// memory of other entities (including positions and actions):
	// memory of surrounding blocks:
	// memory of time of day:
	/// Memory end ///
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	private EntityMemory GetMemoryOf(int id){
		for(EntityMemory t : entitymemories){
			if(t.id == id && t.disappearTime == 0)
				return t;
		}
		return null;
	}
	
	public void Update(EntityMemory memory){
		EntityMemory m = GetMemoryOf(memory.id);
		if(m != null){
			m.Update(memory, lastUpdate);
		}
		else{
			entitymemories.add(memory);
		}
	}

	public void StartUpdate(long time) {
		lastUpdate = time;
	}

	public void UpdateUnupdatedMemories() {
		for(EntityMemory memory : entitymemories){
			if( memory.disappearTime == 0  //if not away already
					&& memory.lastUpdate < lastUpdate){ //if not updated in the current iteration
				memory.disappearTime = memory.lastUpdate;
			}
		}
	}

	public void Update(Action action, long time) {
		// TODO Auto-generated method stub
		
	}
}

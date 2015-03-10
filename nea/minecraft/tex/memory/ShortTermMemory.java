package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.utility.EntityAction;
import nea.minecraft.tex.memory.utility.EntityMemory;

public class ShortTermMemory {
	TexBrain brain;
	long lastUpdate;
	/// Actual memory : ///
	// memory of own position:
	// memory of own actions:
	// memory of inventory:
	EntityMemory selfMemory;
	// memory of nearby items:
	// memory of other entities (including positions and actions):
	ArrayList<EntityMemory> entityMemories = new ArrayList<EntityMemory>();
	// memory of surrounding blocks:
	// memory of time of day:
	/// Memory end ///
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	private EntityMemory GetMemoryOf(int id){
		for(EntityMemory t : entityMemories){
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
			entityMemories.add(memory);
		}
	}

	public void StartUpdate(long time) {
		lastUpdate = time;
	}

	public void UpdateUnupdatedMemories() {
		for(EntityMemory memory : entityMemories){
			if( memory.disappearTime == 0  //if not away already
					&& memory.lastUpdate < lastUpdate){ //if not updated in the current iteration
				memory.disappearTime = memory.lastUpdate;
			}
		}
	}
	
	public void UpdateSelf(EntityMemory memory){
		if(selfMemory != null){
			selfMemory.Update(memory, lastUpdate);
		}
		else{
			selfMemory = memory;
		}
	}

	public void Update(Action action, long time) {
		selfMemory.AddAction(new EntityAction(action, time));
	}
}

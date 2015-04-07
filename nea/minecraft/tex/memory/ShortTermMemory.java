package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.utility.MemAction;
import nea.minecraft.tex.memory.utility.MemEntity;

public class ShortTermMemory {
	TexBrain brain;
	long lastUpdate;
	/// Actual memory : ///
	// memory of own position:
	// memory of own actions:
	// memory of inventory:
	MemEntity selfMemory;
	// memory of nearby items:
	// memory of other entities (including positions and actions):
	ArrayList<MemEntity> entityMemories = new ArrayList<MemEntity>();
	// memory of surrounding blocks:
	// memory of time of day:
	/// Memory end ///
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	private MemEntity GetMemoryOf(int id){
		for(MemEntity t : entityMemories){
			if(t.id == id && t.disappearTime == 0)
				return t;
		}
		return null;
	}
	
	public void Update(MemEntity memory){
		MemEntity m = GetMemoryOf(memory.id);
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
		for(MemEntity memory : entityMemories){
			if( memory.disappearTime == 0  //if not away already
					&& memory.lastUpdate < lastUpdate){ //if not updated in the current iteration
				memory.disappearTime = memory.lastUpdate;
			}
		}
	}
	
	public void UpdateSelf(MemEntity memory){
		if(selfMemory != null){
			selfMemory.Update(memory, lastUpdate);
		}
		else{
			selfMemory = memory;
		}
	}

	public void Update(Action action, long time) {
		selfMemory.AddAction(new MemAction(action, time));
	}
}

package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.utility.MemAction;
import nea.minecraft.tex.memory.utility.MemEntity;

public class ShortTermMemory {
	TexBrain brain;
	long previousTime;
	long currentTime;
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
			if(t.id == id && t.apperanceInterval.endTime >= previousTime)
				return t;
		}
		return null;
	}
	
	public void Update(MemEntity memory){
		MemEntity m = GetMemoryOf(memory.id);
		if(m != null){
			m.Update(memory, currentTime);
		}
		else{
			entityMemories.add(memory);
		}
	}

	public void StartUpdate(long time) {
		previousTime = currentTime;
		currentTime = time;
	}

	/*public void UpdateUnupdatedMemories() {
		for(MemEntity memory : entityMemories){
			if( memory.apperanceInterval.endTime < previousTime  //if not away already
					&& memory.previousTime < currentTime){ //if not updated in the current iteration
				memory.disappearTime = memory.previousTime;
			}
		}
	}*/
	
	public void UpdateSelf(MemEntity memory){
		if(selfMemory != null){
			selfMemory.Update(memory, currentTime);
		}
		else{
			selfMemory = memory;
		}
	}

	public void Update(Action action, boolean success, long time) {
		selfMemory.AddAction(new MemAction(action, time, success));
	}
}

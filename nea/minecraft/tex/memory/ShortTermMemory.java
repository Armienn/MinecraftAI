package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.utility.MemAction;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemReward;

public class ShortTermMemory {
	TexBrain brain;
	public long previousTime;
	public long currentTime;
	/// Actual memory : ///
	public MemEntity selfMemory;
	public ArrayList<MemEntity> entityMemories = new ArrayList<MemEntity>();
	public ArrayList<MemReward> rewardMemories = new ArrayList<MemReward>();
	// memory of surrounding blocks:
	// memory of time of day:
	/// Memory end ///
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	private MemEntity GetMemoryOf(int id){
		for(MemEntity t : entityMemories){
			if(t.id == id && t.appearanceInterval.endTime >= previousTime)
				return t;
		}
		return null;
	}
	
	public void AddReward(MemReward reward){
		rewardMemories.add(reward);
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

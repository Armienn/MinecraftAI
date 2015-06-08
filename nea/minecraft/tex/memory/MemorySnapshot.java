package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.learning.SnapEntity;
import nea.minecraft.tex.memory.utility.MemEntity;

public class MemorySnapshot {
	public long time; 
	//public ArrayList<MemReward> rewards = new ArrayList<MemReward>();
	public ArrayList<SnapEntity> entities = new ArrayList<SnapEntity>();
	public SnapEntity self;
	
	public MemorySnapshot(ShortTermMemory shortmemory, long time){
		this.time = time;
		for(MemEntity entity : shortmemory.entityMemories){
			if(entity.appearanceInterval.Contains(time))
				entities.add(new SnapEntity(entity, time));
		}
		self = new SnapEntity(shortmemory.selfMemory, time);
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		if(level > 2) return "MemorySnapshot";
		
		String result = "MemorySnapshot - " + time + "\n";
		result += tab + "\tTex: " + self.toString(level+1) + "\n";
		for(int i=0; i<entities.size(); i++){
			result += tab + "\tE" + i + ": " + entities.get(i).toString(level+1) + "\n";
		}
		return result;
	}
	
	public ArrayList<SnapEntity> GetAllEntities(){
		ArrayList<SnapEntity> result = new ArrayList<SnapEntity>(entities);
		result.add(self);
		return result;
	}
}

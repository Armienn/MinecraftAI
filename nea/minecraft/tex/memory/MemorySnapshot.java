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
			entities.add(new SnapEntity(entity, time));
		}
		self = new SnapEntity(shortmemory.selfMemory, time);
	}
}

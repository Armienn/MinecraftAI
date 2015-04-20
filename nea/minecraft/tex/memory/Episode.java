package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemReward;

public class Episode {
	public Interval interval;
	
	public MemEntity selfMemory;
	public ArrayList<MemEntity> entityMemories = new ArrayList<MemEntity>();
	public ArrayList<MemReward> rewardMemories = new ArrayList<MemReward>();
	
	public Episode(ShortTermMemory shortmemory, Interval interval){
		this.interval = interval;
		for(MemEntity entity : shortmemory.entityMemories){
			if(entity.appearanceInterval.Intersects(interval)){
				MemEntity newEntity = new MemEntity(entity, interval);
			}
		}
		this.interval = interval;
		// TODO: create episode from short term memory
	}
}

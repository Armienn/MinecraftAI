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
				entityMemories.add(new MemEntity(entity, interval));
			}
		}
		selfMemory = new MemEntity(shortmemory.selfMemory, interval);
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
		if(level > 2) return "Episode";
		
		String result = "Episode - " + interval + "\n";
		result += tab + "\tTex: " + selfMemory.toString(level+1) + "\n";
		for(int i=0; i<entityMemories.size(); i++){
			result += tab + "\tE" + i + ": " + entityMemories.get(i).toString(level+1) + "\n";
		}
		for(int i=0; i<rewardMemories.size(); i++){
			result += tab + "\tR" + i + ": " + rewardMemories.get(i).toString(level+1) + "\n";
		}
		return result;
	}
}

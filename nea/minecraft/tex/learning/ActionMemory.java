package nea.minecraft.tex.learning;

import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.Episode;
import nea.minecraft.tex.memory.MemorySnapshot;
import nea.minecraft.tex.memory.ShortTermMemory;
import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemAction;

public class ActionMemory {
	//ActionType
	MemAction action;
	
	//MemorySnapshot
	MemorySnapshot snapshot;
	
	//TrailingEpisode
	Episode trailingEpisode;
	
	public ActionMemory(MemAction action, ShortTermMemory shortmemory, long episodelength){
		this.action = action.Copy();
		trailingEpisode = new Episode(shortmemory, new Interval(action.interval.startTime-1, action.interval.startTime + episodelength));
		snapshot = new MemorySnapshot(shortmemory, action.interval.startTime);
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
		if(level > 2) return "ActionMemory";
		
		String result = "ActionMemory\n";
		result += tab + "\t" + action.toString(level+1) + "\n";
		result += tab + "\t" + snapshot.toString(level+1) + "\n";
		result += tab + "\t" + trailingEpisode.toString(level+1);
		return result;
	}
}

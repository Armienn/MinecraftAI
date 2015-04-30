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
		trailingEpisode = new Episode(shortmemory, new Interval(action.interval.startTime-2, action.interval.startTime + episodelength));
		snapshot = new MemorySnapshot(shortmemory, action.interval.startTime);
	}
}

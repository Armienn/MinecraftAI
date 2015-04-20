package nea.minecraft.tex.learning;

import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.Episode;
import nea.minecraft.tex.memory.utility.Interval;

public class ActionMemory {
	//ActionType
	public Action.Type type;
	public float[] parameters;
	public Interval interval;
	public boolean success;
	
	//MemorySnapshot
	MemorySnapshot snapshot;
	
	//TrailingEpisode
	Episode trailingEpisode;
}

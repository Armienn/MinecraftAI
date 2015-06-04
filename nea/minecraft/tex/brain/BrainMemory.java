package nea.minecraft.tex.brain;

import nea.minecraft.tex.memory.InstantMemory;
import nea.minecraft.tex.memory.ShortTermMemory;

public class BrainMemory {
	TexBrain brain;
	public InstantMemory instant;
	public ShortTermMemory shortterm;
	
	public BrainMemory(TexBrain brain){
		this.brain = brain;
		
		instant = new InstantMemory(brain);
		shortterm = new ShortTermMemory(brain);
	}
}

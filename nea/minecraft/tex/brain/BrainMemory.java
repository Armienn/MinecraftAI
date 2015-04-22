package nea.minecraft.tex.brain;

import nea.minecraft.tex.memory.SensoryMemory;
import nea.minecraft.tex.memory.ShortTermMemory;

public class BrainMemory {
	TexBrain brain;
	public SensoryMemory sensory;
	public ShortTermMemory shortterm;
	
	public BrainMemory(TexBrain brain){
		this.brain = brain;
		
		sensory = new SensoryMemory(brain);
		shortterm = new ShortTermMemory(brain);
	}
}

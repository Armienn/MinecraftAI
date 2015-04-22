package nea.minecraft.tex.brain;

import nea.minecraft.tex.memory.SensoryMemory;
import nea.minecraft.tex.memory.ShortTermMemory;

public class BrainMemory {
	TexBrain brain;
	public SensoryMemory sensory = new SensoryMemory(brain);
	public ShortTermMemory shortterm = new ShortTermMemory(brain);
	
	public BrainMemory(TexBrain brain){
		this.brain = brain;
	}
}

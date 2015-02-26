package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Senses;

public class SensoryMemory {
	TexBrain brain;
	public ArrayList<Senses> memory = new ArrayList<Senses>();
	
	public SensoryMemory(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(){
		memory.add(brain.senses.Copy());
		while(memory.size() > 50){
			memory.remove(0);
		}
	}

}

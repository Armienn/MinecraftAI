package nea.minecraft.tex.memory;

import java.util.ArrayList;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;

public class SensoryMemory {
	TexBrain brain;
	public ArrayList<Senses> memorysenses = new ArrayList<Senses>();
	public ArrayList<Actions> memoryactions = new ArrayList<Actions>(); 
	
	public SensoryMemory(TexBrain brain){
		this.brain = brain;
	}
	
	public void UpdateExternal(){
		memorysenses.add(brain.senses.Copy());
		while(memorysenses.size() > 50){
			memorysenses.remove(0);
		}
	}
	
	public void UpdateInternal(){
		memoryactions.add(brain.actions.Copy());
		while(memoryactions.size() > 50){
			memoryactions.remove(0);
		}
	}

}

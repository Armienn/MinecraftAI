package nea.minecraft.tex.brain;

import java.util.ArrayList;

import nea.minecraft.tex.learning.ActionKnowledge;

public class BrainKnowledge {
	TexBrain brain;

	public BrainKnowledge(TexBrain brain){
		this.brain = brain;
		
		ArrayList<ActionKnowledge> actionknowledges = new ArrayList<ActionKnowledge>();
	}
}

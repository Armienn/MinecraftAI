package nea.minecraft.tex.brain;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.learning.ActionKnowledge;

public class BrainKnowledge {
	TexBrain brain;
	
	ArrayList<ActionKnowledge> actionknowledges = new ArrayList<ActionKnowledge>();

	public BrainKnowledge(TexBrain brain){
		this.brain = brain;
	}
	
	public ActionKnowledge GetKnowledge(Action.Type type){
		for(ActionKnowledge a : actionknowledges){
			if(a.GetType() == type)
				return a;
		}
		ActionKnowledge newknowledge = new ActionKnowledge(type);
		actionknowledges.add(newknowledge);
		return newknowledge;
	}
}

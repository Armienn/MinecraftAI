package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Action;

public class ActionKnowledge {
	final Action.Type type;
	ArrayList<ConditionSet> conditionSets = new ArrayList<ConditionSet>();
	//ArrayList<ActionMemory> memories = new ArrayList<ActionMemory>();
	
	public ActionKnowledge(Action.Type type){
		this.type = type;
	}
	
	public void Process(ActionMemory memory){
		if(memory.action.type != type) throw new RuntimeException("Mismatch in Action Types");
		if(conditionSets.size() == 0){
			conditionSets.add(new ConditionSet(memory));
		}
		else {
			
		}
		//TODO
	}
	
	public Action.Type GetType(){
		return type;
	}
}

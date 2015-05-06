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
			boolean foundfit = false;
			for(ConditionSet set : conditionSets){
				if(set.AddIfFits(memory)) break; // checks if memory fits the set, and adds it to the set if so. Then breaks out of the loop
			}
			if(!foundfit && conditionSets.size() < 20){
				conditionSets.add(new ConditionSet(memory));
			}
		}
	}
	
	public Action.Type GetType(){
		return type;
	}
}

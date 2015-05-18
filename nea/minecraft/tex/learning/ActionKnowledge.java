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
				if(set.UpdateIfFits(memory)) break; // checks if memory fits the set, and adds it to the set if so. Then breaks out of the loop
			}
			if(!foundfit && conditionSets.size() < 20){
				conditionSets.add(new ConditionSet(memory));
			}
		}
	}
	
	public Action.Type GetType(){
		return type;
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		if(level > 2) return "ActionKnowledge";
		
		String result = "ActionKnowledge - " + type + "\n";
		for(int i=0; i<conditionSets.size(); i++){
			result += tab + "\t" + i + ": " + conditionSets.get(0).toString(level+1) + "\n";
		}
		return result;
	}
}

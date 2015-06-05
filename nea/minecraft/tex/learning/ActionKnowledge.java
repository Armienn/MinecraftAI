package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Action;

public class ActionKnowledge {
	final Action.Type type;
	ArrayList<ConditionSet> conditionSets = new ArrayList<ConditionSet>();
	ArrayList<ActionMemory> memories = new ArrayList<ActionMemory>();
	
	public ActionKnowledge(Action.Type type){
		this.type = type;
	}
	
	public void Process(ActionMemory memory){
		if(memory.action.type != type) throw new RuntimeException("Mismatch in Action Types");
		if(memories.size() < 20){
			memories.add(memory);
		}
		else{
			if(conditionSets.size() == 0){
				//First look at successes vs fails
				ArrayList<ActionMemory> successes = new ArrayList<ActionMemory>();
				ArrayList<ActionMemory> fails = new ArrayList<ActionMemory>();
				for(ActionMemory am : memories){
					if(am.action.success)
						successes.add(am);
					else
						fails.add(am);
				}
				//compare snapshots
				//create list of all conditions in each
				ArrayList<Condition> successconditions = new ArrayList<Condition>();
				ArrayList<Condition> failconditions = new ArrayList<Condition>();
				for(ActionMemory am : memories){
					if(am.action.success)
						AddConditions(am, successconditions);
					else
						AddConditions(am, failconditions);
				}
			}
		}
		/*if(conditionSets.size() == 0){
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
		}*/
	}
	
	void AddConditions(ActionMemory memory, ArrayList<Condition> conditions){
		for(SnapEntity snap : memory.snapshot.entities){
			ArrayList<ArrayList<String>> permutations = Permutations(snap.properties);
			permutations = null;
			//for each property
			
			//for each property
			//  for each later property
			
			//for each property
			//  for each later property
			//    for each later later property
			
			
		}
	}
	
	static <T> ArrayList<ArrayList<T>> Permutations(ArrayList<T> source){
		ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
		for(int i=0; i<source.size(); i++){
			ArrayList<T> current = new ArrayList<T>();
			current.add(source.get(i));
			result.add(current);
			Permutations(result, source, current, i);
		}
		return result;
	}
	
	static <T> void Permutations(ArrayList<ArrayList<T>> destination, ArrayList<T> source, ArrayList<T> previous, int index){
		for(int i=index+1; i<source.size(); i++){
			ArrayList<T> current = new ArrayList<T>(previous);
			current.add(source.get(i));
			destination.add(current);
			Permutations(destination, source, current, i);
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
			result += tab + "\t" + i + ": " + conditionSets.get(i).toString(level+1) + "\n";
		}
		return result;
	}
}

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
				//Part into successes vs fails
				ArrayList<ActionMemory> successes = new ArrayList<ActionMemory>();
				ArrayList<ActionMemory> fails = new ArrayList<ActionMemory>();
				for(ActionMemory am : memories){
					if(am.action.success)
						successes.add(am);
					else
						fails.add(am);
				}
				FindConditions(successes);
				FindConditions(fails);
			}
		}
	}
	
	void FindConditions(ArrayList<ActionMemory> memories){
		ArrayList<ArrayList<Condition>> conditions = new ArrayList<ArrayList<Condition>>();
		for(ActionMemory memory : memories){
			conditions.add(GetConditions(memory));
		}
		//first find all common conditions in each list
		ArrayList<Condition> common = new ArrayList<Condition>();
		for(Condition condition : conditions.get(0)){
			boolean bool = true;
			for(int i=1; i<conditions.size(); i++){
				bool = false;
				for(Condition cond : conditions.get(i))
					if(cond.equals(condition)){
						bool = true;
						break;
					}
				if(!bool)
					break;
			}
			if(bool)
				common.add(condition);
		}
		//if no common exists, do?
		if(common.size() == 0){
			;
		}
		//else single out the entities in snapshots that fit the common conditions and start looking at their parameters.
		else{
			
		}
		
	}
	
	/**
	 * Creates a list of all property conditions this memory meets.
	 * @param memory
	 * @return
	 */
	ArrayList<Condition> GetConditions(ActionMemory memory){
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		for(SnapEntity snap : memory.snapshot.entities){
			ArrayList<ArrayList<String>> permutations = Permutations(snap.properties);
			for(ArrayList<String> props : permutations){
				Condition condition = null;
				for(Condition cond : conditions){
					if(cond.Fits(props)){
						condition = cond;
						break;
					}
				}
				if(condition != null){
					condition.observations++;//add observation to condition
				}
				else{
					conditions.add(new Condition(props));//create new condition
				}
			}
		}
		return conditions;
	}
	
	/**
	 * Finds all ways one or more of the items in the source list can be chosen.
	 * An item can only be chosen once per group, and the sequence does not
	 * matter.
	 * @param source
	 * @return
	 */
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

package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.Episode;

public class ConditionSet {
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	ArrayList<Effect> effects = new ArrayList<Effect>();
	int observations;
	
	public ConditionSet(ActionMemory memory){
		observations = 1;
		//conditions
		for(SnapEntity entity : memory.snapshot.entities){
			conditions.add(new Condition(entity, memory));
		}
		conditions.add(new Condition(memory.snapshot.self, memory));
		//effects
		if(memory.action.success){
			effects = Effect.GetEffects(memory);
		}
		else {
			effects = null;
		}
	}
	
	public boolean AddIfFits(ActionMemory memory){
		ArrayList<Condition> conditionfits;
		ArrayList<Effect> effectfits = CheckEffectFits(memory);
		if(effectfits.size() > 0){
			conditionfits = CheckConditionFits(memory);
			if(conditionfits.size() > 0){
				Add(memory, conditionfits, effectfits);
			}
		}
		return false;
	}

	private ArrayList<Condition> CheckConditionFits(ActionMemory memory){
		ArrayList<Condition> fits = new ArrayList<Condition>();
		//TODO
		return fits;
	}
	
	private ArrayList<Effect> CheckEffectFits(ActionMemory memory){
		ArrayList<Effect> fits = new ArrayList<Effect>();
		//TODO
		return fits;
	}
	
	private void Add(ActionMemory memory, ArrayList<Condition> conditionfits, ArrayList<Effect> effectfits) {
		// TODO Auto-generated method stub
		
	}
}

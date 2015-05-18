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
	
	/**
	 * Checks if the given ActionMemory fits this ConditionSet, and updates the
	 * set with it if so.
	 * @param memory
	 * @return
	 */
	public boolean UpdateIfFits(ActionMemory memory){
		ArrayList<Condition> conditionfits;
		ArrayList<Effect> effectfits = CheckEffectFits(memory);
		if(effectfits.size() > 0){
			conditionfits = CheckConditionFits(memory);
			if(conditionfits.size() > 0){
				Update(memory, conditionfits, effectfits);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of all conditions which fit with the snapshot in the
	 * given ActionMemory.
	 * @param memory
	 * @return
	 */
	private ArrayList<Condition> CheckConditionFits(ActionMemory memory){
		ArrayList<Condition> fits = new ArrayList<Condition>();
		for(Condition condition : conditions){
			if(condition.Fits(memory))
				fits.add(condition);
		}
		//TODO: Additional checks?
		return fits;
	}
	
	/**
	 * Returns a list of all effects which fit with the episode in the given
	 * ActionMemory.
	 * @param memory
	 * @return
	 */
	private ArrayList<Effect> CheckEffectFits(ActionMemory memory){
		ArrayList<Effect> fits = new ArrayList<Effect>();
		for(Effect effect : effects){
			if(effect.Fits(memory))
				fits.add(effect);
		}
		//TODO: Additional checks?
		return fits;
	}
	
	private void Update(ActionMemory memory, ArrayList<Condition> conditionfits, ArrayList<Effect> effectfits) {
		// TODO Auto-generated method stub
		
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
		if(level > 2) return "ConditionSet";
		
		String result = "ConditionSet - \n";
		for(int i=0; i<conditions.size(); i++){
			result += tab + "\t" + i + ": " + conditions.get(0).toString(level+1) + "\n";
		}
		for(int i=0; i<effects.size(); i++){
			result += tab + "\t" + i + ": " + effects.get(0).toString(level+1) + "\n";
		}
		result += tab + "\tObservations " + observations;
		return result;
	}
}

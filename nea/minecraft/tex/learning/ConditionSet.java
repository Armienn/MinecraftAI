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
			// TODO: Analyse interval?
			Episode trailing = memory.trailingEpisode;
			effects = Effect.GetEffects(memory);
		}
		else {
			effects = null;
		}
	}
	
	//  ConditionSet
	//    Conditions
	//    Effect
	
	//  Condition
	//    List of:
	//      Must (not) have (with certainty u) Entity with property x
	//        which has parameter y
	//          with value p
	//          with velocity q
	//        which has complex parameter z
	//          with value m
	//          with velocity n
	
	//  Effect
	//    List of:
	//      Entity with so and so properties and (complex) parameters
	//        will (dis)appear
	//        will get event (with velocity or setting value) in (complex) parameter x
	//    Or:
	//      Fails
}

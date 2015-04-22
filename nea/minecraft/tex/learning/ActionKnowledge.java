package nea.minecraft.tex.learning;

import nea.minecraft.tex.interaction.Action;

public class ActionKnowledge {
	Action.Type type;
	//List of ConditionSets
	
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
	//      Entity with so and so properties and parameters
	//        will (dis)appear
	//        will get event (with velocity or setting value) in (complex) parameter x
	//    Or:
	//      Fails
	
	public ActionKnowledge(Action.Type type){
		this.type = type;
	}
	
	public void Process(ActionMemory memory){
		//TODO
	}
	
	public Action.Type GetType(){
		return type;
	}
}

package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.MemorySnapshot;

public class Condition {
	//boolean mustHave;
	int observations;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<ParameterCondition> parameterConditions = new ArrayList<ParameterCondition>();
	
	public Condition(SnapEntity entity, ActionMemory memory){
		observations = 1;
		properties.addAll(entity.properties);
		for(ComplexParameter comparam : ComplexParameter.GetComplexParameters(entity, memory)){
			parameterConditions.add(new ParameterCondition(comparam, entity, memory));
		}
	}
	

	
	/*public static void AddAllToList(ArrayList<ParameterCondition> parameterConditions, SnapEntity entity, ActionMemory memory){
		//First the basic parameters
		for(SnapParameter param : entity.parameters){
			ParameterCondition cond = new ParameterCondition();
			
			parameterConditions.add(cond);
		}
		//TODO
	}*/
	
	//  Condition
	//    Must (not) have (with certainty u) Entity with property x
	//      which has parameter y
	//        with value p
	//        with velocity q
	//      which has complex parameter z
	//        with value m
	//        with velocity n
}

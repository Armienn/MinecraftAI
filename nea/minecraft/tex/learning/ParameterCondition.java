package nea.minecraft.tex.learning;

import java.util.ArrayList;

public class ParameterCondition {
	public ConditionType valueCondition = ConditionType.None;
	public ConditionType velocityCondition = ConditionType.None;
	public ComplexParameter parameter;
	
	double boundUpper = 0;
	double boundLower = 0;
	
	ArrayList<Double> observedValues = new ArrayList<Double>();
	ArrayList<Double> observedVelocities = new ArrayList<Double>();
	
	public ParameterCondition(ComplexParameter comparam, SnapEntity entity, ActionMemory memory){
		parameter = comparam;
		observedValues.add(parameter.Evaluate(entity, memory));
		if(parameter.DefinesVelocity())
			observedVelocities.add(parameter.EvaluateVelocity(entity, memory));
	}
	
	//public void AddObservation(
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		if(level > 2) return "ParameterCondition - Val:" + valueCondition + " Vel:" + velocityCondition;
		
		String result = "ParameterCondition - ValueCondition: " + valueCondition + " VelocityCondition: " + velocityCondition + "\n";
		result += tab + "\t" + parameter.toString(level+1) + "\n";
		result += tab + "\tDelays " + observedValues + "\n";
		result += tab + "\tValues " + observedVelocities;
		return result;
	}
	
	enum ConditionType { Above, Below, Between, Outside, None };
}

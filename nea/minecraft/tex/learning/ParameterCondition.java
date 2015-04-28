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
	
	enum ConditionType { Above, Below, Between, Outside, None };
}

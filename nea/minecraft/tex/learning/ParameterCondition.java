package nea.minecraft.tex.learning;

import nea.minecraft.tex.memory.MemorySnapshot;

public class ParameterCondition {
	public ConditionType valueType;
	public ConditionType velocityType;
	public ComplexParameter parameter;
	//valuestuff
	
	public ParameterCondition(ComplexParameter comparam, SnapEntity entity, ActionMemory memory){
		//TODO
	}
	
	enum ConditionType { Above, Below, Between, Outside, None };
}

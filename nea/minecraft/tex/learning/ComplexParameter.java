package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.MemorySnapshot;

public class ComplexParameter {
	public final ComplexType type;
	
	public ComplexParameter(String basictype){
		type = ComplexType.Basic;
		//TODO
	}
	
	public static ComplexParameter[] GetComplexParameters(SnapEntity entity, ActionMemory memory){
		ArrayList<ComplexParameter> parameters = new ArrayList<ComplexParameter>();
		//First the basic parameters
		for(SnapParameter param : entity.parameters){
			parameters.add(new ComplexParameter(param.GetType()));
		}
		//TODO
		return null;
	}
	
	public enum ComplexType { Basic, ActionDelta, IntraDelta, InterDelta, DistanceToAI }
}

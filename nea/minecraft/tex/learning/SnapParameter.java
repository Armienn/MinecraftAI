package nea.minecraft.tex.learning;

import nea.minecraft.tex.memory.utility.MemParameter;
import nea.minecraft.tex.memory.utility.ParameterValue;

public class SnapParameter {
	ParameterValue value;
	double velocity;
	
	public SnapParameter(MemParameter source, long time){
		value = source.GetParameter(time);
		velocity = source.GetVelocity(time);
	}
	
	public ParameterValue GetParameter(){
		return value;
	}
	
	public double GetVelocity(){
		return velocity;
	}
}

package nea.minecraft.tex.learning;

import nea.minecraft.tex.memory.utility.MemParameter;
import nea.minecraft.tex.memory.utility.ParameterValue;

public class SnapParameter {
	private String type;
	ParameterValue value;
	double velocity;
	
	public SnapParameter(MemParameter source, long time){
		type = source.GetType();
		value = source.GetParameter(time);
		velocity = source.GetVelocity(time);
	}
	
	public ParameterValue GetParameter(){
		return value;
	}
	
	public double GetVelocity(){
		return velocity;
	}
	
	public String GetType(){
		return type;
	}
}

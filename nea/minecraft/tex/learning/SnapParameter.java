package nea.minecraft.tex.learning;

import java.text.DecimalFormat;

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
	
	public SnapParameter(MemParameter source){
		type = source.GetType();
		value = source.GetParameter();
		velocity = 0;
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
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		//if(level > 2) return "MemParameter";
		DecimalFormat df = new DecimalFormat("#.##");
		String result = "SnapParameter - " + type + " value: " + value + " velocity: " + df.format(velocity);
		return result;
	}
}

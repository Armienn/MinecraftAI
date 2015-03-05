package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

public class EntityMemoryParameter {
	private String type;
	private double initialValue;
	ArrayList<ParameterEvent> events;
	
	public EntityMemoryParameter(double value){
		initialValue = value;
	}
	
	public double GetParameter(){
		if(events == null || events.size() == 0){
			return initialValue;
		}
		else {
			return events.get(events.size()-1).endvalue;
		}
	}
	
	public String GetType(){
		return type;
	}
}

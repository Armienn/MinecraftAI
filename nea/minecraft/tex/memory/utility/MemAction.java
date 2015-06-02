package nea.minecraft.tex.memory.utility;

import nea.minecraft.tex.interaction.Action;

public class MemAction {
	public Action.Type type;
	public float[] parameters;
	public Interval interval;
	public boolean success;
	
	public MemAction(Action action, long time, boolean success){
		interval = new Interval(time, time);
		type = action.GetType();
		parameters = action.GetParameters();
		this.success = success;
	}
	
	public MemAction(Action.Type action, float[] parameters, long starttime, long endtime, boolean success){
		interval = new Interval(starttime, endtime);
		type = action;
		this.parameters = parameters;
		this.success = success;
	}
	
	public MemAction Copy(){
		return new MemAction(type, parameters, interval.startTime, interval.endTime, success);
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
		//if(level > 2) return "Action";
		
		String result = "Action - " + type + " " + (success?"succeeded":"failed") + " " + interval + " " + (parameters != null && parameters.length > 0 ? parameters[0] : "");
		return result;
	}
}

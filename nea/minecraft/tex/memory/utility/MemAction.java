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
}

package nea.minecraft.tex.memory.utility;

import nea.minecraft.tex.interaction.Action;

public class MemAction {
	public Action.Type type;
	public float[] parameters;
	public Interval interval;
	
	public MemAction(Action action, long time){
		interval = new Interval(time, time);
		type = action.GetType();
		parameters = action.GetParameters();
	}
}

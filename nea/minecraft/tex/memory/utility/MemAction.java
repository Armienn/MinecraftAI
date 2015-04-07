package nea.minecraft.tex.memory.utility;

import nea.minecraft.tex.interaction.Action;

public class MemAction {
	public Action.Type type;
	public float[] parameters;
	public long time;
	
	public MemAction(Action action, long time){
		this.time = time;
		type = action.GetType();
		parameters = action.GetParameters();
	}
}

package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Actions;

public class MemEntity {
	
	public int id;
	public Interval appearanceInterval; 
	//private String type;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<MemParameter> parameters = new ArrayList<MemParameter>();
	ArrayList<MemAction> actions = new ArrayList<MemAction>();
	
	public long previousTime;
	public long currentTime;
	
	public MemEntity(int id, String type, long appeartime){
		this.id = id;
		properties.add(type);
		appearanceInterval = new Interval(appeartime, appeartime);
		currentTime = previousTime = appeartime;
	}
	
	public void AddProperty(String property){
		properties.add(property);
	}
	
	public void AddParameter(MemParameter parameter){
		parameters.add(parameter);
	}
	
	public void AddAction(MemAction action){
		actions.add(action);
	}
	
	public MemParameter GetParameter(String type){
		for(MemParameter param : parameters){
			if(param.GetType() == type)
				return param;
		}
		return null;
	}
	
	public String[] GetParameters(){
		String[] list = new String[parameters.size()];
		for(int i=0; i<parameters.size();i++){
			list[i] = parameters.get(i).GetType();
		}
		return list;
	}
	
	public String[] GetProperties(){
		return (String[]) properties.toArray();
	}
	
	public void Update(MemEntity memory, long time){
		previousTime = currentTime;
		currentTime = time;
		appearanceInterval.endTime = currentTime;
		
		for(MemParameter parameter : parameters){
			MemParameter param = memory.GetParameter(parameter.GetType());
			if(param != null)
				parameter.UpdateValue(param.GetParameter(), previousTime, currentTime);
		}
	}
	
	public MemAction[] GetActionsInInterval(Interval interval){
		ArrayList<MemAction> result = new ArrayList<MemAction>();
		for(MemAction a : actions){
			if(a.interval.Intersects(interval))
				result.add(a);
		}
		return result.toArray(new MemAction[0]);
	}
	
	public MemAction[] GetActionsInInterval(Interval interval, boolean success){
		ArrayList<MemAction> result = new ArrayList<MemAction>();
		for(MemAction a : actions){
			if(a.interval.Intersects(interval) && a.success == success)
				result.add(a);
		}
		return result.toArray(new MemAction[0]);
	}
	
	/*public MemAction[] GetEventsInInterval(Interval interval){
		
	}*/
}

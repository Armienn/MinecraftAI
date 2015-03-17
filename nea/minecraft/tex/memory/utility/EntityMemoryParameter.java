package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

public class EntityMemoryParameter {
	private String type;
	private double initialValue;
	ArrayList<ParameterEvent> events;
	
	public EntityMemoryParameter(String type, double value){
		this.type = type;
		initialValue = value;
	}
	
	public double GetParameter(){
		if(events == null || events.size() == 0){
			return initialValue;
		}
		else {
			return events.get(events.size()-1).endValue;
		}
	}
	
	public String GetType(){
		return type;
	}
	
	public void UpdateValue(double value, long lastupdate, long currenttime){
		double delta = value - GetParameter();
		if(Math.abs(delta) > 0.000001){
			if(events == null)
				events = new ArrayList<ParameterEvent>();
			
			if(events.size() == 0){
				events.add(new ParameterEvent(initialValue, lastupdate, value, currenttime));
			}
			else{
				ParameterEvent event = events.get(events.size()-1);
				if(event.endTime < lastupdate){
					events.add(new ParameterEvent(event.endValue, lastupdate, value, currenttime));
				}
				else{
					double oldvel = event.GetVelocity();
					double newvel = delta/(double)(currenttime-lastupdate);
					if(newvel < oldvel*2 && newvel > oldvel*0.5){
						event.endTime = currenttime;
						event.endValue = value;
					}
					else{
						events.add(new ParameterEvent(event.endValue, lastupdate, value, currenttime));
					}
				}
			}
		}
	}
}

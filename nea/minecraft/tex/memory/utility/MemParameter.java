package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

public class MemParameter {
	private String type;
	private ParameterValue initialValue;
	ArrayList<MemEvent> events;
	
	public MemParameter(String type, ParameterValue value){
		this.type = type;
		initialValue = value;
	}
	
	public ParameterValue GetParameter(){
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
	
	public void UpdateValue(ParameterValue value, long previoustime, long currenttime){
		if(value.IsUndefined() && GetParameter().IsUndefined()){
			//no change
		}
		else if(value.IsUndefined() || GetParameter().IsUndefined()){
			//going from undefined to defined or vice versa
			if(events == null)
				events = new ArrayList<MemEvent>();
			events.add(new MemEvent(initialValue, previoustime, value, currenttime));
		}
		else{
			//possibly going from one value to another
			double delta = value.value - GetParameter().value;
			if(Math.abs(delta) > 0.000001){
				if(events == null)
					events = new ArrayList<MemEvent>();
				
				if(events.size() == 0){
					events.add(new MemEvent(initialValue, previoustime, value, currenttime));
				}
				else{
					MemEvent event = events.get(events.size()-1);
					if(event.interval.endTime < previoustime){
						events.add(new MemEvent(event.endValue, previoustime, value, currenttime));
					}
					else{
						double oldvel = event.GetVelocity();
						double newvel = delta/(double)(currenttime-previoustime);
						if(Math.signum(newvel) == Math.signum(oldvel) && Math.abs(newvel) < Math.abs(oldvel)*2 && Math.abs(newvel) > Math.abs(oldvel)*0.5){
							event.interval.endTime = currenttime;
							event.endValue = value;
						}
						else{
							events.add(new MemEvent(event.endValue, previoustime, value, currenttime));
						}
					}
				}
			}
		}
	}
}
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
	
	public void UpdateValue(ParameterValue value, long lastupdate, long currenttime){
		if(value.IsUndefined() && GetParameter().IsUndefined()){
			//no change
		}
		else if(value.IsUndefined() || GetParameter().IsUndefined()){
			//going from undefined to defined or vice versa
			if(events == null)
				events = new ArrayList<MemEvent>();
			events.add(new MemEvent(initialValue, lastupdate, value, currenttime));
		}
		else{
			//possibly going from one value to another
			double delta = value.value - GetParameter().value;
			if(Math.abs(delta) > 0.000001){
				if(events == null)
					events = new ArrayList<MemEvent>();
				
				if(events.size() == 0){
					events.add(new MemEvent(initialValue, lastupdate, value, currenttime));
				}
				else{
					MemEvent event = events.get(events.size()-1);
					if(event.endTime < lastupdate){
						events.add(new MemEvent(event.endValue, lastupdate, value, currenttime));
					}
					else{
						double oldvel = event.GetVelocity();
						double newvel = delta/(double)(currenttime-lastupdate);
						if(newvel < oldvel*2 && newvel > oldvel*0.5){
							event.endTime = currenttime;
							event.endValue = value;
						}
						else{
							events.add(new MemEvent(event.endValue, lastupdate, value, currenttime));
						}
					}
				}
			}
		}
	}
}

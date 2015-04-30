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
	
	public MemParameter(MemParameter source, Interval interval){
		type = source.type;
		initialValue = source.GetParameter(interval.startTime);
		if(source.events == null || source.events.size() == 0){
			;
		}
		else {
			for(int i=0; i<source.events.size(); i++){
				MemEvent event = events.get(i);
				ParameterValue startvalue;
				ParameterValue endvalue;
				Interval inter;
				if(interval.Contains(event.interval)){
					startvalue = event.startValue;
					endvalue = event.endValue;
					inter = new Interval(event.interval.startTime, event.interval.endTime);
				}
				else if(interval.Intersects(event.interval)){
					if(interval.startTime == event.interval.endTime || interval.endTime == event.interval.startTime){
						continue;
					}
					inter = new Interval(event.interval.startTime, event.interval.endTime);
					startvalue = event.startValue;
					endvalue = event.endValue;
					if(event.interval.startTime < interval.startTime){ // cut start off event
						inter = new Interval(interval.startTime, inter.endTime);
						startvalue = new ParameterValue(event.startValue.value + event.GetVelocity()*inter.Length());
					}
					if(event.interval.endTime < interval.endTime){ // cut end off event
						inter = new Interval(inter.startTime, interval.endTime);
						endvalue = new ParameterValue(event.endValue.value - event.GetVelocity()*inter.Length());
					}
					
				}
				else {
					continue;
				}
				events.add(new MemEvent(startvalue, endvalue, inter));
			}
		}
	}
	
	public ParameterValue GetParameter(){
		if(events == null || events.size() == 0){
			return initialValue;
		}
		else {
			return events.get(events.size()-1).endValue;
		}
	}
	
	public ParameterValue GetParameter(long time){
		if(events == null || events.size() == 0){
			return initialValue;
		}
		else {
			ParameterValue val = new ParameterValue();
			for(int i=0; i<events.size(); i++){
				MemEvent event = events.get(i);
				if(time < event.interval.startTime)
					return val;
				else if(event.interval.Contains(time)){
					if(event.IsAppearanceOrDisapperance()){
						if(time == event.interval.startTime){
							return new ParameterValue(event.startValue);
						}
						else{
							return new ParameterValue(event.endValue);
						}
					}
					else{
						long dif = time - event.interval.startTime;
						double delta = ((double)dif) * event.GetVelocity();
						return new ParameterValue(delta + event.startValue.value);
					}
				}
				else{
					val = new ParameterValue(event.endValue);
				}
			}
			return val;
		}
	}
	
	public double GetVelocity(long time){
		if(events == null || events.size() == 0){
			return 0;
		}
		else {
			for(MemEvent event : events){
				if(event.interval.Contains(time))
					return event.GetVelocity();
			}
			return 0;
		}
	}
	
	public ArrayList<MemEvent> GetEvents(){
		return events;
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
	
	public MemParameter Copy(){
		MemParameter param = new MemParameter(type, initialValue);
		param.events = (ArrayList<MemEvent>) events.clone();
		return param;
	}
}

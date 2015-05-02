package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.utility.MemEvent;

public class EventEffect {
	int observations;
	public final EventType type;
	public final ComplexParameter parameter;
	
	ArrayList<Integer> observedDelays = new ArrayList<Integer>();
	ArrayList<Double> observedValues = new ArrayList<Double>();
	
	public EventEffect(EventType type){
		observations = 1;
		this.type = type;
		parameter = null;
	}
	
	public EventEffect(String param, MemEvent event, ActionMemory memory){
		observations = 1;
		observedDelays.add((int)(event.interval.startTime - memory.action.interval.startTime));
		parameter = new ComplexParameter(param);
		if(event.interval.Length() <= 2){
			if(event.IsAppearanceOrDisapperance()){
				type = EventType.Set;
				observedValues.add(event.endValue.defined ? event.endValue.value : null);
			}
			else{
				type = EventType.Add;
				observedValues.add(event.endValue.value - event.endValue.value);	
			}
		}
		else{
			type = EventType.Velocity;
			observedValues.add(event.GetVelocity());
		}
	}
	
	public enum EventType { Appearance, Disappearance, Add, Set, Velocity }
}

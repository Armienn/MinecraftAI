package nea.minecraft.tex.memory.utility;

public class MemEvent {
	public ParameterValue startValue;
	public ParameterValue endValue;
	public Interval interval;
	
	public MemEvent(ParameterValue startvalue, long starttime, ParameterValue endvalue, long endtime){
		startValue = startvalue;
		endValue = endvalue;
		interval = new Interval(starttime, endtime);
	}
	
	public MemEvent(ParameterValue startvalue, ParameterValue endvalue, Interval interval){
		startValue = startvalue;
		endValue = endvalue;
		this.interval = new Interval(interval.startTime, interval.endTime);
	}
	
	public double GetVelocity(){
		if(IsAppearanceOrDisapperance())
			return 0;
		return (endValue.value-startValue.value)/(double)(interval.Length());
	}
	
	public boolean IsAppearanceOrDisapperance(){
		return startValue.IsUndefined() || endValue.IsUndefined();
	}
}

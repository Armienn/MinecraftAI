package nea.minecraft.tex.memory.utility;

public class MemEvent {
	public ParameterValue startValue;
	public ParameterValue endValue;
	public long startTime;
	public long endTime;
	
	public MemEvent(ParameterValue startvalue, long starttime, ParameterValue endvalue, long endtime){
		startValue = startvalue;
		endValue = endvalue;
		startTime = starttime;
		endTime = endtime;
	}
	
	public double GetVelocity(){
		if(IsAppearanceOrDisapperance())
			return 0;
		return (endValue.value-startValue.value)/(double)(endTime-startTime);
	}
	
	public boolean IsAppearanceOrDisapperance(){
		return startValue.IsUndefined() || endValue.IsUndefined();
	}
}

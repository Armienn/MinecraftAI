package nea.minecraft.tex.memory.utility;

public class ParameterEvent {
	public double startValue;
	public double endValue;
	public long startTime;
	public long endTime;
	
	public ParameterEvent(double startvalue, long starttime, double endvalue, long endtime){
		startValue = startvalue;
		endValue = endvalue;
		startTime = starttime;
		endTime = endtime;
	}
}

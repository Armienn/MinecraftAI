package nea.minecraft.tex.memory.utility;

public class Interval {
	public final long startTime;
	public long endTime;
	
	public Interval(long start, long end){
		startTime = start;
		endTime = end;
	}
	
	public long Length(){
		return endTime-startTime;
	}
}

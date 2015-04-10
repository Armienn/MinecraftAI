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
	
	public Interval Offset(long offset){
		return new Interval(startTime + offset, endTime + offset);
	}
	
	public boolean Intersects(Interval other){
		if(startTime < other.startTime && other.startTime <= endTime)
			return true;
		else if(other.startTime < startTime && startTime <= other.endTime)
			return true;
		else if(startTime == other.startTime)
			return true;
		else return false;
	}
	
	public boolean Contains(long time){
		return startTime <= time && time <= endTime;
	}
}

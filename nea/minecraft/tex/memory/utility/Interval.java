package nea.minecraft.tex.memory.utility;

public class Interval {
	public final long startTime;
	public final long endTime;
	
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
	
	public Interval Intersection(Interval other){
		if(Intersects(other)){
			return new Interval( other.startTime < startTime ? startTime : other.startTime, other.endTime < endTime ? other.endTime : endTime);
		}
		else return null;
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
	
	public boolean Contains(Interval interval){
		return startTime <= interval.startTime && interval.endTime <= endTime;
	}
	
	@Override
	public String toString(){
		return "I[" + startTime + "-" + endTime + "]";
	}
}

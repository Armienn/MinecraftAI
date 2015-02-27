package nea.minecraft.tex.memory.utility;

public class Movement {
	public long startTime;
	public double startPosX;
	public double startPosY;
	public double startPosZ;

	public long endTime;
	public double endPosX;
	public double endPosY;
	public double endPosZ;
	
	public Movement(long starttime, double sx, double sy, double sz, long endtime, double ex, double ey, double ez){
		startTime = starttime;
		startPosX = sx;
		startPosY = sy;
		startPosZ = sz;
		endTime = endtime;
		endPosX = ex;
		endPosY = ey;
		endPosZ = ez;
	}
}

package nea.minecraft.tex;

import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;
import net.minecraft.world.World;

public class TexBrain {
	public World worldObj;
	public int id;
	public Actions actions = new Actions();
	public Senses senses = new Senses();
	
	private long lastupdate = 0;
	
	public TexBrain(World worldin, int id){
		worldObj = worldin;
		this.id = id;
	}
	
	public boolean KeepRunning(){
		long currenttime = System.currentTimeMillis();
		boolean keeprunning = false;
		synchronized(this){
			keeprunning = currenttime - lastupdate > 1000 ? true : false;
		}
		return keeprunning;
	}
	
	public void Updated(){
		synchronized(this){
			lastupdate = System.currentTimeMillis();
		}
	}
}

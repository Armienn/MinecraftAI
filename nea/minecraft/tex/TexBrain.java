package nea.minecraft.tex;

import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Rewards;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.tex.memory.SensoryMemory;
import nea.minecraft.tex.memory.ShortTermMemory;
import net.minecraft.world.World;

public class TexBrain {
	public World worldObj;
	public int id;
	public Actions actions = new Actions(this);
	public Senses senses = new Senses(this);
	public Rewards rewards = new Rewards(this);
	
	public SensoryMemory sensememory = new SensoryMemory(this);
	public ShortTermMemory shortmemory = new ShortTermMemory(this);
	
	private long lastupdate = 0;
	
	public TexBrain(World worldin, int id){
		worldObj = worldin;
		this.id = id;
		lastupdate = System.currentTimeMillis();
	}
	
	public boolean KeepRunning(){
		long currenttime = System.currentTimeMillis();
		boolean keeprunning = false;
		synchronized(this){
			keeprunning = currenttime - lastupdate > 1000 ? false : true;
		}
		return keeprunning;
	}
	
	public void Updated(){
		synchronized(this){
			lastupdate = System.currentTimeMillis();
		}
	}
}

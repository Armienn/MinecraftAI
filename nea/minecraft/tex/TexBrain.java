package nea.minecraft.tex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Rewards;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.tex.memory.SensoryMemory;
import nea.minecraft.tex.memory.ShortTermMemory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TexBrain {
	private Logger logger = LogManager.getLogger();
	public World worldObj;
	public int id;
	public Actions actions = new Actions(this);
	public Senses senses = new Senses(this);
	public Rewards rewards = new Rewards(this);
	
	public SensoryMemory sensememory = new SensoryMemory(this);
	public ShortTermMemory shortmemory = new ShortTermMemory(this);
	
	private long lastupdate = 0;
	private boolean alive = true;
	
	public TexBrain(World worldin, int id){
		worldObj = worldin;
		this.id = id;
		lastupdate = System.currentTimeMillis();
	}
	
	public void OnDeath(){
		alive = false;
	}
	
	public boolean KeepRunning(){
		if(!alive){
			return false;
		}
		boolean serverisrunning = ((WorldServer)worldObj).func_73046_m().isServerRunning();
		if(!serverisrunning){
			return false;
		}
		return true;
	}
	
	public boolean Pause(){
		if(!KeepRunning()){
			return false; 
		}
		long currenttime = System.currentTimeMillis();
		boolean pause = false;
		synchronized(this){
			pause = currenttime - lastupdate > 200 ? true : false;
		}
		return pause;
	}
	
	public void Updated(){
		synchronized(this){
			lastupdate = System.currentTimeMillis();
		}
	}
	
	public void Say(String text){
		try{
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Tex #" + id + ": " + text));
		}
		catch(Exception e){ }
	}
	
	public void Log(String text){
		logger.info("Tex #" + id + ": " + text);
	}
}

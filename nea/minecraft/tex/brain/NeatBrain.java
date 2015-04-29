package nea.minecraft.tex.brain;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nea.rtNEAT.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class NeatBrain {
	private Logger logger = LogManager.getLogger();
	public World worldObj;
	public int id;
	
	public BrainSenses senses = new BrainSenses(this);
	
	private long lastupdate = 0;
	private double random = 0;
	
	public NeatBrain(World worldin, int id){
		worldObj = worldin;
		this.id = id;
		lastupdate = System.currentTimeMillis();
		random = Neat.randfloat();
		Neat.load_neat_params("NEAT_params.ne", true);
		Population p = new Population();
	}
	
	public void OnDeath(){
		senses.alive = false;
	}
	
	public boolean KeepRunning(){
		if(!senses.alive){
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
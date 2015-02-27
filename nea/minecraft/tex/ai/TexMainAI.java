package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexMainAI extends Thread {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	
	public TexMainAI(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		logger.info("Tex #" + brain.id + ": Starting Main AI thread");
		brain.Say("Good morning, world!");
		while(brain.KeepRunning()){
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		logger.info("Tex #" + brain.id + ": Ending Main AI thread");
	}
	
	static boolean trySleep(long milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

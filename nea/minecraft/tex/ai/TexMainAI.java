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
		logger.info("Starting Main AI thread of Tex #" + brain.id);
		while(brain.KeepRunning()){
			trySleep(100);
		}
		logger.info("Ending Main AI thread of Tex #" + brain.id);
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

package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexLearning extends Thread {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	
	public TexLearning(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		logger.info("Tex #" + brain.id + ": Starting Learning thread");
		while(brain.KeepRunning()){
			trySleep(100);
		}
		logger.info("Tex #" + brain.id + ": Ending Learning thread");
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

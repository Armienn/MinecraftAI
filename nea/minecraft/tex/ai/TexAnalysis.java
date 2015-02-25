package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexAnalysis extends Thread {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	
	public TexAnalysis(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		logger.info("Starting Analysis thread of Tex #" + brain.id);
		while(brain.KeepRunning()){
			
		}
		logger.info("Ending Analysis thread of Tex #" + brain.id);
	}

}

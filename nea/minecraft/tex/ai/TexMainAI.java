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
		while(brain.KeepRunning()){
			
		}
	}

}

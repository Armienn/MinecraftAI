package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;

public class TexMainAI extends Thread {
	TexBrain brain;
	
	public TexMainAI(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		brain.logger.info("Tex #" + brain.id + ": Starting Main AI thread");
		brain.Say("Good morning, world!");
		while(brain.KeepRunning()){
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		brain.logger.info("Tex #" + brain.id + ": Ending Main AI thread");
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

package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;

public class TexLearning extends Thread {
	TexBrain brain;
	
	public TexLearning(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		brain.logger.info("Tex #" + brain.id + ": Starting Learning thread");
		while(brain.KeepRunning()){
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		brain.logger.info("Tex #" + brain.id + ": Ending Learning thread");
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

package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.learning.LearningResult;

public class TexLearning extends Thread {
	TexBrain brain;
	LearningResult learningResult;
	long lastCheck = 0;
	
	public TexLearning(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		brain.Log("Starting Learning thread");
		while(brain.KeepRunning()){
			// find out if a reward has been received some time since lastCheck
			// for each reward:
			//   extract episode of fixed length from short term memory
			//   for each function type:
			//     (analyse episode: Function.Analyse(episode, learningResult))
			//     for each valid input to the function type, among content of episode:
			//       Add function observation to list of observations with this input type
			//       Reevaluate observation list to produce function approximation
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		brain.Log("Ending Learning thread");
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

package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.learning.LearningResult;
import nea.minecraft.tex.memory.ShortTermMemory;
import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemAction;

public class TexLearning extends Thread {
	TexBrain brain;
	LearningResult learningResult;
	long lastCheck = 0;
	
	static final int consequencetime = 20;
	
	public TexLearning(TexBrain brain){
		this.brain = brain;
		this.setName("TexLearning");
	}
	
	public void run(){
		brain.Log("Starting Learning thread");
		while(brain.KeepRunning()){
			synchronized(brain){
				ShortTermMemory memory = brain.shortmemory;
				if(memory.selfMemory != null){
					long currenttime = memory.currentTime;
					Interval newInterval = new Interval(lastCheck + 1, currenttime);
					Interval leadingInterval = newInterval.Offset(- consequencetime);
					Interval trailingInterval = new Interval(leadingInterval.endTime, currenttime);
					
					/// Examine results of actions:
					MemAction[] actions = memory.selfMemory.GetActionsInInterval(leadingInterval, true);
					for(MemAction a : actions){
						//actionmemory = new actionmemory
						//entity[] ents = blabla
						//for each entity
						//  for each property
						//    Event[] events = get events in trailing interval
						//    for each event
						//      (if event begins after actions)
						//      add possible connection to actionmemory
						//    if entity appeared in trailing interval
						//      add possible connection to actionmemory
						//for each reward
						//  add possible connection to actionmemory
						//use actionmemory to update actionknowledge(?)
					}
					
					
					/// Examine failed actions:
					// actions = memory.selfMemory.GetActionsInInterval(newInterval, false);
					
					/// Examine time before rewards:
					// memory.selfMemory.GetRewardsInInterval(examineInterval);
					
					///
					lastCheck = currenttime;
				}
			}
			//brain.shortmemory.currentTime
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

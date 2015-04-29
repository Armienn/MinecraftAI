package nea.minecraft.tex.ai;

import nea.minecraft.tex.brain.NeatBrain;
import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.learning.ActionKnowledge;
import nea.minecraft.tex.learning.ActionMemory;
import nea.minecraft.tex.learning.LearningResult;
import nea.minecraft.tex.memory.ShortTermMemory;
import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemAction;

public class TexLearning extends Thread {
	TexBrain brain;
	LearningResult learningResult;
	long lastCheck = 0;
	boolean useNEAT = false;
	
	static final int consequencetime = 20;
	
	// NEAT
	NeatBrain neatbrain;
	
	//NEAT END
	
	public TexLearning(TexBrain brain, boolean useneat){
		this.brain = brain;
		this.setName("TexLearning");
		this.useNEAT = useneat;
		if(useneat){
			neatbrain = new NeatBrain(brain);
		}
	}
	
	public void run(){
		brain.Log("Starting Learning thread");
		if(useNEAT) NEAT();
		else Homebrew();
		brain.Log("Ending Learning thread");
	}
	
	private void Homebrew(){
		while(brain.KeepRunning()){
			synchronized(brain){
				ShortTermMemory memory = brain.memory.shortterm;
				if(memory.selfMemory != null){
					long currenttime = memory.currentTime;
					Interval newInterval = new Interval(lastCheck + 1, currenttime);
					Interval leadingInterval = newInterval.Offset(- consequencetime);
					//Interval trailingInterval = new Interval(leadingInterval.endTime, currenttime);
					
					/// Examine results of actions:
					MemAction[] actions = memory.selfMemory.GetActionsInInterval(leadingInterval, true);
					for(MemAction a : actions){
						ActionMemory actionmemory = new ActionMemory(a, memory, consequencetime);
						ActionKnowledge knowledge = brain.knowledge.GetKnowledge(a.type);
						knowledge.Process(actionmemory);
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
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
	}
	
	private void NEAT(){
		
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

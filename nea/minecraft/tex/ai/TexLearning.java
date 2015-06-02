package nea.minecraft.tex.ai;

import nea.minecraft.tex.brain.BrainSenses;
import nea.minecraft.tex.brain.NeatCore;
import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.tex.learning.ActionKnowledge;
import nea.minecraft.tex.learning.ActionMemory;
import nea.minecraft.tex.learning.LearningResult;
import nea.minecraft.tex.memory.ShortTermMemory;
import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemAction;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemReward;
import nea.rtNEAT.Network;
import nea.rtNEAT.Nnode;

public class TexLearning extends Thread {
	TexBrain brain;
	LearningResult learningResult;
	long lastCheck = 0;
	boolean useNEAT = false;
	
	static final int consequencetime = 20;
	
	// NEAT
	NeatCore neatbrain;
	long lastUpdate = 0;
	//NEAT END
	
	public TexLearning(TexBrain brain, boolean useneat){
		this.brain = brain;
		this.setName("TexLearning");
		this.useNEAT = useneat;
		if(useneat){
			neatbrain = new NeatCore(brain);
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
						//if(a.type==Action.Type.Jump)
						//	a = a;
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
		while(brain.KeepRunning()){
			synchronized(brain){
				Senses senses = brain.senses.senses;
				
				if(senses.time > lastUpdate){
					for(int i=0; i<3; i++){
						neatbrain.neatnet.inputs.add(new Nnode(Nnode.nodetype.SENSOR, i));
					}
					double input1 = 0;
					if(senses.entityinfo.size() > 0){
						MemEntity entity = senses.entityinfo.get(0);
						input1 = entity.GetProperties()[0].hashCode();
					}
					neatbrain.neatnet.inputs.get(0).sensor_load(input1);
					
					input1 = senses.self.GetProperties()[0].hashCode();
					neatbrain.neatnet.inputs.get(1).sensor_load(input1);
					
					if(senses.rewards.size() > 0){
						MemReward entity = senses.rewards.get(0);
						input1 = entity.value.value;
					}
					neatbrain.neatnet.inputs.get(2).sensor_load(input1);
					
					Action.Type[] types = Action.GetTypes();
					int neatoutput = (int) (neatbrain.neatnet.outputs.get(0).output *types.length);
					Action.Type type = types[neatoutput];
					Action action = new Action(type);
					brain.senses.actions.actions.add(action);
					
					int neatoutput1 = (int) (neatbrain.neatnet.outputs.get(1).output);				
					
					action.SetParameter(0, neatoutput1);
				}
			}
		}
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

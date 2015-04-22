package nea.minecraft.tex.ai;

import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemReward;

public class TexAnalysis extends Thread {
	TexBrain brain;
	
	public TexAnalysis(TexBrain brain){
		this.brain = brain;
		this.setName("TexAnalysis");
	}
	
	public void run(){
		brain.Log("Starting Analysis thread");
		while(brain.KeepRunning()){
			synchronized(brain){
				while( brain.memory.sensory.memorysenses.size() > 0){
					Senses nextsenses = brain.memory.sensory.memorysenses.get(0);
					brain.memory.shortterm.StartUpdate(nextsenses.time);
					for(MemEntity mem : nextsenses.entityinfo){
						brain.memory.shortterm.Update(mem);
					}
					for(MemReward reward : nextsenses.rewards){
						brain.memory.shortterm.AddReward(reward);
					}
					brain.memory.shortterm.UpdateSelf(nextsenses.self);
					
					brain.memory.sensory.memorysenses.remove(0);
				}
				while( brain.memory.sensory.memoryactions.size() > 0){
					Actions nextactions = brain.memory.sensory.memoryactions.get(0);
					for(Action action : nextactions.succeededactions){
						brain.memory.shortterm.Update(action, true, nextactions.time);
					}
					for(Action action : nextactions.failedactions){
						brain.memory.shortterm.Update(action, false, nextactions.time);
					}
					brain.memory.sensory.memoryactions.remove(0);
				}
			}
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		brain.Log("Ending Analysis thread");
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

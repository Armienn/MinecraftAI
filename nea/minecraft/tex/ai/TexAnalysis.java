package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;
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
				while( brain.sensememory.memorysenses.size() > 0){
					Senses nextsenses = brain.sensememory.memorysenses.get(0);
					brain.shortmemory.StartUpdate(nextsenses.time);
					for(MemEntity mem : nextsenses.entityinfo){
						brain.shortmemory.Update(mem);
					}
					for(MemReward reward : nextsenses.rewards){
						brain.shortmemory.AddReward(reward);
					}
					brain.shortmemory.UpdateSelf(nextsenses.self);
					
					brain.sensememory.memorysenses.remove(0);
				}
				while( brain.sensememory.memoryactions.size() > 0){
					Actions nextactions = brain.sensememory.memoryactions.get(0);
					for(Action action : nextactions.succeededactions){
						brain.shortmemory.Update(action, true, nextactions.time);
					}
					for(Action action : nextactions.failedactions){
						brain.shortmemory.Update(action, false, nextactions.time);
					}
					brain.sensememory.memoryactions.remove(0);
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

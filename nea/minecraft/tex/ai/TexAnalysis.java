package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.utility.ItemInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexAnalysis extends Thread {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	
	public TexAnalysis(TexBrain brain){
		this.brain = brain;
	}
	
	public void run(){
		logger.info("Tex #" + brain.id + ": Starting Analysis thread");
		while(brain.KeepRunning()){
			synchronized(brain){
				while( brain.sensememory.memory.size() > 0){
					Senses nextsenses = brain.sensememory.memory.get(0);
					brain.shortmemory.StartUpdate(nextsenses.time);
					/// items : ///
					for(ItemInfo item : nextsenses.nearbyitems){
						brain.shortmemory.Update(item);
					}
					brain.shortmemory.RemoveUnupdatedItems();
					
					/// end : remove the processed memory ///
					brain.sensememory.memory.remove(0);
				}
			}
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		logger.info("Tex #" + brain.id + ": Ending Analysis thread");
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

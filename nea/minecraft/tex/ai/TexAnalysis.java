package nea.minecraft.tex.ai;

import nea.minecraft.tex.TexBrain;
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
					/// items : ///
					brain.shortmemory.MarkItemsForUpdate();
					for(ItemInfo item : brain.sensememory.memory.get(0).nearbyitems){
						brain.shortmemory.Update(item);
						/*if(brain.shortmemory.CurrentlyExists(item)){
							brain.shortmemory.Update(item);
						}
						else{
							brain.shortmemory.Add(item);
						}*/
					}
					brain.shortmemory.RemoveUnupdatedItems();
					
					
					brain.sensememory.memory.remove(0);
				}
			}
			trySleep(200);
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

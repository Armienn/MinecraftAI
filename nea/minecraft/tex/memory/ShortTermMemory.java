package nea.minecraft.tex.memory;

import nea.minecraft.tex.TexBrain;
import nea.minecraft.utility.ItemInfo;

public class ShortTermMemory {
	TexBrain brain;
	
	public ShortTermMemory(TexBrain brain){
		this.brain = brain;
	}
	
	public boolean CurrentlyExists(ItemInfo item){
		// TODO
		return false;
	}
	
	public void Add(ItemInfo item){
		// TODO
	}
	
	public void Update(ItemInfo item){
		// TODO
	}

	public void MarkItemsForUpdate() {
		// TODO
	}

	public void RemoveUnupdatedItems() {
		// TODO
	}
}

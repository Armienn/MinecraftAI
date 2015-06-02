package nea.minecraft.tex.memory.utility;

import nea.minecraft.tex.brain.TexBrain;

public class MemReward {
	public long time;
	public TexBrain.RewardType type;
	public ParameterValue value;
	
	public MemReward(TexBrain.RewardType type, double value, long time){
		this.type = type;
		this.value = new ParameterValue(value);
		this.time = time;
	}
	
	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		//if(level > 2) return "Reward";
		
		String result = "Reward - " + type + " " + value + "\n";
		return result;
	}
}

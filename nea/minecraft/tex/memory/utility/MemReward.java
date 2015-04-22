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
}

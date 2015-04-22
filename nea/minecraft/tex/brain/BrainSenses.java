package nea.minecraft.tex.brain;

import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Rewards;
import nea.minecraft.tex.interaction.Senses;

public class BrainSenses {
	TexBrain brain;
	public Actions actions = new Actions(brain);
	public Senses senses = new Senses(brain);
	public Rewards rewards = new Rewards(brain);
	protected boolean alive = true;

	public BrainSenses(TexBrain brain){
		this.brain = brain;
	}
	
	public boolean IsAlive(){
		return alive;
	}
}

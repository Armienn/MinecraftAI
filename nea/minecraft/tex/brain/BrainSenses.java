package nea.minecraft.tex.brain;

import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;

public class BrainSenses {
	TexBrain brain;
	public Actions actions;
	public Senses senses;
	protected boolean alive = true;

	public BrainSenses(TexBrain brain){
		this.brain = brain;
		
		actions = new Actions(brain);
		senses = new Senses(brain);
	}

	public boolean IsAlive(){
		return alive;
	}
}

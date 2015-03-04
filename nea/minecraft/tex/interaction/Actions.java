package nea.minecraft.tex.interaction;

import java.util.ArrayList;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;

public class Actions {
	TexBrain brain;
	public long time; 
	/// Takeable actions : ///
	public ArrayList<Action> actions = new ArrayList<Action>();
	
	public Actions(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(EntityTex entity) {
		time = brain.worldObj.getTotalWorldTime();
		for(Action action : actions){
			switch(action.GetType()){
			case Move:
				if (entity.onGround) {
					try{
						float angle = action.GetParameter(0);
						float speed = action.GetParameter(1);
						entity.motionX = speed*Math.cos(angle*Math.PI);
						entity.motionZ = speed*Math.sin(angle*Math.PI);
						entity.rotationYaw = angle*360;
					}
					catch(Exception e) { }
				}
				break;
			case Jump:
				if (entity.onGround) {
					entity.motionY = 0.45;
				}
				break;
			case Use:
				
				break;
			default:
				break;
			}
		}
	}
	
	public Actions Copy(){
		Actions actions = new Actions(brain);
		actions.time = time;
		actions.actions.addAll(this.actions);
		this.actions.clear();
		return actions;
	}
}

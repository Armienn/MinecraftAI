package nea.minecraft.tex.ai;

import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.interaction.Action;

public class TexMainAI extends Thread {
	TexBrain brain;
	
	public TexMainAI(TexBrain brain){
		this.brain = brain;
		this.setName("TexMainAI");
	}
	
	public void run(){
		brain.Log("Starting Main AI thread");
		brain.Say("Good morning, world!");
		while(brain.KeepRunning()){
			//trySleep(100);
			ChooseRandomAction();
			trySleep(1000);
			while(brain.Pause()){
				trySleep(100);
			}
		}
		brain.Log("Ending Main AI thread");
	}
	
	void ChooseRandomAction(){
		Action.Type[] types = Action.GetTypes();
		int number = (int) (Math.random()*types.length);
		Action.Type type = types[number];
		Action action = new Action(type);
		for(int i=0; i<Action.DegreesOfFreedom(type); i++){
			action.SetParameter(i, (float)Math.random());
		}
		synchronized(brain){
			brain.senses.actions.actions.add(action);
		}
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

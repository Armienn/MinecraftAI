package nea.minecraft.tex.interaction;

import java.util.ArrayList;

public class Action {
	ActionType type;
	float[] parameters;
	
	public Action(ActionType type){
		this.type = type;
		int dof = DegreesOfFreedom(); 
		if( dof > 0){
			parameters = new float[dof];
		}
	}
	
	public int DegreesOfFreedom(){
		return DegreesOfFreedom(type);
	}
	
	public void SetParameter(int index, float value) throws Exception{
		if( index < DegreesOfFreedom() 
				&& value >= 0 
				&& value <= 1 ){
			parameters[index] = value;
		}
		else{
			throw new Exception("Trying to set parameter that shouldn't exist");
		}
	}
	
	public float GetParameter(int index) throws Exception{
		if( index < DegreesOfFreedom() ){
			return parameters[index];
		}
		else{
			throw new Exception("Trying to get parameter that shouldn't exist");
		}
	}
	
	public ActionType GetType(){
		return type;
	}
	
	public static int DegreesOfFreedom(ActionType type){
		switch(type){
		case Move:
			return 2; // direction and speed
		case Jump:
			return 0;
		case Use:
			return 0;
		default:
			return 0;
		}
	}
}

enum ActionType {Move, Jump, Use}
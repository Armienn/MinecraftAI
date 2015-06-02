package nea.minecraft.tex.interaction;

public class Action {
	Type type;
	float[] parameters;
	
	public Action(Type type){
		this.type = type;
		int dof = DegreesOfFreedom(); 
		if( dof > 0){
			parameters = new float[dof];
		}
	}
	
	public int DegreesOfFreedom(){
		return DegreesOfFreedom(type);
	}
	
	public void SetParameter(int index, float value) {
		if( index < DegreesOfFreedom() 
				&& value >= 0 
				&& value <= 1 ){
			parameters[index] = value;
		}
		else{
			throw new RuntimeException("Trying to set parameter that shouldn't exist");
		}
	}
	
	public float GetParameter(int index) {
		if( index < DegreesOfFreedom() ){
			return parameters[index];
		}
		else{
			throw new RuntimeException("Trying to get parameter that shouldn't exist");
		}
	}
	
	public float[] GetParameters(){
		if(parameters == null)
			return null;
		return parameters.clone();
	}
	
	public Type GetType(){
		return type;
	}
	
	public static Type[] GetTypes(){
		return Type.values();
	}
	
	public static int DegreesOfFreedom(Type type){
		switch(type){
		case Move:
			return 1; // direction (and not speed)
		case Jump:
			return 0;
		case PickUp:
			return 1; // Pick up closest item to inventory slot [value*8]
		case Drop:
			return 1; // Drop item in inventory slot [value*8]
		case Eat:
			return 1; // Use item in inventory slot [value*8]
		default:
			return 0;
		}
	}
	
	public enum Type{
		Move, Jump, PickUp, Drop, Eat
	}
}
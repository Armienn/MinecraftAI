package nea.minecraft.tex.memory.utility;

public class ParameterValue {
	public final double value;
	public final boolean defined;
	
	public ParameterValue(){
		value = 0;
		defined = false;
	}
	
	public ParameterValue(double value){
		//if(value >= 1 || value < 0)
		//	throw new RuntimeException("Tried to set parameter to value outside of 0 to 1 (" + value + ")");
		this.value = value;
		defined = true;
	}
	
	public ParameterValue(ParameterValue source){
		value = source.value;
		defined = source.defined;
	}
	
	public boolean IsUndefined(){
		return !defined;
	}
}

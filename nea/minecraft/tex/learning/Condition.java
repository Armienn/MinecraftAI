package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.MemorySnapshot;

public class Condition {
	//boolean mustHave;
	int observations;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<ParameterCondition> parameterConditions = new ArrayList<ParameterCondition>();
	
	public Condition(SnapEntity entity, ActionMemory memory){
		observations = 1;
		properties.addAll(entity.properties);
		for(ComplexParameter comparam : ComplexParameter.GetComplexParameters(entity, memory)){
			parameterConditions.add(new ParameterCondition(comparam, entity, memory));
		}
	}
	
	public Condition(ArrayList<String> props){
		observations = 1;
		properties.addAll(props);
	}
	
	public boolean equals(Condition other){
		//TODO: properly finish this
		if(other.properties.size() == properties.size())
			if(other.Fits(properties))
				return true;
		return false;
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
		if(level > 2) return "Condition";
		
		String result = "Condition - \n";
		result += tab + "\tProperties " + properties + "\n";
		for(int i=0; i<parameterConditions.size(); i++){
			result += tab + "\t" + i + ": " + parameterConditions.get(i).toString(level+1) + "\n";
		}
		result += tab + "\tObservations " + observations;
		return result;
	}
	
	public boolean Fits(ArrayList<String> props){
		boolean fit = true;
		for(String s : properties){
			fit = false;
			for(String t : props){
				if(t.equalsIgnoreCase(s)){
					fit = true;
					break;
				}
			}
			if(!fit)
				return false;
		}
		return true;
	}
	
	public boolean Fits(ActionMemory memory){
		//TODO
		return false;
	}
	
	/*public static void AddAllToList(ArrayList<ParameterCondition> parameterConditions, SnapEntity entity, ActionMemory memory){
		//First the basic parameters
		for(SnapParameter param : entity.parameters){
			ParameterCondition cond = new ParameterCondition();
			
			parameterConditions.add(cond);
		}
		//TODO
	}*/
}

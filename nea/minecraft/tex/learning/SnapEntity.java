package nea.minecraft.tex.learning;

import java.util.ArrayList;
import java.util.Arrays;

import nea.minecraft.tex.memory.utility.MemEntity;

public class SnapEntity {
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<SnapParameter> parameters = new ArrayList<SnapParameter>();
	
	public SnapEntity(MemEntity source, long time){
		for(String s : source.GetProperties()){
			properties.add(s);
		}
		String[] params = source.GetParameters();
		for(String param : params){
			parameters.add(new SnapParameter(source.GetParameter(param), time));
		}
	}
	
	public SnapEntity(MemEntity source){
		for(String s : source.GetProperties()){
			properties.add(s);
		}
		String[] params = source.GetParameters();
		for(String param : params){
			parameters.add(new SnapParameter(source.GetParameter(param)));
		}
	}
	
	public SnapParameter GetParameter(String type){
		for(SnapParameter param : parameters){
			if(param.GetType().equalsIgnoreCase(type)){
				return param;
			}
		}
		return null;
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
		if(level > 2) return "SnapEntity";
		
		String result = "SnapEntity\n";
		result += tab + "\t" + properties + "\n";
		for(int i=0; i<parameters.size(); i++){
			result += tab + "\t" + i + ": " + parameters.get(i).toString(level+1) + "\n";
		}
		return result;
	}
}

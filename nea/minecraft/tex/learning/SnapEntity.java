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
	
	public SnapParameter GetParameter(String type){
		for(SnapParameter param : parameters){
			if(param.GetType().equalsIgnoreCase(type)){
				return param;
			}
		}
		return null;
	}
}

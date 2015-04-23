package nea.minecraft.tex.learning;

import java.util.ArrayList;

public class Condition {
	boolean mustHave;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<ParameterCondition> parameterConditions = new ArrayList<ParameterCondition>();
	
	//  Condition
	//    Must (not) have (with certainty u) Entity with property x
	//      which has parameter y
	//        with value p
	//        with velocity q
	//      which has complex parameter z
	//        with value m
	//        with velocity n
}

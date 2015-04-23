package nea.minecraft.tex.learning;

import java.util.ArrayList;

public class ConditionSet {
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	ArrayList<Effect> effects = new ArrayList<Effect>();
	//  ConditionSet
	//    Conditions
	//    Effect
	
	//  Condition
	//    List of:
	//      Must (not) have (with certainty u) Entity with property x
	//        which has parameter y
	//          with value p
	//          with velocity q
	//        which has complex parameter z
	//          with value m
	//          with velocity n
	
	//  Effect
	//    List of:
	//      Entity with so and so properties and (complex) parameters
	//        will (dis)appear
	//        will get event (with velocity or setting value) in (complex) parameter x
	//    Or:
	//      Fails
}

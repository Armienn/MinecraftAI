package nea.minecraft.tex.learning;

import nea.minecraft.tex.memory.Episode;

public class Function {

	
	public void Analyse(Episode episode, LearningResult learning){

		//  for each valid input to the function type, among content of episode:
		//    Add function observation to list of observations with this input type
		//    Reevaluate observation list to produce function approximation
		
		// for each entity in episode:
		//   for each action by entity:
		//     create observation of action(type, entity(id, properties, parameters), time)-reward tuple.
		//     add observation to learningresults list of this functions actionthingstuff
		//     fit
	}
}

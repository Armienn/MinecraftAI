package nea.rtNEAT;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;
//import java.util.logging.Logger;

import org.apache.commons.math3.analysis.function.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//#ifndef _POPULATION_H_
//#define _POPULATION_H_
//
//#include <cmath>
//#include <vector>
//#include "innovation.h"
//#include "genome.h"
//#include "species.h"
//#include "organism.h"
//
//namespace NEAT {
//
//	class Species;
//	class Organism;
//
//	// ---------------------------------------------  
//	// POPULATION CLASS:
//	//   A Population is a group of Organisms   
//	//   including their species                        
//	// ---------------------------------------------  
//	class Population {
public class Population {
	private  Logger logger = LogManager.getLogger();
	// protected:
	//
	// // A Population can be spawned off of a single Genome
	// // There will be size Genomes added to the Population
	// // The Population does not have to be empty to add Genomes
	// bool spawn(Genome *g,int size);
	protected boolean spawn(Genome g, int size) {
		int count;
		Genome new_genome = null;
		Organism new_organism = null;

		// Create size copies of the Genome
		// Start with perturbed linkweights
		for (count = 1; count <= size; count++) {
			// cout<<"CREATING ORGANISM "<<count<<endl;

			new_genome = g.duplicate(count);
			// new_genome->mutate_link_weights(1.0,1.0,GAUSSIAN);
			new_genome.mutate_link_weights(1.0, 1.0,
					Genome.mutator.COLDGAUSSIAN);
			// new_genome.randomize_traits();
			new_organism = new Organism(0.0, new_genome, 1);
			organisms.add(new_organism);
		}

		// Keep a record of the innovation and node number we are on
		cur_node_id = new_genome.get_last_node_id();
		cur_innov_num = new_genome.get_last_gene_innovnum();

		// Separate the new Population into species
		speciate();

		return true;
	}

	//
	// public:
	//
	// std::vector<Organism*> organisms; //The organisms in the Population
	public Vector<Organism> organisms = new Vector<Organism>();
	//
	// std::vector<Species*> species; // Species in the Population. Note that
	// the species should comprise all the genomes
	//
	public Vector<Species> species = new Vector<Species>();
	// // ******* Member variables used during reproduction *******
	// std::vector<Innovation*> innovations; // For holding the genetic
	// innovations of the newest generation
	public Vector<Innovation> innovation = new Vector<Innovation>();
	// int cur_node_id; //Current label number available
	public int cur_node_id;
	// double cur_innov_num;
	public double cur_innov_num;
	//
	// int last_species; //The highest species number
	public int last_species;
	//
	// // ******* Fitness Statistics *******
	// double mean_fitness;
	public double mean_fitness;
	// double variance;
	public double variance;
	// double standard_deviation;
	public double standard_deviation;
	//
	// int winnergen; //An integer that when above zero tells when the first
	// winner appeared
	public int winneraen;
	//
	// // ******* When do we need to delta code? *******
	// double highest_fitness; //Stagnation detector
	public double highest_fitness;
	// int highest_last_changed; //If too high, leads to delta coding
	public int highest_last_changed;
	public Vector<Innovation> innovations;

	//
	// // Separate the Organisms into species
	// bool speciate();
	public boolean speciate() {
		// std::vector<Organism*>::iterator curorg; //For stepping through
		// Population
		// std::vector<Species*>::iterator curspecies; //Steps through species
		Organism comporg = null; // Organism for comparison
		Species newspecies = null; // For adding a new species
		Species curspecies = null;

		int counter = 0; // Species counter

		// Step through all existing organisms
		// for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
		for (Organism curorg : organisms) {

			// if (curspecies==species.end()){
			if (species == null) {
				// Create the first species
				newspecies = new Species(++counter);
				species.add(newspecies);
				newspecies.add_Organism(curorg); // Add the current organism
				curorg.species = newspecies; // Point organism to its species
			} else {
				for(int i=0; i<species.size(); i++){
					// For each organism, search for a species it is compatible to
					curspecies=species.get(i);
					comporg = curspecies.first();
					if (((curorg.gnome).compatibility(comporg.gnome)) < Neat.compat_thresh) {

						// Found compatible species, so add this organism to it
						curspecies.add_Organism(curorg);
						curorg.species = curspecies; // Point organism to its
														// species
						break; // Note the search is over
					}
				}

				// If we didn't find a match, create a new species
				if (comporg == null) {
					newspecies = new Species(++counter);
					species.add(newspecies);
					newspecies.add_Organism(curorg); // Add the current organism
					curorg.species = newspecies; // Point organism to its
													// species
				}

			} // end else

		} // end for

		last_species = counter; // Keep track of highest species

		return true;
	}
	
	public Boolean epoch(int generation) {
		Species curspecies = null;
		Species deadspecies; // = new Vector<Species>();
		//std::vector<Species*>::iterator curspecies;
		//std::vector<Species*>::iterator deadspecies;  //For removing empty Species
		
		Organism curorg = null; // = new Vector<Organism>();
		Organism deadorg = null;// = new Vector<Organism>();
		//std::vector<Organism*>::iterator curorg;
		//std::vector<Organism*>::iterator deadorg;
		
		Innovation curinnov; // = new Vector<Innovation>();
		Innovation deadinnov; // = new Vector<Innovation>();
		//std::vector<Innovation*>::iterator curinnov;  
		//std::vector<Innovation*>::iterator deadinnov;  //For removing old Innovs

		double total = 0.0; //Used to compute average fitness over all Organisms

		double overall_average = 0.0;  //The average modified fitness among ALL organisms

		int orgcount = 0;

		//The fractional parts of expected offspring that can be 
		//Used only when they accumulate above 1 for the purposes of counting
		//Offspring
		double skim = 0.0; 
		int total_expected = 0;  //precision checking
		int total_organisms = organisms.size();
		int max_expected = 0;
		//Species best_species = null;
		//Species *best_species;
		int final_expected = 0;

		int pause = 0;

		//Rights to make babies can be stolen from inferior species
		//and given to their superiors, in order to concentrate exploration on
		//the best species
		int NUM_STOLEN = Neat.babies_stolen; //NEAT::babies_stolen; //Number of babies to steal
		int one_fifth_stolen = 0;
		int one_tenth_stolen = 0;

		Vector<Species> sorted_species = new Vector<Species>();
		Vector<Species> best_species = new Vector<Species>();
		//std::vector<Species*> sorted_species;  //Species sorted by max fit org in Species
		int stolen_babies = 0; //Babies taken from the bad species and given to the champs

		int half_pop = 0;

		int best_species_num = 0;  //Used in debugging to see why (if) best species dies
		boolean best_ok = false;

		//We can try to keep the number of species constant at this number
		int num_species_target = 4;
		int num_species = species.size();
		double compat_mod = 0.3;  //Modify compat thresh to control speciation
		
		int curspeciesi = 0;

		//Keeping species diverse
		//This commented out code forces the system to aim for 
		// num_species species at all times, enforcing diversity
		//This tinkers with the compatibility threshold, which
		// normally would be held constant
		
		/*if (generation>1) {
			if (num_species<num_species_target)
				Neat.compat_thresh-=compat_mod;
			else if (num_species>num_species_target)
				Neat.compat_thresh+=compat_mod;

			if (Neat.compat_thresh<0.3) Neat.compat_thresh=0.3;

		}*/
		

		//Stick the Species pointers into a new Species list for sorting
		/*for(curspecies=species.begin();curspecies!=species.end();++curspecies) {
			sorted_species.push_back(*curspecies);
		}*/
		sorted_species.addAll(species);
		
		//Sort the Species by max fitness (Use an extra list to do this)
		//These need to use ORIGINAL fitness
		//sorted_species.qsort(order_species);
	    //std::sort(sorted_species.begin(), sorted_species.end(), order_species);
	    Collections.sort(sorted_species);
	    
		//Flag the lowest performing species over age 20 every 30 generations 
		//NOTE: THIS IS FOR COMPETITIVE COEVOLUTION STAGNATION DETECTION

		//curspecies = sorted_species.end();
	    //curspecies--
		//while((curspecies!=sorted_species.begin())&&
		//	((*curspecies)->age<20))
		//	--curspecies;
	    int i = 0;
	    if(sorted_species.size() > 3)
	    	i = sorted_species.size()-2;
	    else i = sorted_species.size()-1;  	
	    
	    for( ; sorted_species.get(i).age < 20 && i>0; i--)
	    	curspecies = sorted_species.get(i);
	 
		//if ((generation%30)==0)
		//	(*curspecies)->obliterate=true;
	    
	    if ((generation%30)==0)
	    	curspecies.obliterate = true;
	    
	    logger.info("Number of Species: " + num_species);
	    //logger.info("compat_tresh: " + compat_threshold);
		//std::cout<<"Number of Species: "<<num_species<<std::endl;
		//std::cout<<"compat_thresh: "<<compat_threshold<<std::endl;

		//Use Species' ages to modify the objective fitness of organisms
		// in other words, make it more fair for younger species
		// so they have a chance to take hold
		//Also penalize stagnant species
		//Then adjust the fitness using the species size to "share" fitness
		//within a species.
		//Then, within each Species, mark for death 
		//those below survival_thresh*average
		/*for(curspecies=species.begin();curspecies!=species.end();++curspecies) {
			(*curspecies)->adjust_fitness();
		}*/
		for(i = 0; i < species.size(); i++){
			species.get(i).adjust_fitness();
		}

		//Go through the organisms and add up their fitnesses to compute the
		//overall average
		/*for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
			total+=(*curorg)->fitness;
		}*/
		
		for(i = 0; i < organisms.size(); i++){
			curorg = organisms.elementAt(i);
			total += curorg.fitness;
		}
		
		overall_average=total/total_organisms;
		logger.info("Generation: " + generation + " overall_average = " + overall_average);
		//std::cout<<"Generation "<<generation<<": "<<"overall_average = "<<overall_average<<std::endl;

		//Now compute expected number of offspring for each individual organism
		/*for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
			(*curorg)->expected_offspring=(((*curorg)->fitness)/overall_average);
		}*/
		for(i = 0; i < organisms.size(); i++)
			organisms.get(i).expected_offspring = organisms.get(i).fitness/overall_average;
		//Now add those offspring up within each Species to get the number of
		//offspring per Species
		skim = 0.0;
		total_expected = 0;
		/*for(curspecies=species.begin();curspecies!=species.end();++curspecies) {
			skim=(*curspecies)->count_offspring(skim);
			total_expected+=(*curspecies)->expected_offspring;
		}*/
		for(i = 0; i < species.size(); i++){
			skim = species.get(i).count_offspring(skim);
			total_expected += species.get(i).expected_offspring;
		}

		//Need to make up for lost foating point precision in offspring assignment
		//If we lost precision, give an extra baby to the best Species
		if (total_expected < total_organisms) {
			//Find the Species expecting the most
			max_expected = 0;
			final_expected = 0;
			/*for(curspecies=species.begin();curspecies!=species.end();++curspecies) {
				if ((*curspecies)->expected_offspring>=max_expected) {
					max_expected=(*curspecies)->expected_offspring;
					best_species=(*curspecies);
				}
				final_expected+=(*curspecies)->expected_offspring;
			}*/
			for(i = 0; i < species.size(); i++){
				if(species.get(i).expected_offspring >= max_expected){
					max_expected = species.get(i).expected_offspring;
					best_species.add(species.get(i));
				}
				final_expected += species.get(i).expected_offspring;
				logger.info("expected offspring : " + final_expected);
				logger.info("best species expected offspring : " + species.get(i).expected_offspring);
				species.get(i).expected_offspring++;
			}
			//Give the extra offspring to the best species
			//++(best_species->expected_offspring);
			//if(best_species.expected_offspring != 0)
			//best_species.expected_offspring++;
			
			final_expected++;

			//If we still arent at total, there is a problem
			//Note that this can happen if a stagnant Species
			//dominates the population and then gets killed off by its age
			//Then the whole population plummets in fitness
			//If the average fitness is allowed to hit 0, then we no longer have 
			//an average we can use to assign offspring.
			if (final_expected < total_organisms) {
				//      cout<<"Population died!"<<endl;
				//cin>>pause;
				/*for(curspecies=species.begin();curspecies!=species.end();++curspecies) {
					(*curspecies)->expected_offspring=0;
				}*/
				for(i = 0; i < species.size(); i++){
					//curspecies.expected_offspring = 0; //
					species.get(i).expected_offspring = 0;
					//best_species->expected_offspring=total_organisms;
					best_species.get(i).expected_offspring = total_organisms;
				}
				
			}
		}

		//Sort the Species by max fitness (Use an extra list to do this)
		//These need to use ORIGINAL fitness
		//sorted_species.qsort(order_species);
	    //std::sort(sorted_species.begin(), sorted_species.end(), order_species);
	    Collections.sort(sorted_species);

		best_species_num=sorted_species.get(0).id;

		/*for(curspecies=sorted_species.begin();curspecies!=sorted_species.end();++curspecies) {

			//Print out for Debugging/viewing what's going on 
			std::cout<<"orig fitness of Species"<<(*curspecies)->id<<"(Size "<<(*curspecies)->organisms.size()<<"): "<<(*((*curspecies)->organisms).begin())->orig_fitness<<" last improved "<<((*curspecies)->age-(*curspecies)->age_of_last_improvement)<<std::endl;
		}*/
		for(i = 0; i < sorted_species.size(); i++){
			logger.info("orid fitness of Species :" + sorted_species.get(i).id + "(Size: " + sorted_species.get(i).organisms.size() + "): " + sorted_species.get(i).organisms.get(0).orig_fitness + "last improved: " + sorted_species.get(i).age_of_last_improvement);
		}
		//Check for Population-level stagnation
		//curspecies=sorted_species.begin();
		curspecies = sorted_species.get(0);
		//(*(((*curspecies)->organisms).begin()))->pop_champ=true; //DEBUG marker of the best of pop
		curspecies.organisms.get(0).pop_champ = true;
		/*if (((*(((*curspecies)->organisms).begin()))->orig_fitness)>
			highest_fitness) {
				highest_fitness=((*(((*curspecies)->organisms).begin()))->orig_fitness);
				highest_last_changed=0;
				std::cout<<"NEW POPULATION RECORD FITNESS: "<<highest_fitness<<std::endl;
			}
		else {
			++highest_last_changed;
			std::cout<<highest_last_changed<<" generations since last population fitness record: "<<highest_fitness<<std::endl;
		}*/
		
		if(curspecies.organisms.get(0).orig_fitness > highest_fitness){
			highest_fitness = curspecies.organisms.get(0).orig_fitness;
			highest_last_changed = 0;
			logger.info("NEW POPULATION RECORD FITNESS: " + highest_fitness);
		}
		else{
			highest_last_changed++;
			logger.info(highest_last_changed + " generations since last population fitness record: " + highest_fitness);
		}


		//Check for stagnation- if there is stagnation, perform delta-coding
		if (highest_last_changed >= Neat.dropoff_age + 5) {

			//    cout<<"PERFORMING DELTA CODING"<<endl;

			highest_last_changed = 0;

			half_pop = Neat.pop_size / 2;

			//    cout<<"half_pop"<<half_pop<<" pop_size-halfpop: "<<pop_size-half_pop<<endl;

			//curspecies=sorted_species.begin();
			curspecies = sorted_species.get(0);

			//(*(((*curspecies)->organisms).begin()))->super_champ_offspring=half_pop;
			//(*curspecies)->expected_offspring=half_pop;
			//(*curspecies)->age_of_last_improvement=(*curspecies)->age;
			curspecies.organisms.get(0).super_champ_offspring = half_pop;
			curspecies.expected_offspring = half_pop;
			curspecies.age_of_last_improvement = curspecies.age;
			
			//curpcecies++;
			curspecies = sorted_species.get(0);

			if(curspeciesi != species.size()){ //if (curspecies!=sorted_species.end()) {

				//(*(((*curspecies)->organisms).begin()))->super_champ_offspring=NEAT::pop_size-half_pop;
				//(*curspecies)->expected_offspring=NEAT::pop_size-half_pop;
				//(*curspecies)->age_of_last_improvement=(*curspecies)->age;
				curspecies.organisms.get(0).super_champ_offspring = Neat.pop_size - half_pop;
				curspecies.expected_offspring = Neat.pop_size - half_pop;
				curspecies.age_of_last_improvement = curspecies.age;

				//curspecies++;
				curspecies = sorted_species.get(1);
				
				//Get rid of all species under the first 2
				/*while(curspecies!=sorted_species.end()) {
					(*curspecies)->expected_offspring=0;
					++curspecies;
				}*/
				while(curspeciesi != species.size()){
					curspecies.expected_offspring = 0;
					//curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}
				curspeciesi++;
			}
			else {
				//curspecies=sorted_species.begin();
				//(*(((*curspecies)->organisms).begin()))->super_champ_offspring+=NEAT::pop_size-half_pop;
				//(*curspecies)->expected_offspring=NEAT::pop_size-half_pop;
				curspecies = sorted_species.get(0);
				curspecies.organisms.get(0).super_champ_offspring += Neat.pop_size - half_pop;
			}

		}
		//STOLEN BABIES:  The system can take expected offspring away from
		//  worse species and give them to superior species depending on
		//  the system parameter babies_stolen (when babies_stolen > 0)
		else if (Neat.babies_stolen > 0) {
			//Take away a constant number of expected offspring from the worst few species

			stolen_babies=0;
			//curspecies=sorted_species.end();
			curspecies = sorted_species.lastElement();
			//curspecies--;
			curspecies = sorted_species.get(sorted_species.size()-2);
			while ((stolen_babies < NUM_STOLEN)&&
				curspecies != sorted_species.get(0)) {   //(curspecies!=sorted_species.begin())) {

					//cout<<"Considering Species "<<(*curspecies)->id<<": age "<<(((*curspecies)->age))<<" expected offspring "<<(((*curspecies)->expected_offspring))<<endl;

					/*if ((((*curspecies)->age)>5)&&
						(((*curspecies)->expected_offspring)>2)) {
							//cout<<"STEALING!"<<endl;

							//This species has enough to finish off the stolen pool
							if (((*curspecies)->expected_offspring-1)>=(NUM_STOLEN-stolen_babies)) {
								(*curspecies)->expected_offspring-=(NUM_STOLEN-stolen_babies);
								stolen_babies=NUM_STOLEN;
							}
							//Not enough here to complete the pool of stolen
							else {
								stolen_babies+=(*curspecies)->expected_offspring-1;
								(*curspecies)->expected_offspring=1;

							}
						}*/
					if(curspecies.age > 5 && curspecies.expected_offspring > 2){
						//This species has enough to finish off the stolen pool
						if(curspecies.expected_offspring-1 >= NUM_STOLEN-stolen_babies){
							curspecies.expected_offspring -= (NUM_STOLEN-stolen_babies);
							stolen_babies = NUM_STOLEN;
						}
						//Not enough here to complete the pool of stolen
						else stolen_babies += curspecies.expected_offspring - 1;
						curspecies.expected_offspring = 1;
					}
						curspecies = sorted_species.get(sorted_species.size()-2);
						//curspecies--;

						//if (stolen_babies>0)
						//cout<<"stolen babies so far: "<<stolen_babies<<endl;
				}

				//cout<<"STOLEN BABIES: "<<stolen_babies<<endl;

				//Mark the best champions of the top species to be the super champs
				//who will take on the extra offspring for cloning or mutant cloning
				//curspecies=sorted_species.begin();
				curspecies = sorted_species.get(0);

				//Determine the exact number that will be given to the top three
				//They get , in order, 1/5 1/5 and 1/10 of the stolen babies
				one_fifth_stolen = Neat.babies_stolen / 5;
				one_tenth_stolen = Neat.babies_stolen / 10;

				//Don't give to dying species even if they are champs
				//while((curspecies!=sorted_species.end())&&((*curspecies)->last_improved()>NEAT::dropoff_age))
				//	++curspecies;
				
				while(curspeciesi != species.size() && curspecies.last_improved() > Neat.dropoff_age){
					//curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}

				//Concentrate A LOT on the number one species
				/*if ((stolen_babies>=one_fifth_stolen)&&(curspecies!=sorted_species.end())) {
					(*(((*curspecies)->organisms).begin()))->super_champ_offspring=one_fifth_stolen;
					(*curspecies)->expected_offspring+=one_fifth_stolen;
					stolen_babies-=one_fifth_stolen;
					//cout<<"Gave "<<one_fifth_stolen<<" babies to Species "<<(*curspecies)->id<<endl;
					//      cout<<"The best superchamp is "<<(*(((*curspecies)->organisms).begin()))->gnome->genome_id<<endl;

					//Print this champ to file "champ" for observation if desired
					//IMPORTANT:  This causes generational file output 
					//print_Genome_tofile((*(((*curspecies)->organisms).begin()))->gnome,"champ");

					curspecies++;
				}*/
				
				if(stolen_babies >= one_fifth_stolen && curspeciesi != species.size()){
					curspecies.organisms.get(0).super_champ_offspring = one_fifth_stolen;
					curspecies.expected_offspring += one_fifth_stolen;
					stolen_babies -= one_fifth_stolen;
					
					//curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}

				//Don't give to dying species even if they are champs
				//while((curspecies!=sorted_species.end())&&((*curspecies)->last_improved()>NEAT::dropoff_age))
				//	++curspecies;
				
				while(curspeciesi != species.size() && curspecies.last_improved() > Neat.dropoff_age){
					//curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}

				/*if ((curspecies!=sorted_species.end())) {
					if (stolen_babies>=one_fifth_stolen) {
						(*(((*curspecies)->organisms).begin()))->super_champ_offspring=one_fifth_stolen;
						(*curspecies)->expected_offspring+=one_fifth_stolen;
						stolen_babies-=one_fifth_stolen;
						//cout<<"Gave "<<one_fifth_stolen<<" babies to Species "<<(*curspecies)->id<<endl;
						curspecies++;

					}
				}*/
				if(curspeciesi != species.size()){
					if(stolen_babies >= one_fifth_stolen){
						curspecies.organisms.get(0).super_champ_offspring = one_fifth_stolen;
						curspecies.expected_offspring += one_fifth_stolen;
						stolen_babies -= one_fifth_stolen;
						//curspecies++;
						curspecies = sorted_species.get(1);
						curspeciesi++;
					}
				}

				//Don't give to dying species even if they are champs
				//while((curspecies!=sorted_species.end())&&((*curspecies)->last_improved()>NEAT::dropoff_age))
				//	++curspecies;
				
				while(curspeciesi != species.size() && curspecies.last_improved() > Neat.dropoff_age){
					//curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}

				/*if (curspecies!=sorted_species.end())
					if (stolen_babies>=one_tenth_stolen) {
						(*(((*curspecies)->organisms).begin()))->super_champ_offspring=one_tenth_stolen;
						(*curspecies)->expected_offspring+=one_tenth_stolen;
						stolen_babies-=one_tenth_stolen;

						//cout<<"Gave "<<one_tenth_stolen<<" babies to Species "<<(*curspecies)->id<<endl;
						curspecies++;

					}*/
				
				if(curspeciesi != species.size()){
					if(stolen_babies >= one_tenth_stolen){
						curspecies.organisms.get(0).super_champ_offspring = one_tenth_stolen;
						curspecies.expected_offspring += one_tenth_stolen;
						stolen_babies -= one_tenth_stolen;
						//curspecies++;
						curspecies = sorted_species.get(1);
						curspeciesi++;
					}
				}

				//Don't give to dying species even if they are champs
				//while((curspecies!=sorted_species.end())&&((*curspecies)->last_improved()>NEAT::dropoff_age))
				//	++curspecies;
				while(curspeciesi != species.size() && curspecies.last_improved() > Neat.dropoff_age){
					///curspecies++;
					curspecies = sorted_species.get(1);
					curspeciesi++;
				}

				while((stolen_babies > 0)&&
					(curspecies != sorted_species.lastElement())) {
						//Randomize a little which species get boosted by a super champ

						if (Math.random() > 0.1)
							if (stolen_babies > 3) {
								//(*(((*curspecies)->organisms).begin()))->super_champ_offspring=3;
								//(*curspecies)->expected_offspring+=3;
								//stolen_babies-=3;
								//cout<<"Gave 3 babies to Species "<<(*curspecies)->id<<endl;
								curspecies.organisms.get(0).super_champ_offspring = 3;
								curspecies.expected_offspring += 3;
								stolen_babies -= 3;
							}
							else {
								//cout<<"3 or less babies available"<<endl;
								//(*(((*curspecies)->organisms).begin()))->super_champ_offspring=stolen_babies;
								//(*curspecies)->expected_offspring+=stolen_babies;
								//cout<<"Gave "<<stolen_babies<<" babies to Species "<<(*curspecies)->id<<endl;
								curspecies.organisms.get(0).super_champ_offspring = stolen_babies;
								curspecies.expected_offspring += stolen_babies;
								stolen_babies = 0;
							}

							//curspecies++;
							curspecies = sorted_species.get(1);

							//Don't give to dying species even if they are champs
							//while((curspecies!=sorted_species.end())&&((*curspecies)->last_improved()>NEAT::dropoff_age))
							//	++curspecies;
							while(curspeciesi != species.size() && curspecies.last_improved() > Neat.dropoff_age){
								//curspecies++;
								curspecies = sorted_species.get(1);
								curspeciesi++;
							}

					}

					//cout<<"Done giving back babies"<<endl;

					//If any stolen babies aren't taken, give them to species #1's champ
					if (stolen_babies > 0) {

						//cout<<"Not all given back, giving to best Species"<<endl;

						/*curspecies=sorted_species.begin();
						(*(((*curspecies)->organisms).begin()))->super_champ_offspring+=stolen_babies;
						(*curspecies)->expected_offspring+=stolen_babies;
						stolen_babies=0;*/
						
						curspecies = sorted_species.get(0);
						curspecies.organisms.get(0).super_champ_offspring += stolen_babies;
						curspecies.expected_offspring += stolen_babies;
						stolen_babies = 0;
					}
		}


		//Kill off all Organisms marked for death.  The remainder
		//will be allowed to reproduce.
		//curorg=organisms.begin();
		curorg = organisms.get(0);
		/*while(curorg!=organisms.end()) {
			if (((*curorg)->eliminate)) {
				//Remove the organism from its Species
				((*curorg)->species)->remove_org(*curorg);

				//Delete the organism from memory
				delete (*curorg);

				//Remember where we are
				deadorg=curorg;
				++curorg;

				//iter2 =  v.erase(iter); 

				//Remove the organism from the master list
				curorg=organisms.erase(deadorg);

			}
			else {
				++curorg;
			}
		}*/
		

		//cout<<"Reproducing"<<endl;

		//Perform reproduction.  Reproduction is done on a per-Species
		//basis.  (So this could be paralellized potentially.)
		//	for(curspecies=species.begin();curspecies!=species.end();++curspecies) {

		//KENHACK                                                                      
		//		for(std::vector<Species*>::iterator curspecies2=species.begin();curspecies2!=species.end();++curspecies2) {
		//		  std::cout<<"PRE in repro specloop SPEC EXISTING number "<<(*curspecies2)->id<<std::endl;
		//	}

		//	(*curspecies)->reproduce(generation,this,sorted_species);


		//}    


		//curspecies=species.begin();
		//int last_id=(*curspecies)->id;
		
		curspecies = species.get(0);
		int last_id = curspecies.id;
		int curorgi = 0;
		//while(curspecies!=species.end()) {
		//  (*curspecies)->reproduce(generation,this,sorted_species);
		
		while(curorgi != organisms.size()){
			curspecies.reproduce(generation, this, sorted_species);

		  //Set the current species to the id of the last species checked
		  //(the iterator must be reset because there were possibly vector insertions during reproduce)
		  //std::vector<Species*>::iterator curspecies2=species.begin();
		  Species curspecies2 = species.get(0);
		  
		  /*while(curspecies2!=species.end()) {
		    if (((*curspecies2)->id)==last_id)
		      curspecies=curspecies2;
		    curspecies2++;
		  }*/
		  
		  while(curorgi != organisms.size()){
			  if(curspecies2.id == last_id)
				  curspecies = curspecies2;
			  //curspecies2++;
			  curspecies2 = sorted_species.get(0);
			  curorgi++;
		  }

		  //Move to the next on the list
		  //curspecies++;
		  curspecies = sorted_species.get(0);
		  
		  //Record where we are
		  //if (curspecies!=species.end())
		  //  last_id=(*curspecies)->id;
		  if(curspecies != species.lastElement())
			  last_id = curspecies.id;
		}

		//cout<<"Reproduction Complete"<<endl;


		//Destroy and remove the old generation from the organisms and species  
		//curorg=organisms.begin();
		curorg = organisms.get(0);
		/*while(curorg!=organisms.end()) {

		  //Remove the organism from its Species
		  ((*curorg)->species)->remove_org(*curorg);

		  //std::cout<<"deleting org # "<<(*curorg)->gnome->genome_id<<std::endl;

		  //Delete the organism from memory
		  delete (*curorg);
		  
		  //Remember where we are
		  deadorg = curorg;
		  ++curorg;
		  
		  //std::cout<<"next org #  "<<(*curorg)->gnome->genome_id<<std::endl;

		  //Remove the organism from the master list
		  curorg=organisms.erase(deadorg);

		  //std::cout<<"nnext org # "<<(*curorg)->gnome->genome_id<<std::endl;
		}*/
		while(curorgi != organisms.size()){
			curorg.species.remove_org(curorg);
			
			//delete(curorg);
			
			deadorg = curorg;
			//curorg++;
			curorg = organisms.get(1);;
			//curorg = organisms.erase(deadorg);
			curorgi++;
		}

		//Remove all empty Species and age ones that survive
		//As this happens, create master organism list for the new generation
		//curspecies=species.begin();
		curspecies = species.get(0);
		orgcount = 0;
		while(curorgi != organisms.size()) {
			/*if (((*curspecies)->organisms.size())==0) {
				delete (*curspecies);

				deadspecies=curspecies;
				++curspecies;

				curspecies=species.erase(deadspecies);
			}*/
			if(curspecies.organisms.size() == 0){
				//delete(curspecies);
				deadspecies = curspecies;
				//curspecies++;
				curspecies = sorted_species.get(1);
				//curspecies = species.erase(deadspecies);
			}
			//Age surviving Species and 
			//Rebuild master Organism list: NUMBER THEM as they are added to the list
			else {
				//Age any Species that is not newly created in this generation
				/*if ((*curspecies)->novel) {
					(*curspecies)->novel=false;
				}*/
				if(curspecies.novel){
					curspecies.novel = false;
				}
				//else ++((*curspecies)->age);
				else curspecies.age++;

				//Go through the organisms of the curspecies and add them to 
				//the master list
				/*for(curorg=((*curspecies)->organisms).begin();curorg!=((*curspecies)->organisms).end();++curorg) {
					((*curorg)->gnome)->genome_id=orgcount++;
					organisms.push_back(*curorg);
				}*/
				for(i = 0; i < organisms.size(); i++){
					curorg.gnome.genome_id = i;
					//organisms.push_back(curorg);
					organisms.add(curorg);
				}
				//++curspecies;
				curspecies = sorted_species.get(1);

			}
			curorgi++;
		}      

		//Remove the innovations of the current generation
		//curinnov=innovations.begin();
//		curinnov = innovation.get(0);
		/*while(curinnov!=innovations.end()) {
			delete (*curinnov);

			deadinnov=curinnov;
			++curinnov;

			curinnov=innovations.erase(deadinnov);
		}*/
/*		while(curinnov != innovation.lastElement()){
			//delete (curinnov);
			deadinnov = curinnov;
			//curinnov ++;
			curspecies = sorted_species.get(1);
			//curinnov = innovation.erase(deadinnov);
		}
*/
		//DEBUG: Check to see if the best species died somehow
		// We don't want this to happen
		//curspecies=species.begin();
		//curspecies = species.get(0);
		
		best_ok = false;
		/*while(curspecies!=species.end()) {
			if (((*curspecies)->id)==best_species_num) best_ok=true;
			++curspecies;
		}*/
		while(curspeciesi != species.size()){
			if(curspecies.id == best_species_num)
				best_ok = true;
			curspeciesi++;
			curspecies = sorted_species.get(0);
		}
		if (!best_ok) {
			//cout<<"ERROR: THE BEST SPECIES DIED!"<<endl;
		}
		else {
			//cout<<"The best survived: "<<best_species_num<<endl;
		}

		//DEBUG: Checking the top organism's duplicate in the next gen
		//This prints the champ's child to the screen
		/*for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
			if ((*curorg)->pop_champ_child) {
				//cout<<"At end of reproduction cycle, the child of the pop champ is: "<<(*curorg)->gnome<<endl;
			}
		}*/
		for(i = 0; i < organisms.size(); i++){
			if(curorg.pop_champ_child){
				logger.info("At end of reproduction cycle, the child of the pop champ is: " + curorg.gnome);
			}
		}

		//cout<<"babies_stolen at end: "<<babies_stolen<<endl;

		//cout<<"Epoch complete"<<endl; 

		return true;

	}

	//
	// // Print Population to a file specified by a string
	// bool print_to_file(std::ostream& outFile);
	//
	// // Print Population to a file in speciated order with comments separating
	// each species
	// bool print_to_file_by_species(std::ostream& outFile);
	// bool print_to_file_by_species(char *filename);
	//
	// // Prints the champions of each species to files starting with
	// directory_prefix
	// // The file name are as follows: [prefix]g[generation_num]cs[species_num]
	// // Thus, they can be indexed by generation or species
	// bool print_species_champs_tofiles(char *directory_prefix,int generation);
	//
	// // Run verify on all Genomes in this Population (Debugging)
	// bool verify();
	//
	// // Turnover the population to a new generation using fitness
	// // The generation argument is the next generation
	// bool epoch(int generation);
	//
	// // *** Real-time methods ***
	//
	// // Places the organisms in species in order from best to worst fitness
	// bool rank_within_species();
	//
	// // Estimates average fitness for all existing species
	// void estimate_all_averages();
	//
	// //Reproduce only out of the pop champ
	// Organism* reproduce_champ(int generation);
	//
	// // Probabilistically choose a species to reproduce
	// // Note that this method is effectively real-time fitness sharing in that
	// the
	// // species will tend to produce offspring in an amount proportional
	// // to their average fitness, which approximates the generational
	// // method of producing the next generation of the species en masse
	// // based on its average (shared) fitness.
	// Species *choose_parent_species();
	//
	// //Remove a species from the species list (sometimes called by
	// remove_worst when a species becomes empty)
	// bool remove_species(Species *spec);
	//
	// // Removes worst member of population that has been around for a minimum
	// amount of time and returns
	// // a pointer to the Organism that was removed (note that the pointer will
	// not point to anything at all,
	// // since the Organism it was pointing to has been deleted from memory)
	// Organism* remove_worst();
	//
	// //Warning: rtNEAT does not behave like regular NEAT if you remove the
	// worst probabilistically
	// //You really should just use "remove_worst," which removes the org with
	// worst adjusted fitness.
	// Organism* remove_worst_probabilistic();
	//
	// //KEN: New 2/17/04
	// //This method takes an Organism and reassigns what Species it belongs to
	// //It is meant to be used so that we can reasses where Organisms should
	// belong
	// //as the speciation threshold changes.
	// void reassign_species(Organism *org);
	//
	// //Move an Organism from one Species to another (called by
	// reassign_species)
	// void switch_species(Organism *org, Species *orig_species, Species
	// *new_species);
	//
	// // Construct off of a single spawning Genome
	// Population(Genome *g,int size);
	//
	// // Construct off of a single spawning Genome without mutation
	// Population(Genome *g,int size, float power);
	//
	// //MSC Addition
	// // Construct off of a vector of genomes with a mutation rate of "power"
	// Population(std::vector<Genome*> genomeList, float power);
	//
	// bool clone(Genome *g,int size, float power);
	//
	// //// Special constructor to create a population of random topologies
	// //// uses Genome(int i, int o, int n,int nmax, bool r, double linkprob)
	// //// See the Genome constructor for the argument specifications
	// //Population(int size,int i,int o, int nmax, bool r, double linkprob);
	//
	// // Construct off of a file of Genomes
	// Population(const char *filename);
	//
	// // It can delete a Population in two ways:
	// // -delete by killing off the species
	// // -delete by killing off the organisms themselves (if not speciated)
	// // It does the latter if it sees the species list is empty
	// ~Population();
	//
	//
	//
	// };
	//
	// } // namespace NEAT

} // end class

//
// #endif

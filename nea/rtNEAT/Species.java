package nea.rtNEAT;

import java.util.Collections;
import java.util.Vector;

//#ifndef _SPECIES_H_
//#define _SPECIES_H_
//
//#include "neat.h"
//#include "organism.h"
//#include "population.h"
//
//namespace NEAT {
//
//	class Organism;
//	class Population;
//
//	// ---------------------------------------------  
//	// SPECIES CLASS:
//	//   A Species is a group of similar Organisms      
//	//   Reproduction takes place mostly within a
//	//   single species, so that compatible organisms
//	//   can mate.                                      
//	// ---------------------------------------------  
//	class Species {
public class Species implements Comparable<Species>{
	//
	// public:
	//
	// int id;
	public int id;
	// int age; //The age of the Species
	public int age;
	// double ave_fitness; //The average fitness of the Species
	public double ave_fitness;
	// double max_fitness; //Max fitness of the Species
	public double max_fitness;
	// double max_fitness_ever; //The max it ever had
	public double max_fitness_ever;
	// int expected_offspring;
	public int expected_offspring;
	// bool novel;
	public boolean novel;
	// bool checked;
	public boolean checked;
	// bool obliterate; //Allows killing off in competitive coevolution
	// stagnation
	public boolean obliterate;
	// std::vector<Organism*> organisms; //The organisms in the Species
	public Vector<Organism> organisms = new Vector<Organism>();
	// //std::vector<Organism*> reproduction_pool; //The organisms for
	// reproduction- NOT NEEDED
	// int age_of_last_improvement; //If this is too long ago, the Species will
	// goes extinct
	public int age_of_last_improvement;
	// double average_est; //When playing real-time allows estimating average
	// fitness
	public double average_est;

	public Organism curorg;

	//
	// bool add_Organism(Organism *o);

	//
	// Organism *first();
	//
	// bool print_to_file(std::ostream &outFile);
	// bool print_to_file(std::ofstream &outFile);
	//
	// //Change the fitness of all the organisms in the species to possibly
	// depend slightly on the age of the species
	// //and then divide it by the size of the species so that the organisms in
	// the species "share" the fitness
	// void adjust_fitness();

	public void adjust_fitness() {
		// std::vector<Organism*>::iterator curorg;

		int num_parents;
		int count;

		int age_debt;

		// std::cout<<"Species "<<id<<" last improved "<<(age-age_of_last_improvement)<<" steps ago when it moved up to "<<max_fitness_ever<<std::endl;

		age_debt = (age - age_of_last_improvement + 1) - Neat.dropoff_age;

		if (age_debt == 0)
			age_debt = 1;

		// for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
		for (Organism curorg : organisms) {

			// Remember the original fitness before it gets modified
			(curorg).orig_fitness = (curorg).fitness;

			// Make fitness decrease after a stagnation point dropoff_age
			// Added an if to keep species pristine until the dropoff point
			// obliterate is used in competitive coevolution to mark stagnation
			// by obliterating the worst species over a certain age
			if ((age_debt >= 1) || obliterate) {

				// Possible graded dropoff
				// ((*curorg)->fitness)=((*curorg)->fitness)*(-atan(age_debt));

				// Extreme penalty for a long period of stagnation (divide
				// fitness by 100)
				((curorg).fitness) = ((curorg).fitness) * 0.01;
				// std::cout<<"OBLITERATE Species "<<id<<" of age "<<age<<std::endl;
				// std::cout<<"dropped fitness to "<<((*curorg)->fitness)<<std::endl;
			}

			// Give a fitness boost up to some young age (niching)
			// The age_significance parameter is a system parameter
			// if it is 1, then young species get no fitness boost
			if (age <= 10)
				((curorg).fitness) = ((curorg).fitness) * Neat.age_significance;

			// Do not allow negative fitness
			if (((curorg).fitness) < 0.0)
				(curorg).fitness = 0.0001;

			// Share fitness with the species
			(curorg).fitness = ((curorg).fitness) / (organisms.size());

		}

		// Sort the population and mark for death those after
		// survival_thresh*pop_size
		// organisms.qsort(order_orgs);
		Collections.sort(organisms);

		// Update age_of_last_improvement here
		if ((((organisms.firstElement())).orig_fitness) > max_fitness_ever) {
			age_of_last_improvement = age;
			max_fitness_ever = (((organisms.firstElement())).orig_fitness);
		}

		// Decide how many get to reproduce based on survival_thresh*pop_size
		// Adding 1.0 ensures that at least one will survive
		num_parents = (int) Math
				.floor((Neat.survival_thresh * ((double) organisms.size())) + 1.0);

		// Mark for death those who are ranked too low to be parents
		// curorg = organisms.begin();

		curorg = organisms.get(0);
		curorg.champion = true;
		for (int i = 1; i <= num_parents; i++) {
			if (curorg != organisms.lastElement()) {
				curorg = i < organisms.size() ? organisms.get(i) : null;
			}
		}
		/*
		 * for (Organism curorg : organisms){ curorg.champion = true;
		 * //(*curorg)->champion=true; //Mark the champ as such
		 * for(count=1;count<=num_parents;count++) { if
		 * (curorg!=organisms.lastElement()) curorg++; } }
		 */
		while (curorg != organisms.lastElement()) {
			curorg.eliminate = true; // (*curorg)->eliminate=true; //Mark for
										// elimination
			// std::std::cout<<"marked org # "<<(*curorg)->gnome->genome_id<<" fitness = "<<(*curorg)->fitness<<std::std::endl;
			for (int i = 0; i < organisms.size(); i++) {
				if (curorg == organisms.get(i)) {
					curorg = organisms.get(i + 1);
					break;
				}
			}
		}

	}

	//
	// double compute_average_fitness();
	//
	// double compute_max_fitness();
	//
	// //Counts the number of offspring expected from all its members skim is
	// for keeping track of remaining
	// // fractional parts of offspring and distributing them among species
	// double count_offspring(double skim);
	//
	// //Compute generations since last improvement
	// int last_improved() {
	// return age-age_of_last_improvement;
	// }
	//
	// //Remove an organism from Species
	// bool remove_org(Organism *org);
	//
	// double size() {
	// return organisms.size();
	// }
	//
	// Organism *get_champ();
	//
	// //Perform mating and mutation to form next generation
	// bool reproduce(int generation, Population *pop,std::vector<Species*>
	// &sorted_species);
	//
	// // *** Real-time methods ***
	//
	// //Place organisms in this species in order by their fitness
	// bool rank();
	public boolean rank() {
		// organisms.qsort(order_orgs);
		// std::sort(organisms.begin(), organisms.end(), order_orgs);
		Collections.sort(organisms);
		return true;
	}

	//
	// //Compute an estimate of the average fitness of the species
	// //The result is left in variable average_est and returned
	// //New variable: average_est, NEAT::time_alive_minimum (const)
	// //Note: Initialization requires calling estimate_average() on all species
	// // Later it should be called only when a species changes
	// double estimate_average();
	public double estimate_average() {
		// std::vector<Organism*>::iterator curorg;
		double total = 0.0; // running total of fitnesses

		// Note: Since evolution is happening in real-time, some organisms may
		// not
		// have been around long enough to count them in the fitness evaluation

		double num_orgs = 0; // counts number of orgs above the time_alive
								// threshold

		// for(curorg = organisms.begin(); curorg != organisms.end(); ++curorg)
		// {
		for (Organism curorg : organisms) {
			// New variable time_alive
			if (((curorg).time_alive) >= Neat.time_alive_minimum) {
				total += (curorg).fitness;
				++num_orgs;
			}
		}

		if (num_orgs > 0)
			average_est = total / num_orgs;
		else {
			average_est = 0;
		}

		return average_est;
	}
	
	public boolean add_Organism(Organism o){
		organisms.add(o);
		return true;
	}
	
	public Organism first() {
		return organisms.elementAt(0);
	}
	
	public double count_offspring(double skim) {
		//std::vector<Organism*>::iterator curorg;
		Organism curorg;
		int e_o_intpart;  //The floor of an organism's expected offspring
		double e_o_fracpart; //Expected offspring fractional part
		double skim_intpart;  //The whole offspring in the skim

		expected_offspring=0;

		/*for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
			e_o_intpart=(int) floor((*curorg)->expected_offspring);
			e_o_fracpart=fmod((*curorg)->expected_offspring,1.0);

			expected_offspring+=e_o_intpart;

			//Skim off the fractional offspring
			skim+=e_o_fracpart;

			//NOTE:  Some precision is lost by computer
			//       Must be remedied later
			if (skim>1.0) {
				skim_intpart=floor(skim);
				expected_offspring+=(int) skim_intpart;
				skim-=skim_intpart;
			}
		}*/
		
		for(int i = 0; i < organisms.size(); i++){
			e_o_intpart = (int) Math.floor(organisms.get(i).expected_offspring);
			e_o_fracpart = Math.floorMod((long) organisms.get(i).expected_offspring, (long) 1.0);
			
			expected_offspring+=e_o_intpart;
			
			//Skim off the fractional offspring
			skim+=e_o_fracpart;
			
			if (skim>1.0) {
				skim_intpart=Math.floor(skim);
				expected_offspring+=(int) skim_intpart;
				skim-=skim_intpart;
			}
		}

		return skim;

	}

	//
	// //Like the usual reproduce() method except only one offspring is produced
	// //Note that "generation" will be used to just count which offspring #
	// this is over all evolution
	// //Here is how to get sorted species:
	// // Sort the Species by max fitness (Use an extra list to do this)
	// // These need to use ORIGINAL fitness
	// // sorted_species.sort(order_species);
	// Organism *reproduce_one(int generation, Population
	// *pop,std::vector<Species*> &sorted_species);
	// // Organism *reproduce_one(int generation, Population
	// *pop,Vector<Species*> &sorted_species, bool addAdv, Genome* adv);
	//
	// Species(int i);
	public Species(int i) {
		id = i;
		age = 1;
		ave_fitness = 0.0;
		expected_offspring = 0;
		novel = false;
		age_of_last_improvement = 0;
		max_fitness = 0;
		max_fitness_ever = 0;
		obliterate = false;

		average_est = 0;
	}

	//
	// //Allows the creation of a Species that won't age (a novel one)
	// //This protects new Species from aging inside their first generation
	// Species(int i,bool n);
	public Species(int i, boolean n) {
		id = i;
		age = 1;
		ave_fitness = 0.0;
		expected_offspring = 0;
		novel = n;
		age_of_last_improvement = 0;
		max_fitness = 0;
		max_fitness_ever = 0;
		obliterate = false;

		average_est = 0;
	}
	
	//Remove an organism from Species
	public boolean remove_org(Organism org) {
		return true;
	}
	
	//Compute generations since last improvement
	int last_improved() {
		return age - age_of_last_improvement;
	}
	
//	public boolean reproduce(int generation, Population pop, Vector<Species> sorted_species) {
//		int count;
//		Vector<Organism> curorg = new Vector<Organism>();
//
//		int poolsize;  //The number of Organisms in the old generation
//
//		int orgnum;  //Random variable
//		int orgcount;
//		//Organism *mom; //Parent Organisms
//		//Organism *dad;
//		//Organism *baby;  //The new Organism
//		Organism mom;
//		Organism dad;
//		Organism baby;
//		
//		//Genome *new_genome;  //For holding baby's genes
//		Genome new_genome;
//
//		//std::vector<Species*>::iterator curspecies;  //For adding baby
//		Vector<Species> curspecies = new Vector<Species>();
//		//Species *newspecies; //For babies in new Species
//		Species newspecies;
//		//Organism *comporg;  //For Species determination through comparison
//		Organism comporg;
//
//		//Species *randspecies;  //For mating outside the Species
//		Species randspecies;
//		double randmult;
//		int randspeciesnum;
//		int spcount;  
//		//std::vector<Species*>::iterator cursp;
//		Vector<Species> cursp = new Vector<Species>();
//
//		//Network *net_analogue;  //For adding link to test for recurrency
//		Network net_analogue;
//		int pause;
//
//		boolean outside;
//
//		boolean found;  //When a Species is found
//
//		boolean champ_done = false; //Flag the preservation of the champion  
//
//		//Organism *thechamp;
//		Organism thechamp;
//
//		int giveup; //For giving up finding a mate outside the species
//
//		boolean mut_struct_baby;
//		boolean mate_baby;
//
//		//The weight mutation power is species specific depending on its age
//		double mut_power = Neat.weigh_mut_power;
//
//		//Roulette wheel variables
//		double total_fitness = 0.0;
//		double marble;  //The marble will have a number between 0 and total_fitness
//		double spin;  //0Fitness total while the wheel is spinning
//
//		//Compute total fitness of species for a roulette wheel
//		//Note: You don't get much advantage from a roulette here
//		// because the size of a species is relatively small.
//		// But you can use it by using the roulette code here
//		//for(curorg=organisms.begin();curorg!=organisms.end();++curorg) {
//		//  total_fitness+=(*curorg)->fitness;
//		//}
//
//		//Check for a mistake
//		/*if ((expected_offspring>0)&&
//			(organisms.size()==0)) {
//				//    std::cout<<"ERROR:  ATTEMPT TO REPRODUCE OUT OF EMPTY SPECIES"<<std::endl;
//				return false;
//			}*/
//		
//		if(expected_offspring > 0 && organisms.size() == 0){
//			return false;
//		}
//
//		poolsize = organisms.size()-1;
//
//		//thechamp=(*(organisms.begin()));
//		thechamp = organisms.get(0);
//
//		//Create the designated number of offspring for the Species
//		//one at a time
//		for (count = 0; count < expected_offspring; count++) {
//
//			mut_struct_baby = false;
//			mate_baby = false;
//
//			outside = false;
//
//			//Debug Trap
//			if (expected_offspring>Neat.pop_size) {
//				//      std::cout<<"ALERT: EXPECTED OFFSPRING = "<<expected_offspring<<std::endl;
//				//      cin>>pause;
//			}
//
//			//If we have a super_champ (Population champion), finish off some special clones
//			/*if ((thechamp->super_champ_offspring) > 0) {
//				mom=thechamp;
//				new_genome=(mom->gnome)->duplicate(count);*/
//			if(thechamp.super_champ_offspring > 0){
//				mom = thechamp;
//				new_genome = mom.gnome.duplicate(count);
//			}
//				//if ((thechamp->super_champ_offspring) == 1) {
//			else if(thechamp.super_champ_offspring == 1){
//					dad = thechamp;
//					new_genome = dad.gnome.duplicate(count);
//			}
//					//Most superchamp offspring will have their connection weights mutated only
//					//The last offspring will be an exact duplicate of this super_champ
//					//Note: Superchamp offspring only occur with stolen babies!
//					//      Settings used for published experiments did not use this
//			else if(thechamp.super_champ_offspring > 1){ // ((thechamp->super_champ_offspring) > 1) {
//				if ((randfloat()<0.8)||
//					(NEAT::mutate_add_link_prob==0.0)) 
//					//ABOVE LINE IS FOR:
//					//Make sure no links get added when the system has link adding disabled
//					new_genome->mutate_link_weights(mut_power,1.0,GAUSSIAN);
//				else {
//					//Sometimes we add a link to a superchamp
//					net_analogue=new_genome->genesis(generation);
//					new_genome->mutate_add_link(pop->innovations,pop->cur_innov_num,NEAT::newlink_tries);
//					delete net_analogue;
//					mut_struct_baby=true;
//				}
//			}
//
//			baby=new Organism(0.0,new_genome,generation);
//
///*					if ((thechamp->super_champ_offspring) == 1) {
//						if (thechamp->pop_champ) {
//							//std::cout<<"The new org baby's genome is "<<baby->gnome<<std::endl;
//							baby->pop_champ_child=true;
//							baby->high_fit=mom->orig_fitness;
//						}
//					}
//
//					thechamp->super_champ_offspring--;
//				}
//				//If we have a Species champion, just clone it 
//				else if ((!champ_done)&&
//					(expected_offspring>5)) {
//
//						mom=thechamp; //Mom is the champ
//
//						new_genome=(mom->gnome)->duplicate(count);
//
//						baby=new Organism(0.0,new_genome,generation);  //Baby is just like mommy
//
//						champ_done=true;
//
//					}
//					//First, decide whether to mate or mutate
//					//If there is only one organism in the pool, then always mutate
//				else if ((randfloat()<NEAT::mutate_only_prob)||
//					poolsize== 0) {
//
//						//Choose the random parent
//
//						//RANDOM PARENT CHOOSER
//						orgnum=randint(0,poolsize);
//						curorg=organisms.begin();
//						for(orgcount=0;orgcount<orgnum;orgcount++)
//							++curorg;                       
//
//
//
//						////Roulette Wheel
//						//marble=randfloat()*total_fitness;
//						//curorg=organisms.begin();
//						//spin=(*curorg)->fitness;
//						//while(spin<marble) {
//						//++curorg;
//
//						////Keep the wheel spinning
//						//spin+=(*curorg)->fitness;
//						//}
//						////Finished roulette
//						//
//
//						mom=(*curorg);
//
//						new_genome=(mom->gnome)->duplicate(count);
//
//						//Do the mutation depending on probabilities of 
//						//various mutations
//
//						if (randfloat()<NEAT::mutate_add_node_prob) {
//							//std::cout<<"mutate add node"<<std::endl;
//							new_genome->mutate_add_node(pop->innovations,pop->cur_node_id,pop->cur_innov_num);
//							mut_struct_baby=true;
//						}
//						else if (randfloat()<NEAT::mutate_add_link_prob) {
//							//std::cout<<"mutate add link"<<std::endl;
//							net_analogue=new_genome->genesis(generation);
//							new_genome->mutate_add_link(pop->innovations,pop->cur_innov_num,NEAT::newlink_tries);
//							delete net_analogue;
//							mut_struct_baby=true;
//						}
//						//NOTE:  A link CANNOT be added directly after a node was added because the phenotype
//						//       will not be appropriately altered to reflect the change
//						else {
//							//If we didn't do a structural mutation, we do the other kinds
//
//							if (randfloat()<NEAT::mutate_random_trait_prob) {
//								//std::cout<<"mutate random trait"<<std::endl;
//								new_genome->mutate_random_trait();
//							}
//							if (randfloat()<NEAT::mutate_link_trait_prob) {
//								//std::cout<<"mutate_link_trait"<<std::endl;
//								new_genome->mutate_link_trait(1);
//							}
//							if (randfloat()<NEAT::mutate_node_trait_prob) {
//								//std::cout<<"mutate_node_trait"<<std::endl;
//								new_genome->mutate_node_trait(1);
//							}
//							if (randfloat()<NEAT::mutate_link_weights_prob) {
//								//std::cout<<"mutate_link_weights"<<std::endl;
//								new_genome->mutate_link_weights(mut_power,1.0,GAUSSIAN);
//							}
//							if (randfloat()<NEAT::mutate_toggle_enable_prob) {
//								//std::cout<<"mutate toggle enable"<<std::endl;
//								new_genome->mutate_toggle_enable(1);
//
//							}
//							if (randfloat()<NEAT::mutate_gene_reenable_prob) {
//								//std::cout<<"mutate gene reenable"<<std::endl;
//								new_genome->mutate_gene_reenable();
//							}
//						}
//
//						baby=new Organism(0.0,new_genome,generation);
//
//					}
//
//					//Otherwise we should mate 
//				else {
//
//					//Choose the random mom
//					orgnum=randint(0,poolsize);
//					curorg=organisms.begin();
//					for(orgcount=0;orgcount<orgnum;orgcount++)
//						++curorg;
//
//
//					////Roulette Wheel
//					//marble=randfloat()*total_fitness;
//					//curorg=organisms.begin();
//					//spin=(*curorg)->fitness;
//					//while(spin<marble) {
//					//++curorg;
//
//					////Keep the wheel spinning
//					//spin+=(*curorg)->fitness;
//					//}
//					////Finished roulette
//					//
//
//					mom=(*curorg);         
//
//					//Choose random dad
//
//					if ((randfloat()>NEAT::interspecies_mate_rate)) {
//						//Mate within Species
//
//						orgnum=randint(0,poolsize);
//						curorg=organisms.begin();
//						for(orgcount=0;orgcount<orgnum;orgcount++)
//							++curorg;
//
//
//						////Use a roulette wheel
//						//marble=randfloat()*total_fitness;
//						//curorg=organisms.begin();
//						//spin=(*curorg)->fitness;
//						//while(spin<marble) {
//						//++curorg;
//						//}
//
//						////Keep the wheel spinning
//						//spin+=(*curorg)->fitness;
//						//}
//						////Finished roulette
//						//
//
//						dad=(*curorg);
//					}
//					else {
//
//						//Mate outside Species  
//						randspecies=this;
//
//						//Select a random species
//						giveup=0;  //Give up if you cant find a different Species
//						while((randspecies==this)&&(giveup<5)) {
//
//							//This old way just chose any old species
//							//randspeciesnum=randint(0,(pop->species).size()-1);
//
//							//Choose a random species tending towards better species
//							randmult=gaussrand()/4;
//							if (randmult>1.0) randmult=1.0;
//							//This tends to select better species
//							randspeciesnum=(int) floor((randmult*(sorted_species.size()-1.0))+0.5);
//							cursp=(sorted_species.begin());
//							for(spcount=0;spcount<randspeciesnum;spcount++)
//								++cursp;
//							randspecies=(*cursp);
//
//							++giveup;
//						}
//
//						//OLD WAY: Choose a random dad from the random species
//						//Select a random dad from the random Species
//						//NOTE:  It is possible that a mating could take place
//						//       here between the mom and a baby from the NEW
//						//       generation in some other Species
//						//orgnum=randint(0,(randspecies->organisms).size()-1);
//						//curorg=(randspecies->organisms).begin();
//						//for(orgcount=0;orgcount<orgnum;orgcount++)
//						//  ++curorg;
//						//dad=(*curorg);            
//
//						//New way: Make dad be a champ from the random species
//						dad=(*((randspecies->organisms).begin()));
//
//						outside=true;	
//					}
//
//					//Perform mating based on probabilities of differrent mating types
//					if (randfloat()<NEAT::mate_multipoint_prob) { 
//						new_genome=(mom->gnome)->mate_multipoint(dad->gnome,count,mom->orig_fitness,dad->orig_fitness,outside);
//					}
//					else if (randfloat()<(NEAT::mate_multipoint_avg_prob/(NEAT::mate_multipoint_avg_prob+NEAT::mate_singlepoint_prob))) {
//						new_genome=(mom->gnome)->mate_multipoint_avg(dad->gnome,count,mom->orig_fitness,dad->orig_fitness,outside);
//					}
//					else {
//						new_genome=(mom->gnome)->mate_singlepoint(dad->gnome,count);
//					}
//
//					mate_baby=true;
//
//					//Determine whether to mutate the baby's Genome
//					//This is done randomly or if the mom and dad are the same organism
//					if ((randfloat()>NEAT::mate_only_prob)||
//						((dad->gnome)->genome_id==(mom->gnome)->genome_id)||
//						(((dad->gnome)->compatibility(mom->gnome))==0.0))
//					{
//
//						//Do the mutation depending on probabilities of 
//						//various mutations
//						if (randfloat()<NEAT::mutate_add_node_prob) {
//							new_genome->mutate_add_node(pop->innovations,pop->cur_node_id,pop->cur_innov_num);
//							//  std::cout<<"mutate_add_node: "<<new_genome<<std::endl;
//							mut_struct_baby=true;
//						}
//						else if (randfloat()<NEAT::mutate_add_link_prob) {
//							net_analogue=new_genome->genesis(generation);
//							new_genome->mutate_add_link(pop->innovations,pop->cur_innov_num,NEAT::newlink_tries);
//							delete net_analogue;
//							//std::cout<<"mutate_add_link: "<<new_genome<<std::endl;
//							mut_struct_baby=true;
//						}
//						else {
//							//Only do other mutations when not doing sturctural mutations
//
//							if (randfloat()<NEAT::mutate_random_trait_prob) {
//								new_genome->mutate_random_trait();
//								//std::cout<<"..mutate random trait: "<<new_genome<<std::endl;
//							}
//							if (randfloat()<NEAT::mutate_link_trait_prob) {
//								new_genome->mutate_link_trait(1);
//								//std::cout<<"..mutate link trait: "<<new_genome<<std::endl;
//							}
//							if (randfloat()<NEAT::mutate_node_trait_prob) {
//								new_genome->mutate_node_trait(1);
//								//std::cout<<"mutate_node_trait: "<<new_genome<<std::endl;
//							}
//							if (randfloat()<NEAT::mutate_link_weights_prob) {
//								new_genome->mutate_link_weights(mut_power,1.0,GAUSSIAN);
//								//std::cout<<"mutate_link_weights: "<<new_genome<<std::endl;
//							}
//							if (randfloat()<NEAT::mutate_toggle_enable_prob) {
//								new_genome->mutate_toggle_enable(1);
//								//std::cout<<"mutate_toggle_enable: "<<new_genome<<std::endl;
//							}
//							if (randfloat()<NEAT::mutate_gene_reenable_prob) {
//								new_genome->mutate_gene_reenable(); 
//								//std::cout<<"mutate_gene_reenable: "<<new_genome<<std::endl;
//							}
//						}
//
//						//Create the baby
//						baby=new Organism(0.0,new_genome,generation);
//
//					}
//					else {
//						//Create the baby without mutating first
//						baby=new Organism(0.0,new_genome,generation);
//					}
//
//				}
//
//				//Add the baby to its proper Species
//				//If it doesn't fit a Species, create a new one
//
//				baby->mut_struct_baby=mut_struct_baby;
//				baby->mate_baby=mate_baby;
//
//				curspecies=(pop->species).begin();
//				if (curspecies==(pop->species).end()){
//					//Create the first species
//					newspecies=new Species(++(pop->last_species),true);
//					(pop->species).push_back(newspecies);
//					newspecies->add_Organism(baby);  //Add the baby
//					baby->species=newspecies;  //Point the baby to its species
//				} 
//				else {
//					comporg=(*curspecies)->first();
//					found=false;
//					while((curspecies!=(pop->species).end())&&
//						(!found)) {	
//							if (comporg==0) {
//								//Keep searching for a matching species
//								++curspecies;
//								if (curspecies!=(pop->species).end())
//									comporg=(*curspecies)->first();
//							}
//							else if (((baby->gnome)->compatibility(comporg->gnome))<NEAT::compat_threshold) {
//								//Found compatible species, so add this organism to it
//								(*curspecies)->add_Organism(baby);
//								baby->species=(*curspecies);  //Point organism to its species
//								found=true;  //Note the search is over
//							}
//							else {
//								//Keep searching for a matching species
//								++curspecies;
//								if (curspecies!=(pop->species).end()) 
//									comporg=(*curspecies)->first();
//							}
//						}
//
//						//If we didn't find a match, create a new species
//						if (found==false) {
//						  newspecies=new Species(++(pop->last_species),true);
//						  //std::std::cout<<"CREATING NEW SPECIES "<<pop->last_species<<std::std::endl;
//						  (pop->species).push_back(newspecies);
//						  newspecies->add_Organism(baby);  //Add the baby
//						  baby->species=newspecies;  //Point baby to its species
//						}
//
//
//				} //end else 
//
//			}*/
//			return true;
//	}
//	
	
	//
	// ~Species();
	//
	// };
	//
	// // This is used for list sorting of Species by fitness of best organism
	// highest fitness first
	// bool order_species(Species *x, Species *y);
	//
	// bool order_new_species(Species *x, Species *y);
	//
	// }
	public static boolean order_spec(Species x, Species y) {
		return (x).ave_fitness > (y).ave_fitness;
	}
	
	@Override
	public int compareTo(Species spec) {
		if(Species.order_spec(this, spec)) return 1;
		else if (Species.order_spec(spec, this)) return -1;
		else return 0;
	}

} // end of Species class

//
// #endif
//

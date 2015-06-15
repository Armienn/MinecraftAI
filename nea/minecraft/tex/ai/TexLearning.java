package nea.minecraft.tex.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import nea.minecraft.tex.brain.BrainSenses;
import nea.minecraft.tex.brain.NeatCore;
import nea.minecraft.tex.brain.TexBrain;
import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.interaction.Actions;
import nea.minecraft.tex.interaction.Senses;
import nea.minecraft.tex.learning.ActionKnowledge;
import nea.minecraft.tex.learning.ActionMemory;
import nea.minecraft.tex.learning.LearningResult;
import nea.minecraft.tex.memory.ShortTermMemory;
import nea.minecraft.tex.memory.utility.Interval;
import nea.minecraft.tex.memory.utility.MemAction;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemReward;
import nea.minecraft.tex.memory.utility.ParameterValue;
import nea.rtNEAT.Genome;
import nea.rtNEAT.Network;
import nea.rtNEAT.Nnode;
import nea.rtNEAT.Organism;
import nea.rtNEAT.Species;

public class TexLearning extends Thread {
	TexBrain brain;
	LearningResult learningResult;
	long lastCheck = 0;
	boolean useNEAT = false;
	
	static final int consequencetime = 20;
	
	// NEAT
	NeatCore neatbrain;
	long lastUpdate = 0;
	//NEAT END
	
	public TexLearning(TexBrain brain, boolean useneat){
		this.brain = brain;
		this.setName("TexLearning");
		this.useNEAT = useneat;
		if(useneat){
			Genome g = new Genome(1, 11, 2, 50, 63, true, 0.7);
			neatbrain = new NeatCore(brain, g);
		}
	}
	
	public void run(){
		brain.Log("Starting Learning thread");
		if(useNEAT) NEAT();
		else Homebrew();
		brain.Log("Ending Learning thread");
	}
	
	private void Homebrew(){
		while(brain.KeepRunning()){
			synchronized(brain){
				ShortTermMemory memory = brain.memory.shortterm;
				if(memory.selfMemory != null){
					long currenttime = memory.currentTime;
					Interval newInterval = new Interval(lastCheck + 1, currenttime);
					Interval leadingInterval = newInterval.Offset(- consequencetime);
					//Interval trailingInterval = new Interval(leadingInterval.endTime, currenttime);
					
					/// Examine results of actions:
					MemAction[] actions = memory.selfMemory.GetActionsInInterval(leadingInterval, true);
					for(MemAction a : actions){
						//if(a.type==Action.Type.Jump)
						//	a = a;
						ActionMemory actionmemory = new ActionMemory(a, memory, consequencetime);
						ActionKnowledge knowledge = brain.knowledge.GetKnowledge(a.type);
						knowledge.Process(actionmemory);
						//entity[] ents = blabla
						//for each entity
						//  for each property
						//    Event[] events = get events in trailing interval
						//    for each event
						//      (if event begins after actions)
						//      add possible connection to actionmemory
						//    if entity appeared in trailing interval
						//      add possible connection to actionmemory
						//for each reward
						//  add possible connection to actionmemory
						//use actionmemory to update actionknowledge(?)
					}
					
					
					/// Examine failed actions:
					// actions = memory.selfMemory.GetActionsInInterval(newInterval, false);
					
					/// Examine time before rewards:
					// memory.selfMemory.GetRewardsInInterval(examineInterval);
					
					///
					lastCheck = currenttime;
				}
			}
			trySleep(100);
			while(brain.Pause()){
				trySleep(100);
			}
		}
	}
	
	private void NEAT(){
		Vector<Organism> org = new Vector<Organism>();
		Vector<Organism> organisms = new Vector<Organism>();
		//Species species = null;
		int gencount = 1;
		int invValue = 0;
		double sum = 0;
		boolean success = false;
		double input0 = 0;
		double input1 = 0;
		double input2 = 0;
		double input3 = 0;
		double[] invInput = new double[8];
		int inputnodes = 1; // add the bias node
		int outputnodes = 0;
		boolean firstrun = true;
		
		double firstHF = 0; //first high fitness
		double secondHF = 0; //second high fitness
		
		for(int i=0; i<12; i++){
			neatbrain.neatnet.inputs.add(new Nnode(Nnode.nodetype.SENSOR, i));
			inputnodes++;
		}
		
		for(int i=0; i<2; i++){
			neatbrain.neatnet.add_output(new Nnode(Nnode.nodetype.SENSOR, i));
			outputnodes++;
		}
		
		while(brain.KeepRunning()){
			//Add all genomes for the pop
			FileOutputStream is = null;
			FileOutputStream os = null;
			double this_out = 0;
			double[] out = null;
			Genome g = null;
			/*
			try {
				is = new FileInputStream("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\minecraft_startgenes");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader iFile = new BufferedReader(new InputStreamReader(is));*/
			
			for(int i=0; i<5; i++){
				try {
					os = new FileOutputStream("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\Genome_test" + i);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedWriter oFile = new BufferedWriter(new OutputStreamWriter(os));
				
				long timerStart = System.nanoTime();
				brain.Log("Starting organism # " + i);
				long currentTime = brain.worldObj.getTotalWorldTime();
				long startTime = brain.worldObj.getTotalWorldTime();
				
				if(gencount == 1){
					//Genome g = new Genome(i, iFile);
					g = new Genome(i, inputnodes, outputnodes, 0, 100, false, 0.6); //public Genome(int new_id, int i, int o, int n, int nmax, boolean r, double linkprob)
					//Genome g = new Genome(inputnodes+1, outputnodes, 0, 3);//public Genome(int num_in, int num_out, int num_hidden, int type) {
					org.add(new Organism(1, g, gencount));
					//firstrun = false;
				}
				else g =  neatbrain.p.organisms.get(i).gnome;
				
				//Print the genomes to a file
				g.print_to_file(oFile);
//				g.phenotype.all_nodes.addAll(g.nodes);
//				g.phenotype.print_links_tofile("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\phenotype_print");
				
//				org.update_phenotype();
				for(int j=0; j < org.size(); j++){
					org.get(j).print_to_file("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\Organism_test " + i);
					//organisms.add(org.get(j));
				}
				//NeatCore nc = new NeatCore(brain, g);
				long timerEnd = System.nanoTime();
				//brain.Log("Initializing startgene file took :" + (timerEnd - timerStart));
				
				while(currentTime < startTime + 200 && brain.KeepRunning()){
					neatbrain.p.organisms.add(org.get(i));
					//parse inputs and outputs
					//inputs:
					long time = 0;
					synchronized(brain){
						time = brain.senses.senses.time;
					}
					if(time > lastUpdate){
						timerStart = System.nanoTime();
						synchronized(brain){
							Senses senses = brain.senses.senses;
							if(senses.entityinfo.size() > 0){
								MemEntity entity = senses.entityinfo.get(0);
								input0 = entity.GetProperties()[0].hashCode();
								input1 = senses.entityinfo.get(0).GetParameter("PositionX").GetParameter().value;
								input2 = senses.entityinfo.get(0).GetParameter("PositionY").GetParameter().value;
								for(int j = 0; j < invInput.length; j++){
									invInput[j] = 0;
								}
								for(MemEntity ent : senses.entityinfo){
									if(senses.entityinfo.get(0).GetParameter("PositionInventory") != null){
										if(!senses.entityinfo.get(0).GetParameter("PositionInventory").GetParameter().IsUndefined()){
											invValue = (int)(senses.entityinfo.get(0).GetParameter("PositionInventory").GetParameter().value * 8);
											invInput[invValue] = 1;
										}
									}
								}
							}
						}
						timerEnd = System.nanoTime();
						brain.Log("Sensory parsing :" + (timerEnd - timerStart));
						timerStart = System.nanoTime();
						
						//Load sensory data for rewards, entity in sight and positions x,y
						neatbrain.neatnet.inputs.get(0).sensor_load(input0);
						neatbrain.neatnet.inputs.get(1).sensor_load(input1);
						neatbrain.neatnet.inputs.get(2).sensor_load(input2);
						neatbrain.neatnet.inputs.get(11).sensor_load(input3);
						
						//Load sensory data for every inventory slot
						neatbrain.neatnet.inputs.get(3).sensor_load(invInput[0]);
						neatbrain.neatnet.inputs.get(4).sensor_load(invInput[1]);
						neatbrain.neatnet.inputs.get(5).sensor_load(invInput[2]);
						neatbrain.neatnet.inputs.get(6).sensor_load(invInput[3]);
						neatbrain.neatnet.inputs.get(7).sensor_load(invInput[4]);
						neatbrain.neatnet.inputs.get(8).sensor_load(invInput[5]);
						neatbrain.neatnet.inputs.get(9).sensor_load(invInput[7]);
						neatbrain.neatnet.inputs.get(10).sensor_load(invInput[7]);
						
						timerEnd = System.nanoTime();
						brain.Log("Inputs loaded took time :" + (timerEnd - timerStart));
						timerStart = System.nanoTime();
						
						//brain.Log("Inputs loaded " + System.nanoTime()/1000000);
						/*
						//Load sensory data for self
						input3 = senses.self.GetProperties()[0].hashCode();
						//input1 = senses.entityinfo.get(0).GetParameter("positionx").GetParameter().value;
						neatbrain.neatnet.inputs.get(11).sensor_load(input3);*/
						
						success = neatbrain.neatnet.activate();
						timerEnd = System.nanoTime();
						brain.Log("Activation :" + (timerEnd - timerStart));
						timerStart = System.nanoTime();
						
						//outputs:	
						double reward = 0;
						
						for(int relax = 0; relax <= neatbrain.neatnet.max_depth(); relax ++){
							success = neatbrain.neatnet.activate();
						}
						
						Action.Type[] types = Action.GetTypes();
						int neatoutput = (int) (neatbrain.neatnet.outputs.get(0).output *types.length);
						Action.Type type = types[neatoutput];
						Action action = new Action(type);
						
						if(action.DegreesOfFreedom() > 0){
							float neatoutput1 = (float) (neatbrain.neatnet.outputs.get(1).output);				
							action.SetParameter(0, neatoutput1);
							brain.Log("Action Decided " + neatoutput);
						}
						synchronized(brain){
							brain.senses.actions.actions.add(action);
						}
						
						neatbrain.neatnet.flush();
						
						timerEnd = System.nanoTime();
						
						brain.Log("Deciding action took time :" + (timerEnd - timerStart));
						
						synchronized(brain){
							lastUpdate = brain.senses.senses.time;
							if(!brain.senses.senses.rewards.isEmpty()){
								input3 = brain.senses.senses.rewards.get(0).value.value;
								sum += input3;
								brain.Log("Total reward " + sum + " took " + System.nanoTime());
							}
							else if(brain.senses.senses.rewards.isEmpty()){
								input3 = 0;
							}
						}
					}
					else{
						try {
							Thread.sleep(45);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					currentTime = brain.worldObj.getTotalWorldTime();
				}
				
				timerStart = System.nanoTime();
				if(!brain.KeepRunning())
					break;
				//get final fitness
				//get genome + fitness
				if(success){
					org.get(i).fitness = sum;
				}
				else{
					org.get(i).fitness = 0.001;
				}
				timerEnd = System.nanoTime();
				
				brain.Log("Fitness calculation took :" + (timerEnd - timerStart));
				
				//g.mutate_random_trait();
				//g.mutate_link_trait(10);
				//g.mutate_add_node(neatbrain.p.innovation, neatbrain.p.cur_node_id, neatbrain.p.cur_innov_num);
				//firstHF = org.get(i).high_fit;
				//secondHF = org.get(i).high_fit;
				neatbrain.p.speciate();
				neatbrain.p.epoch(org.get(i).generation);
				org.get(i).update_phenotype();
				}
			//do generation end stuff and end generation	
			gencount++;
			//neatbrain.neatnet.show_activation();
			neatbrain.neatnet.print_links_tofile("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\linksToFile");
			//neatbrain.neatnet.show_input();
			}			
		}
	
	static boolean trySleep(long milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

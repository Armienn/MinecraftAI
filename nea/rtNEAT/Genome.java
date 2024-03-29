package nea.rtNEAT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Vector;

import nea.rtNEAT.Nnode.nodeplace;

//#ifndef _GENOME_H_
//#define _GENOME_H_
//
//#include <vector>
//#include "gene.h"
//#include "innovation.h"
//
//namespace NEAT {
//
//	enum mutator {
//		GAUSSIAN = 0,
//		COLDGAUSSIAN = 1
//	};
//
//	//----------------------------------------------------------------------- 
//	//A Genome is the primary source of genotype information used to create   
//	//a phenotype.  It contains 3 major constituents:                         
//	//  1) A list of Traits                                                 
//	//  2) A list of NNodes pointing to a Trait from (1)                      
//	//  3) A list of Genes with Links that point to Traits from (1)           
//	//(1) Reserved parameter space for future use
//	//(2) NNode specifications                                                
//	//(3) Is the primary source of innovation in the evolutionary Genome.     
//	//    Each Gene in (3) has a marker telling when it arose historically.   
//	//    Thus, these Genes can be used to speciate the population, and the   
//	//    list of Genes provide an evolutionary history of innovation and     
//	//    link-building.
//
//	class Genome {
public class Genome {

	public enum mutator {
		GAUSSIAN, COLDGAUSSIAN
	};

	//
	// public:
	// int genome_id;
	public int genome_id;
	//
	// std::vector<Trait*> traits; //parameter conglomerations
	public Vector<Trait> traits = new Vector<Trait>();
	// std::vector<NNode*> nodes; //List of NNodes for the Network
	public Vector<Nnode> nodes = new Vector<Nnode>();
	// std::vector<Gene*> genes; //List of innovation-tracking genes
	public Vector<Gene> genes = new Vector<Gene>();
	//
	// Network *phenotype; //Allows Genome to be matched with its Network
	public Network phenotype;

	//
	// int get_last_node_id(); //Return id of final NNode in Genome
	public int get_last_node_id() {
		return nodes.lastElement().node_id;
	}

	// double get_last_gene_innovnum(); //Return last innovation number in
	// Genome
	public double get_last_gene_innovnum() {
		// return ((*(genes.end() - 1))->innovation_num)+1;
		return genes.lastElement().innovation_num;
	}

	//
	// void print_genome(); //Displays Genome on screen
	//
	// //Constructor which takes full genome specs and puts them into the new
	// one
	// Genome(int id, std::vector<Trait*> t, std::vector<NNode*> n,
	// std::vector<Gene*> g);
	// public Genome(int id, Vector<Trait> t, Vector<Nnode> n, Vector<Gene> g) {
	// genome_id=id;
	// traits= new Vector<Trait>(t);
	// nodes= new Vector<Nnode>(n);
	// genes= new Vector<Gene>(g);
	// }
	//
	// //Constructor which takes in links (not genes) and creates a Genome
	// Genome(int id, std::vector<Trait*> t, std::vector<NNode*> n,
	// std::vector<Link*> links);
	public Genome(int id, Vector<Trait> t, Vector<Nnode> n, Vector<Gene> g,
			Vector<Link> links) {
		// std::vector<Link*>::iterator curlink;
		Gene tempgene;
		if (t != null)
			traits = new Vector<Trait>(t);
		if (n != null)
			nodes = new Vector<Nnode>(n);
		if (g != null)
			genes = new Vector<Gene>(g);

		genome_id = id;

		// We go through the links and turn them into original genes
		// for(curlink=links.begin();curlink!=links.end();++curlink) {
		for (Link curlink : links) {
			// Create genes one at a time
			tempgene = new Gene((curlink).linktrait, (curlink).weight,
					(curlink).in_node, (curlink).out_node,
					(curlink).is_recurrent, 1.0, 0.0);
			genes.add(tempgene);
		}

	}
	
	public Genome(int id, Vector<Trait> t, Vector<Nnode> n, Vector<Gene> g) {
		genome_id = id;
		traits = t;
		nodes = n; 
		genes = g;
	}

	//
	// // Copy constructor
	// Genome(const Genome& genome);

	public Genome(Genome genome) {
		genome_id = genome.genome_id;

		// Vector<Trait>::const_iterator curtrait;
		// std::vector<NNode*>::const_iterator curnode;
		// std::vector<Gene*>::const_iterator curgene;

		// for(curtrait=genome.traits.begin(); curtrait!=genome.traits.end();
		// ++curtrait) {
		for (Trait curtrait : genome.traits) {
			traits.add(new Trait(curtrait));
		}

		Trait assoc_trait = new Trait();
		// Duplicate NNodes
		// for(curnode=genome.nodes.begin();curnode!=genome.nodes.end();++curnode)
		// {
		for (Nnode curnode : genome.nodes) {
			// First, find the trait that this node points to
			if (((curnode).nodetrait) == null)
				assoc_trait = null;
			else {
				// curtrait=traits.firstElement();
				// while(((*curtrait)->trait_id)!=(((*curnode)->nodetrait)->trait_id))
				// ++curtrait;
				for (Trait curtrait : genome.traits) {
					if (curtrait.trait_id != curnode.nodetrait.trait_id)
						continue;
					assoc_trait = new Trait(curtrait);
					break;
				}
				// assoc_trait=(*curtrait);
			}

			Nnode newnode = new Nnode(curnode, assoc_trait);

			(curnode).dup = newnode; // Remember this node's old copy
			// (*curnode)->activation_count=55;
			nodes.add(newnode);
		}

		Nnode inode = null; // For forming a gene
		Nnode onode = null;; // For forming a gene
		Trait traitptr = null;

		// Duplicate Genes
		// for(curgene=genome.genes.begin(); curgene!=genome.genes.end();
		// ++curgene) {
		for (Gene curgene : genome.genes) {
			// First find the nodes connected by the gene's link

			inode = (((curgene).lnk).in_node).dup;
			onode = (((curgene).lnk).out_node).dup;

			// Get a pointer to the trait expressed by this gene
			traitptr = ((curgene).lnk).linktrait;
			if (traitptr == null)
				assoc_trait = null;
			else {
				// curtrait=traits.begin();
				// while(((*curtrait)->trait_id)!=(traitptr->trait_id))
				// ++curtrait;
				// assoc_trait=(*curtrait);
				for (Trait curtrait : traits) {
					if (curtrait.trait_id != traitptr.trait_id)
						continue;
					assoc_trait = new Trait(curtrait);
					break;
				}
			}

			Gene newgene = new Gene(curgene, assoc_trait, inode, onode);
			genes.add(newgene);

		}
	}

	//
	// //Special constructor which spawns off an input file
	// //This constructor assumes that some routine has already read in
	// GENOMESTART
	// Genome(int id, std::ifstream &iFile);
	public Genome(int id, BufferedReader iFile) {
		try {
			// char curword[128]; //max word size of 128 characters
			String curword;
			// char curline[1024]; //max line size of 1024 characters
			String curline;
			// char delimiters[] = " \n";
			String delimeters = new String(" \n");

			// int done=0;
			boolean done = false;

			// int pause;

			genome_id = id;

			// iFile.getline(curline, sizeof(curline));
			curline = iFile.readLine();
			// int wordcount = NEAT::getUnitCount(curline, delimiters);
			int wordcount = Neat.getUnitCount(curline, delimeters);
			int curwordnum = 0;

			// Loop until file is finished, parsing each line
			while (!done) {

				// std::cout << curline << std::endl;

				if (curwordnum > wordcount || wordcount == 0) {
					// iFile.getline(curline, sizeof(curline));
					curline = iFile.readLine();
					// wordcount = NEAT::getUnitCount(curline, delimiters);
					wordcount = Neat.getUnitCount(curline, delimeters);
					curwordnum = 0;
				}

				// std::stringstream ss(curline);
				// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
				// delimiters));
				// ss >> curword;
				curword = curline.split(" ", 2)[0];
				curline = new String(curline.split(" ", 2)[1]);

				// printf(curword);
				// printf(" test\n");
				// Check for end of Genome
				// if (strcmp(curword,"genomeend")==0) {
				if (curword.equalsIgnoreCase("genomeend")) {
					// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
					// delimiters));
					// ss >> curword;
					curword = curline.split(" ", 2)[0];
					//curline = new String(curline.split(" ", 2)[1]);
					int idcheck = Integer.parseInt(curword);
					// iFile>>idcheck;
					if (idcheck != genome_id)
						System.out.println("ERROR: id mismatch in genome");
					else{
						done = true;
						System.out.println("Genome successfully loaded!");
					}
				}

				// Ignore genomestart if it hasn't been gobbled yet
				else if (curword.equalsIgnoreCase("genomestart")) {
					++curwordnum;
					// cout<<"genomestart"<<endl;
				}

				// Ignore comments surrounded by - they get printed to screen
				else if (curword.equalsIgnoreCase("/*")) {
					// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
					// delimiters));
					// ss >> curword;
					curword = curline.split(" ", 2)[0];
					//curline = new String(curline.split(" ", 2)[1]);
					while ((curword != "*/")) {
						// cout<<curword<<" ";
						// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
						// delimiters));
						// ss >> curword;
						curword = curline.split(" ", 2)[0];
						//curline = new String(curline.split(" ", 2)[1]);
					}
					// cout<<endl;
				}

				// Read in a trait
				else if ((curword.equalsIgnoreCase("trait"))) {
					Trait newtrait  = null;

					// char argline[1024];
					String argline;
					// strcpy(argline, NEAT::getUnits(curline, curwordnum,
					// wordcount, delimiters));

					curwordnum = wordcount + 1;

					// ss.getline(argline, 1024);
					argline = curline.split("\n", 2)[0];
					//curline = curline.split("\n", 2)[1];
					// Allocate the new trait
					newtrait = new Trait(argline);

					// Add trait to vector of traits
					traits.add(newtrait);
				}

				// Read in a node
				else if (curword.equalsIgnoreCase("node")) {
					Nnode newnode = null;

					// char argline[1024];
					String argline;
					// strcpy(argline, NEAT::getUnits(curline, curwordnum,
					// wordcount, delimiters));
					curwordnum = wordcount + 1;

					// ss.getline(argline, 1024);
					argline = curline.split("\n", 2)[0];
					//curline = curline.split("\n", 2)[1];
					// Allocate the new node
					newnode = new Nnode(argline, traits);

					// Add the node to the list of nodes
					nodes.add(newnode);
				}

				// Read in a Gene
				else if (curword.equalsIgnoreCase("gene")) {
					Gene newgene = null;

					// char argline[1024];
					String argline;
					// strcpy(argline, NEAT::getUnits(curline, curwordnum,
					// wordcount, delimiters));
					curwordnum = wordcount + 1;

					// ss.getline(argline, 1024);
					argline = curline.split("\n", 2)[0];
					//curline = curline.split("\n", 2)[1];
					// std::cout << "New gene: " << ss.str() << std::endl;
					// Allocate the new Gene
					newgene = new Gene(argline, traits, nodes);

					// Add the gene to the genome
					genes.add(newgene);

					// std::cout<<"Added gene " << newgene << std::endl;
				}
				curline = iFile.readLine();
			}

		} catch (Exception e) {
			System.out.println("Genome has had trouble reading from file");
			System.out.println(e.getMessage());
		}
		try {
			iFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	// // This special constructor creates a Genome
	// // with i inputs, o outputs, n out of nmax hidden units, and random
	// // connectivity. If r is true then recurrent connections will
	// // be included.
	// // The last input is a bias
	// // Linkprob is the probability of a link
	// Genome(int new_id,int i, int o, int n,int nmax, bool r, double linkprob);
	public Genome(int new_id, int i, int o, int n, int nmax, boolean r,
			double linkprob) {
		int totalnodes = 0;
		boolean[] cm = null; // The connection matrix which will be randomized
		boolean[] cmp = null; // Connection matrix pointer
		int matrixdim = 0;
		int count = 0;

		int ncount = 0; // Node and connection counters
		int ccount = 0;

		int row = 0; // For navigating the matrix
		int col = 0;

		double new_weight = 0;

		int maxnode = 0; // No nodes above this number for this genome

		int first_output = 0; // Number of first output node

		totalnodes = i + o + nmax;
		matrixdim = totalnodes * totalnodes;
		cm = new boolean[matrixdim]; // Dimension the connection matrix
		maxnode = i + n;

		first_output = totalnodes - o + 1;

		// For creating the new genes
		Nnode newnode = null;
		Gene newgene = null;
		Trait newtrait = null;
		Nnode in_node = null;
		Nnode out_node = null;

		// Retrieves the nodes pointed to by connection genes
		// Vector<Nnode>::iterator node_iter;

		// Assign the id
		genome_id = new_id;

		// cout<<"Assigned id "<<genome_id<<endl;

		// Step through the connection matrix, randomly assigning bits
		cmp = cm;
		for (count = 0; count < matrixdim; count++) {
			if (Neat.randfloat() < linkprob)
				cmp[count] = true;
			else
				cmp[count] = false;
			// cmp++;
		}

		// Create a dummy trait (this is for future expansion of the system)
		newtrait = new Trait(1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		traits.add(newtrait);

		// Build the input nodes
		for (ncount = 1; ncount <= i; ncount++) {
			if (ncount < i)
				newnode = new Nnode(Nnode.nodetype.SENSOR, ncount,
						Nnode.nodeplace.INPUT);
			else
				newnode = new Nnode(Nnode.nodetype.SENSOR, ncount,
						Nnode.nodeplace.BIAS);

			newnode.nodetrait = newtrait;

			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the hidden nodes
		for (ncount = i + 1; ncount <= i + n; ncount++) {
			newnode = new Nnode(Nnode.nodetype.NEURON, ncount,
					Nnode.nodeplace.HIDDEN);
			newnode.nodetrait = newtrait;
			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the output nodes
		for (ncount = first_output; ncount <= totalnodes; ncount++) {
			newnode = new Nnode(Nnode.nodetype.NEURON, ncount,
					Nnode.nodeplace.OUTPUT);
			newnode.nodetrait = newtrait;
			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// cout<<"Built nodes"<<endl;

		// Connect the nodes
		ccount = 1; // Start the connection counter

		// Step through the connection matrix, creating connection genes
		cmp = cm;
		count = 0;
		for (col = 1; col <= totalnodes; col++)
			for (row = 1; row <= totalnodes; row++) {
				// Only try to create a link if it is in the matrix
				// and not leading into a sensor

				if ((cmp[row] == true) && (col > i)
						&& ((col <= maxnode) || (col >= first_output))
						&& ((row <= maxnode) || (row >= first_output))) {
					// If it isn't recurrent, create the connection no matter
					// what
					if (col > row) {

						// Retrieve the in_node
						// node_iter=nodes.begin();
						// while((*node_iter)->node_id!=row)
						// node_iter++;
						//
						// in_node=(*node_iter);
						for (Nnode node_iter : nodes) {
							if (node_iter.node_id != row)
								continue;
							in_node = new Nnode(node_iter);
							break;
						}

						// Retrieve the out_node
						// node_iter=nodes.begin();
						// while((*node_iter)->node_id!=col)
						// node_iter++;
						//
						// out_node=(*node_iter);
						for (Nnode node_iter : nodes) {
							if (node_iter.node_id != col)
								continue;
							out_node = new Nnode(node_iter);
							break;
						}

						// Create the gene
						new_weight = Neat.randposneg() * Neat.randfloat();
						newgene = new Gene(newtrait, new_weight, in_node,
								out_node, false, count, new_weight);

						// Add the gene to the genome
						genes.add(newgene);
					} else if (r) {
						// Create a recurrent connection

						// Retrieve the in_node
						// node_iter=nodes.begin();
						// while((*node_iter)->node_id!=row)
						// node_iter++;
						//
						// in_node=(*node_iter);
						for (Nnode node_iter : nodes) {
							if (node_iter.node_id != row)
								continue;
							in_node = new Nnode(node_iter);
							break;
						}

						// Retrieve the out_node
						// node_iter=nodes.begin();
						// while((*node_iter)->node_id!=col)
						// node_iter++;
						//
						// out_node=(*node_iter);
						for (Nnode node_iter : nodes) {
							if (node_iter.node_id != col)
								continue;
							out_node = new Nnode(node_iter);
							break;
						}

						// Create the gene
						new_weight = Neat.randposneg() * Neat.randfloat();
						newgene = new Gene(newtrait, new_weight, in_node,
								out_node, true, count, new_weight);

						// Add the gene to the genome
						genes.add(newgene);

					}

				}

				count++; // increment gene counter
				// cmp++;
			}

		// delete [] cm;

	}

	//
	// //Special constructor that creates a Genome of 3 possible types:
	// //0 - Fully linked, no hidden nodes
	// //1 - Fully linked, one hidden node splitting each link
	// //2 - Fully connected with a hidden layer, recurrent
	// //num_hidden is only used in type 2
	// Genome(int num_in,int num_out,int num_hidden,int type);
	public Genome(int num_in, int num_out, int num_hidden, int type) {

		// Temporary lists of nodes
		Vector<Nnode> inputs = new Vector<Nnode>();
		Vector<Nnode> outputs = new Vector<Nnode>();
		Vector<Nnode> hidden = new Vector<Nnode>();
		Nnode bias = null; // Remember the bias

		// std::vector<NNode*>::iterator curnode1; //Node iterator1
		// std::vector<NNode*>::iterator curnode2; //Node iterator2
		// std::vector<NNode*>::iterator curnode3; //Node iterator3

		// For creating the new genes
		Nnode newnode;
		Gene newgene;
		Trait newtrait;

		int count;
		int ncount;

		// Assign the id 0
		genome_id = 0;

		// Create a dummy trait (this is for future expansion of the system)
		newtrait = new Trait(1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		traits.add(newtrait);

		// Adjust hidden number
		if (type == 0)
			num_hidden = 0;
		else if (type == 1)
			num_hidden = num_in * num_out;

		// Create the inputs and outputs

		// Build the input nodes
		for (ncount = 1; ncount <= num_in; ncount++) {
			if (ncount < num_in)
				newnode = new Nnode(Nnode.nodetype.SENSOR, ncount,
						Nnode.nodeplace.INPUT);
			else {
				newnode = new Nnode(Nnode.nodetype.SENSOR, ncount,
						Nnode.nodeplace.BIAS);
				bias = newnode;
			}

			// newnode->nodetrait=newtrait;

			// Add the node to the list of nodes
			nodes.add(newnode);
			inputs.add(newnode);
		}

		// Build the hidden nodes
		for (ncount = num_in + 1; ncount <= num_in + num_hidden; ncount++) {
			newnode = new Nnode(Nnode.nodetype.NEURON, ncount,
					Nnode.nodeplace.HIDDEN);
			// newnode->nodetrait=newtrait;
			// Add the node to the list of nodes
			nodes.add(newnode);
			hidden.add(newnode);
		}

		// Build the output nodes
		for (ncount = num_in + num_hidden + 1; ncount <= num_in + num_hidden
				+ num_out; ncount++) {
			newnode = new Nnode(Nnode.nodetype.NEURON, ncount,
					Nnode.nodeplace.OUTPUT);
			// newnode->nodetrait=newtrait;
			// Add the node to the list of nodes
			nodes.add(newnode);
			outputs.add(newnode);
		}

		// Create the links depending on the type
		if (type == 0) {
			// Just connect inputs straight to outputs

			count = 1;

			// Loop over the outputs
			// for(curnode1=outputs.begin();curnode1!=outputs.end();++curnode1)
			// {
			for (Nnode curnode1 : outputs) {
				// Loop over the inputs
				// for(curnode2=inputs.begin();curnode2!=inputs.end();++curnode2)
				// {
				for (Nnode curnode2 : inputs) {
					// Connect each input to each output
					newgene = new Gene(newtrait, 0, (curnode2), (curnode1),
							false, count, 0);

					// Add the gene to the genome
					genes.add(newgene);

					count++;

				}

			}

		} // end type 0
			// A split link from each input to each output
		else if (type == 1) {
			count = 1; // Start the gene number counter

			// curnode3=hidden.begin(); //One hidden for ever input-output pair
			int curnode3 = 0;
			// Loop over the outputs
			// for(curnode1=outputs.begin();curnode1!=outputs.end();++curnode1)
			// {
			for (Nnode curnode1 : outputs) {
				// Loop over the inputs
				// for(curnode2=inputs.begin();curnode2!=inputs.end();++curnode2)
				// {
				for (Nnode curnode2 : inputs) {

					// Connect Input to hidden
					newgene = new Gene(newtrait, 0, (curnode2),
							(hidden.get(curnode3)), false, count, 0);
					// Add the gene to the genome
					genes.add(newgene);

					count++; // Next gene

					// Connect hidden to output
					newgene = new Gene(newtrait, 0, (hidden.get(curnode3)),
							(curnode1), false, count, 0);
					// Add the gene to the genome
					genes.add(newgene);

					++curnode3; // Next hidden node
					count++; // Next gene

				}
			}

		}// end type 1
			// Fully connected
		else if (type == 2) {
			count = 1; // Start gene counter at 1

			// Connect all inputs to all hidden nodes
			// for(curnode1=hidden.begin();curnode1!=hidden.end();++curnode1) {
			for (Nnode curnode1 : hidden) {
				// Loop over the inputs
				// for(curnode2=inputs.begin();curnode2!=inputs.end();++curnode2)
				// {
				for (Nnode curnode2 : inputs) {
					// Connect each input to each hidden
					newgene = new Gene(newtrait, 0, (curnode2), (curnode1),
							false, count, 0);

					// Add the gene to the genome
					genes.add(newgene);

					count++;

				}
			}

			// Connect all hidden units to all outputs
			// for(curnode1=outputs.begin();curnode1!=outputs.end();++curnode1)
			// {
			for (Nnode curnode1 : outputs) {
				// Loop over the inputs
				// for(curnode2=hidden.begin();curnode2!=hidden.end();++curnode2)
				// {
				for (Nnode curnode2 : hidden) {
					// Connect each input to each hidden
					newgene = new Gene(newtrait, 0, (curnode2), (curnode1),
							false, count, 0);

					// Add the gene to the genome
					genes.add(newgene);

					count++;

				}
			}

			// Connect the bias to all outputs
			// for(curnode1=outputs.begin();curnode1!=outputs.end();++curnode1)
			// {
			for (Nnode curnode1 : outputs) {
				newgene = new Gene(newtrait, 0, bias, (curnode1), false, count,
						0);

				// Add the gene to the genome
				genes.add(newgene);

				count++;
			}

			// Recurrently connect the hidden nodes
			// for(curnode1=hidden.begin();curnode1!=hidden.end();++curnode1) {
			for (Nnode curnode1 : hidden) {
				// Loop Over all Hidden
				// for(curnode2=hidden.begin();curnode2!=hidden.end();++curnode2)
				// {
				for (Nnode curnode2 : hidden) {
					// Connect each hidden to each hidden
					newgene = new Gene(newtrait, 0, (curnode2), (curnode1),
							true, count, 0);

					// Add the gene to the genome
					genes.add(newgene);

					count++;

				}

			}

		}// end type 2

	}

	//
	// // Loads a new Genome from a file (doesn't require knowledge of Genome's
	// id)
	// static Genome *new_Genome_load(char *filename);
	public static Genome new_Genome_load(String filename) {
		Genome newgenome;

		int id;

		// char curline[1024];
		char curword[] = new char[20]; // max word size of 20 characters
		// String curword;
		// char delimiters[] = " \n";
		// int curwordnum = 0;

		// std::ifstream iFile(filename);
		try {
			InputStream is = new FileInputStream(filename);
			BufferedReader iFile = new BufferedReader(new InputStreamReader(is));

			// Make sure it worked
			// if (!iFile) {
			// cerr<<"Can't open "<<filename<<" for input"<<endl;
			// return 0;
			// }

			// iFile>>curword;
			iFile.read(curword);
			// iFile.getline(curline, sizeof(curline));
			// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
			// delimiters));

			// Bypass initial comment
			if ((new String(curword) == "/*")) {
				// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
				// delimiters));
				// iFile>>curword;
				iFile.read(curword);
				while ((new String(curword) != "*/")) {
					System.out.println(curword);
					// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
					// delimiters));
					// iFile>>curword;
					iFile.read(curword);
				}

				// cout<<endl;
				// iFile>>curword;
				iFile.read(curword);
				// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
				// delimiters));
			}

			// strcpy(curword, NEAT::getUnit(curline, curwordnum++,
			// delimiters));
			// id = atoi(curword);
			// iFile>>id;
			id = iFile.read();

			newgenome = new Genome(id, iFile);

			iFile.close();
			return newgenome;
		} catch (Exception e) {
			System.out.println("trouble constructing new Genome from file");
			System.out.println(e.getMessage());
			return null;
		}
	}

	//
	// //Destructor kills off all lists (including the trait vector)
	// ~Genome();
	//
	// //Generate a network phenotype from this Genome with specified id
	// Network *genesis(int);
	public Network genesis(int id) {
		// std::vector<NNode*>::iterator curnode;
		// std::vector<Gene*>::iterator curgene;
		Nnode newnode = null;
		Trait curtrait = null;
		Link curlink= null;
		Link newlink= null;

		double maxweight = 0.0; // Compute the maximum weight for adaptation
								// purposes
		double weight_mag = 0.0; // Measures absolute value of weights

		// Inputs and outputs will be collected here for the network
		// All nodes are collected in an all_list-
		// this will be used for later safe destruction of the net
		Vector<Nnode> inlist = new Vector<Nnode>();
		Vector<Nnode> outlist = new Vector<Nnode>();
		Vector<Nnode> all_list = new Vector<Nnode>();

		// Gene translation variables
		Nnode inode = null;
		Nnode onode = null;

		// The new network
		Network newnet = null;

		// Create the nodes
		// for(curnode=nodes.begin();curnode!=nodes.end();++curnode) {
		for (Nnode curnode : nodes) {
			newnode = new Nnode((curnode).type, (curnode).node_id);

			// Derive the node parameters from the trait pointed to
			curtrait = (curnode).nodetrait;
			newnode.derive_trait(curtrait);

			// Check for input or output designation of node
			if (((curnode).gen_node_label) == Nnode.nodeplace.INPUT)
				inlist.add(newnode);
			if (((curnode).gen_node_label) == Nnode.nodeplace.BIAS)
				inlist.add(newnode);
			if (((curnode).gen_node_label) == Nnode.nodeplace.OUTPUT)
				outlist.add(newnode);

			// Keep track of all nodes, not just input and output
			all_list.add(newnode);

			// Have the node specifier point to the node it generated
			(curnode).analogue = newnode;

		}

		// Create the links by iterating through the genes
		// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
		for (Gene curgene : genes) {
			// Only create the link if the gene is enabled
			if (((curgene).enable) == true) {
				curlink = (curgene).lnk;
				//inode = (curlink.in_node).analogue;
				//onode = (curlink.out_node).analogue;
				inode = curlink.in_node;
				onode = curlink.out_node;
				// NOTE: This line could be run through a recurrency check if
				// desired
				// (no need to in the current implementation of NEAT)
				newlink = new Link(curlink.weight, inode, onode,
						curlink.is_recurrent);
				if(onode != null || inode != null){
					if(onode.incoming == null || inode.outgoing == null)
						newlink = newlink;
					(onode.incoming).add(newlink);
					(inode.outgoing).add(newlink);
				}

				// Derive link's parameters from its Trait pointer
				curtrait = (curlink.linktrait);

				newlink.derive_trait(curtrait);

				// Keep track of maximum weight
				if (newlink.weight > 0)
					weight_mag = newlink.weight;
				else
					weight_mag = -newlink.weight;
				if (weight_mag > maxweight)
					maxweight = weight_mag;
			}
		}

		// Create the new network
		newnet = new Network(inlist, outlist, all_list, id);

		// Attach genotype and phenotype together
		newnet.genotype = this;
		phenotype = newnet;

		newnet.maxweight = maxweight;

		return newnet;

	}

	//
	// // Dump this genome to specified file
	// void print_to_file(std::ostream &outFile);
	// void print_to_file(std::ofstream &outFile);
	public void print_to_file(BufferedWriter outFile) {
		// std::vector<Trait*>::iterator curtrait;
		// std::vector<NNode*>::iterator curnode;
		// std::vector<Gene*>::iterator curgene;
		try {
			// outFile<<"genomestart "<<genome_id<<std::endl;
			outFile.write("genomestart " + genome_id + "\n");

			// Output the traits
			// for(curtrait=traits.begin();curtrait!=traits.end();++curtrait) {
			for (Trait curtrait : traits) {
				// (curtrait).trait_id=curtrait-traits.begin()+1;
				// (*curtrait)->print_to_file(outFile);
				curtrait.print_to_file(outFile);
			}

			// Output the nodes
			// for(curnode=nodes.begin();curnode!=nodes.end();++curnode) {
			for (Nnode curnode : nodes) {
				// (*curnode)->print_to_file(outFile);
				curnode.print_to_file(outFile);
			}

			// Output the genes
			// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
			for (Gene curgene : genes) {
				// (*curgene)->print_to_file(outFile);
				curgene.print_to_file(outFile);
			}

			// outFile<<"genomeend "<<genome_id<<std::endl;
			outFile.write("genomeend" + genome_id + "\n");
		} catch (Exception e) {
			System.out.println("Genome had trouble writing to field.");
			System.out.println(e.getMessage());
		}
		try {
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	// // Wrapper for print_to_file above
	// void print_to_filename(char *filename);
	public void print_to_filename(String filename) {
		// std::ofstream oFile(filename);
		// oFile.open(filename, std::ostream::Write);
		try {
			OutputStream is = new FileOutputStream("filename");
			BufferedWriter oFile = new BufferedWriter(
					new OutputStreamWriter(is));
			print_to_file(oFile);
			oFile.close();
		} catch (Exception e) {
			System.out.println("Trouble writing to output file in Genome");
			System.out.println(e.getMessage());
		}
	}

	//
	// // Duplicate this Genome to create a new one with the specified id
	// Genome *duplicate(int new_id);
	public Genome duplicate(int new_id) {
		// Collections for the new Genome
		Vector<Trait> traits_dup = new Vector<Trait>();
		Vector<Nnode> nodes_dup = new Vector<Nnode>();
		Vector<Gene> genes_dup = new Vector<Gene>();

		// Iterators for the old Genome
		// std::vector<Trait*>::iterator curtrait;
		// std::vector<NNode*>::iterator curnode;
		// std::vector<Gene*>::iterator curgene;

		// New item pointers
		Trait newtrait = null;
		Nnode newnode = null;
		Gene newgene = null;
		Trait assoc_trait = null; // Trait associated with current item

		Nnode inode = null; // For forming a gene
		Nnode onode = null; // For forming a gene
		Trait traitptr = null;

		Genome newgenome = null;

		// verify();

		// Duplicate the traits
		// for(curtrait=traits.begin();curtrait!=traits.end();++curtrait) {
		for (Trait curtrait : traits) {
			newtrait = new Trait(curtrait);
			traits_dup.add(newtrait);
		}

		// Duplicate NNodes
		// for(curnode=nodes.begin();curnode!=nodes.end();++curnode) {
		for (Nnode curnode : nodes) {
			// First, find the trait that this node points to
			if (((curnode).nodetrait) == null)
				assoc_trait = null;
			else {
				// curtrait=traits_dup.begin();
				// while(((*curtrait)->trait_id)!=(((*curnode)->nodetrait)->trait_id))
				// ++curtrait;
				// assoc_trait=(*curtrait);
				for (Trait curtrait : traits) {
					if (curtrait.trait_id != curnode.nodetrait.trait_id)
						continue;
					assoc_trait = new Trait(curtrait);
					break;
				}
			}

			newnode = new Nnode(curnode, assoc_trait);

			(curnode).dup = newnode; // Remember this node's old copy
			// (*curnode)->activation_count=55;
			nodes_dup.add(newnode);
		}

		// Duplicate Genes
		// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
		for (Gene curgene : genes) {
			// First find the nodes connected by the gene's link

			inode = (((curgene).lnk).in_node).dup;
			onode = (((curgene).lnk).out_node).dup;

			// Get a pointer to the trait expressed by this gene
			traitptr = ((curgene).lnk).linktrait;
			if (traitptr == null)
				assoc_trait = null;
			else {
				// curtrait=traits_dup.begin();
				// while(((*curtrait)->trait_id)!=(traitptr->trait_id))
				// ++curtrait;
				// assoc_trait=(*curtrait);
				for (Trait curtrait : traits) {
					if (curtrait.trait_id != traitptr.trait_id)
						continue;
					assoc_trait = new Trait(curtrait);
					break;
				}
			}

			newgene = new Gene(curgene, assoc_trait, inode, onode);
			genes_dup.add(newgene);

		}

		// Finally, return the genome
		newgenome = new Genome(new_id, traits_dup, nodes_dup, genes_dup); //null);

		return newgenome;

	}

	//
	// // For debugging: A number of tests can be run on a genome to check its
	// // integrity
	// // Note: Some of these tests do not indicate a bug, but rather are meant
	// // to be used to detect specific system states
	// bool verify();
	public boolean verify() {
		// std::vector<NNode*>::iterator curnode;
		// std::vector<Gene*>::iterator curgene;
		// std::vector<Gene*>::iterator curgene2;
		Nnode inode = null;
		Nnode onode = null;

		boolean disab = false;

		int last_id = 0;

		// int pause;

		// cout<<"Verifying Genome id: "<<this->genome_id<<endl;

		if (this == null) {
			// cout<<"ERROR GENOME EMPTY"<<endl;
			// cin>>pause;
		}

		// Check each gene's nodes
		// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
		for (Gene curgene : genes) {
			inode = ((curgene).lnk).in_node;
			onode = ((curgene).lnk).out_node;

			// Look for inode
			// curnode=nodes.begin();
			// while((curnode!=nodes.end())&&
			// ((*curnode)!=inode))
			// ++curnode;
			//
			// if (curnode==nodes.end()) {
			// //cout<<"MISSING iNODE FROM GENE NOT IN NODES OF GENOME!!"<<endl;
			// //cin>>pause;
			// return false;
			// }
			for (Nnode curnode : nodes) {
				if (curnode != nodes.lastElement() && curnode != inode)
					continue;
				if (curnode == nodes.lastElement()) {
					return false;
				}
			}

			// Look for onode
			// curnode=nodes.begin();
			// while((curnode!=nodes.end())&&
			// ((*curnode)!=onode))
			// ++curnode;
			//
			// if (curnode==nodes.end()) {
			// //cout<<"MISSING oNODE FROM GENE NOT IN NODES OF GENOME!!"<<endl;
			// //cin>>pause;
			// return false;
			// }
			for (Nnode curnode : nodes) {
				if (curnode != nodes.lastElement() && curnode != onode)
					continue;
				if (curnode == nodes.lastElement()) {
					return false;
				}
			}

		}

		// Check for NNodes being out of order
		last_id = 0;
		// for(curnode=nodes.begin();curnode!=nodes.end();++curnode) {
		for (Nnode curnode : nodes) {
			if ((curnode).node_id < last_id) {
				// cout<<"ALERT: NODES OUT OF ORDER in "<<this<<endl;
				// cin>>pause;
				return false;
			}

			last_id = (curnode).node_id;
		}

		// Make sure there are no duplicate genes
		// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
		for (Gene curgene : genes) {

			// for(curgene2=genes.begin();curgene2!=genes.end();++curgene2) {
			for (Gene curgene2 : genes) {
				if (((curgene) != (curgene2))
						&& ((((curgene).lnk).is_recurrent) == (((curgene2).lnk).is_recurrent))
						&& ((((((curgene2).lnk).in_node).node_id) == ((((curgene).lnk).in_node).node_id)) && (((((curgene2).lnk).out_node).node_id) == ((((curgene).lnk).out_node).node_id)))) {
					// cout<<"ALERT: DUPLICATE GENES: "<<(*curgene)<<(*curgene2)<<endl;
					// cout<<"INSIDE GENOME: "<<this<<endl;

					// cin>>pause;
				}

			}
		}

		// See if a gene is not disabled properly
		// Note this check does not necessary mean anything is wrong
		//
		// if (nodes.size()>=15) {
		// disab=false;
		// //Go through genes and see if one is disabled
		// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
		// if (((*curgene)->enable)==false) disab=true;
		// }

		// if (disab==false) {
		// cout<<"ALERT: NO DISABLED GENE IN GENOME: "<<this<<endl;
		// //cin>>pause;
		// }

		// }
		//

		// Check for 2 disables in a row
		// Note: Again, this is not necessarily a bad sign
		if (nodes.size() >= 500) {
			disab = false;
			// for(curgene=genes.begin();curgene!=genes.end();++curgene) {
			for (Gene curgene : genes) {
				if ((((curgene).enable) == false) && (disab == true)) {
					// cout<<"ALERT: 2 DISABLES IN A ROW: "<<this<<endl;
				}
				if (((curgene).enable) == false)
					disab = true;
				else
					disab = false;
			}
		}

		// cout<<"GENOME OK!"<<endl;

		return true;
	}

	//
	// // ******* MUTATORS *******
	//
	// // Perturb params in one trait
	// void mutate_random_trait();
	public void mutate_random_trait() {
		// std::vector<Trait*>::iterator thetrait; //Trait to be mutated
		int traitnum = 0;

		// Choose a random traitnum
		traitnum = Neat.randint(0, (traits.size()) - 1);

		// Retrieve the trait and mutate it
		// thetrait=traits.begin();
		// (*(thetrait[traitnum])).mutate();
		traits.setElementAt(new Trait(traits.get(traitnum)), traitnum);

		// TRACK INNOVATION? (future possibility)

	}

	//
	// // Change random link's trait. Repeat times times
	// void mutate_link_trait(int times);
	public void mutate_link_trait(int times) {
		int traitnum = 0;
		int genenum = 0;
		// std::vector<Gene*>::iterator thegene; //Link to be mutated
		// std::vector<Trait*>::iterator thetrait; //Trait to be attached
		int count = 0;
		int loop = 0;

		for (loop = 1; loop <= times; loop++) {

			// Choose a random traitnum
			int y = traits.size() -1;
			traitnum = Neat.randint(0, y);
			
			y = genes.size() -1;
			// Choose a random linknum
			//genenum = Neat.randint(0, y);
			genenum = (int)Math.random()*y;

			// set the link to point to the new trait
			// thegene=genes.begin();
			// for(count=0;count<genenum;count++)
			// ++thegene;
			Gene thegene = genes.get(genenum);

			// Do not alter frozen genes
			if (!((thegene).frozen)) {
				// thetrait=traits.begin();

				// ((*thegene)->lnk)->linktrait=thetrait[traitnum];
				thegene.lnk.linktrait = traits.get(traitnum);
				genes.set(genenum, thegene);

			}
			// TRACK INNOVATION- future use
			// (*thegene)->mutation_num+=randposneg()*randfloat()*linktrait_mut_sig;

		}
	}

	//
	// // Change random node's trait times times
	// void mutate_node_trait(int times);
	public void mutate_node_trait(int times) {
		int traitnum = 0;
		int nodenum = 0;
		// std::vector<NNode*>::iterator thenode; //Link to be mutated
		Nnode thenode = null;
		// std::vector<Gene*>::iterator thegene; //Gene to record innovation
		// std::vector<Trait*>::iterator thetrait; //Trait to be attached
		Trait thetrait = null;
		int count = 0;
		int loop = 0;

		for (loop = 1; loop <= times; loop++) {

			// Choose a random traitnum
			int y = traits.size() -1;
			traitnum = Neat.randint(0, y);//(traits.size()) - 1);
			
			y = nodes.size() -1;
			// Choose a random nodenum
			//nodenum = Neat.randint(0, y); //nodes.size() - 1);
			nodenum = (int)Math.random()*y;

			// set the link to point to the new trait
			// thenode=nodes.begin();
			// for(count=0;count<nodenum;count++)
			// ++thenode;
			thenode = nodes.get(nodenum);

			// Do not mutate frozen nodes
			if (!((thenode).frozen)) {

				// thetrait=traits.begin();
				thetrait = traits.get(traitnum);
				// (*thenode)->nodetrait=thetrait[traitnum];
				thenode.nodetrait = thetrait;
				nodes.set(nodenum, thenode);

			}
			// TRACK INNOVATION! - possible future use
			// for any gene involving the mutated node, perturb that gene's
			// mutation number
			// for(thegene=genes.begin();thegene!=genes.end();++thegene) {
			// if (((((*thegene)->lnk)->in_node)==(*thenode))
			// ||
			// ((((*thegene)->lnk)->out_node)==(*thenode)))
			// (*thegene)->mutation_num+=randposneg()*randfloat()*nodetrait_mut_sig;
			// }
		}
	}

	//
	// // Add Gaussian noise to linkweights either GAUSSIAN or COLDGAUSSIAN
	// (from zero)
	// void mutate_link_weights(double power,double rate,mutator mut_type);
	public void mutate_link_weights(double power, double rate, mutator mut_type) {
		// std::vector<Gene*>::iterator curgene;
		double num = 0.0; // counts gene placement
		double gene_total = 0.0;
		double powermod = 0.0; // Modified power by gene number
		// The power of mutation will rise farther into the genome
		// on the theory that the older genes are more fit since
		// they have stood the test of time

		double randnum = 0.0;
		double randchoice = 0.0; // Decide what kind of mutation to do on a gene
		double endpart = 0.0; // Signifies the last part of the genome
		double gausspoint = 0.0;
		double coldgausspoint = 0.0;

		boolean severe = false; // Once in a while really shake things up

		// Wright variables
		// double oldval;
		// double perturb;

		// --------------- WRIGHT'S MUTATION METHOD --------------

		// //Use the fact that we know ends are newest
		// gene_total=(double) genes.size();
		// endpart=gene_total*0.8;
		// num=0.0;

		// for(curgene=genes.begin();curgene!=genes.end();curgene++) {

		// //Mutate rate 0.2 controls how many params mutate in the list
		// if ((randfloat()<rate)||
		// ((gene_total>=10.0)&&(num>endpart))) {

		// oldval=((*curgene)->lnk)->weight;

		// //The amount to perturb the value by
		// perturb=randfloat()*power;

		// //Once in a while leave the end part alone
		// if (num>endpart)
		// if (randfloat()<0.2) perturb=0;

		// //Decide positive or negative
		// if (gRandGen.randI()%2) {
		// //Positive case

		// //if it goes over the max, find something smaller
		// if (oldval+perturb>100.0) {
		// perturb=(100.0-oldval)*randfloat();
		// }

		// ((*curgene)->lnk)->weight+=perturb;

		// }
		// else {
		// //Negative case

		// //if it goes below the min, find something smaller
		// if (oldval-perturb<100.0) {
		// perturb=(oldval+100.0)*randfloat();
		// }

		// ((*curgene)->lnk)->weight-=perturb;

		// }
		// }

		// num+=1.0;

		// }

		// ------------------------------------------------------

		if (Neat.randfloat() > 0.5)
			severe = true;
		else
			severe = false;

		// Go through all the Genes and perturb their link's weights
		num = 0.0;
		gene_total = (double) genes.size();
		endpart = gene_total * 0.8;
		// powermod=randposneg()*power*randfloat(); //Make power of mutation
		// random
		// powermod=randfloat();
		powermod = 1.0;

		// Possibility: Jiggle the newest gene randomly
		// if (gene_total>10.0) {
		// lastgene=genes.end();
		// lastgene--;
		// if (randfloat()>0.4)
		// ((*lastgene)->lnk)->weight+=0.5*randposneg()*randfloat();
		// }

		/*
		 * //KENHACK: NOTE THIS HAS BEEN MAJORLY ALTERED // THE LOOP BELOW IS
		 * THE ORIGINAL METHOD if (mut_type==COLDGAUSSIAN) {
		 * //printf("COLDGAUSSIAN");
		 * for(curgene=genes.begin();curgene!=genes.end();curgene++) { if
		 * (randfloat()<0.9) { randnum=randposneg()*randfloat()*power*powermod;
		 * ((*curgene)->lnk)->weight+=randnum; } } }
		 * 
		 * 
		 * for(curgene=genes.begin();curgene!=genes.end();curgene++) { if
		 * (randfloat()<0.2) { randnum=randposneg()*randfloat()*power*powermod;
		 * ((*curgene)->lnk)->weight+=randnum;
		 * 
		 * //Cap the weights at 20.0 (experimental) if
		 * (((*curgene)->lnk)->weight>1.0) ((*curgene)->lnk)->weight=1.0; else
		 * if (((*curgene)->lnk)->weight<-1.0) ((*curgene)->lnk)->weight=-1.0; }
		 * }
		 */

		// Loop on all genes (ORIGINAL METHOD)
		// for(curgene=genes.begin();curgene!=genes.end();curgene++) {
		for (Gene curgene : genes) {

			// Possibility: Have newer genes mutate with higher probability
			// Only make mutation power vary along genome if it's big enough
			// if (gene_total>=10.0) {
			// This causes the mutation power to go up towards the end up the
			// genome
			// powermod=((power-0.7)/gene_total)*num+0.7;
			// }
			// else powermod=power;

			// The following if determines the probabilities of doing cold
			// gaussian
			// mutation, meaning the probability of replacing a link weight with
			// another, entirely random weight. It is meant to bias such
			// mutations
			// to the tail of a genome, because that is where less time-tested
			// genes
			// reside. The gausspoint and coldgausspoint represent values above
			// which a random float will signify that kind of mutation.

			// Don't mutate weights of frozen links
			if (!((curgene).frozen)) {

				if (severe) {
					gausspoint = 0.3;
					coldgausspoint = 0.1;
				} else if ((gene_total >= 10.0) && (num > endpart)) {
					gausspoint = 0.5; // Mutate by modification % of connections
					coldgausspoint = 0.3; // Mutate the rest by replacement % of
											// the time
				} else {
					// Half the time don't do any cold mutations
					if (Neat.randfloat() > 0.5) {
						gausspoint = 1.0 - rate;
						coldgausspoint = 1.0 - rate - 0.1;
					} else {
						gausspoint = 1.0 - rate;
						coldgausspoint = 1.0 - rate;
					}
				}

				// Possible methods of setting the perturbation:
				// randnum=gaussrand()*powermod;
				// randnum=gaussrand();

				randnum = Neat.randposneg() * Neat.randfloat() * power
						* powermod;
				// std::cout << "RANDOM: " << randnum << " " << randposneg() <<
				// " " << randfloat() << " " << power << " " << powermod <<
				// std::endl;
				if (mut_type == mutator.GAUSSIAN) {
					randchoice = Neat.randfloat();
					if (randchoice > gausspoint)
						((curgene).lnk).weight += randnum;
					else if (randchoice > coldgausspoint)
						((curgene).lnk).weight = randnum;
				} else if (mut_type == mutator.COLDGAUSSIAN)
					((curgene).lnk).weight = randnum;

				// Cap the weights at 8.0 (experimental)
				if (((curgene).lnk).weight > 8.0)
					((curgene).lnk).weight = 8.0;
				else if (((curgene).lnk).weight < -8.0)
					((curgene).lnk).weight = -8.0;

				// Record the innovation
				// (*curgene)->mutation_num+=randnum;
				(curgene).mutation_num = ((curgene).lnk).weight;

				num += 1.0;

			}

		} // end for loop

	}

	//
	// // toggle genes on or off
	// void mutate_toggle_enable(int times);
	public void mutate_toggle_enable(int times) {
		int genenum = 0;
		int count = 0;
		// std::vector<Gene*>::iterator thegene; //Gene to toggle
		// std::vector<Gene*>::iterator checkgene; //Gene to check
		int genecount;

		for (count = 1; count <= times; count++) {

			// Choose a random genenum
			int y = genes.size() -1;
			genenum = Neat.randint(0, y); //genes.size() - 1);

			// find the gene
			// thegene=genes.begin();
			// for(genecount=0;genecount<genenum;genecount++)
			// ++thegene;
			Gene thegene = genes.get(genenum);

			// Toggle the enable on this gene
			if (((thegene).enable) == true) {
				// We need to make sure that another gene connects out of the
				// in-node
				// Because if not a section of network will break off and become
				// isolated
				// checkgene=genes.begin();
				// while((checkgene!=genes.end())&&
				// (((((*checkgene)->lnk)->in_node)!=(((*thegene)->lnk)->in_node))||
				// (((*checkgene)->enable)==false)||
				// ((*checkgene)->innovation_num==(*thegene)->innovation_num)))
				// ++checkgene;
				for (Gene checkgene : genes) {
					if ((checkgene != genes.lastElement())
							&& (((((checkgene).lnk).in_node) != (((thegene).lnk).in_node))
									|| (((checkgene).enable) == false) || ((checkgene).innovation_num == (thegene).innovation_num)))
						continue;
					// Disable the gene if it's safe to do so
					if (checkgene != genes.lastElement())
						(thegene).enable = false;

				}

			} else
				(thegene).enable = true;
			genes.set(genenum, thegene);
		}
	}

	//
	// // Find first disabled gene and enable it
	// void mutate_gene_reenable();
	void mutate_gene_reenable() {
		// std::vector<Gene*>::iterator thegene; //Gene to enable

		// thegene=genes.begin();

		// Search for a disabled gene
		// while((thegene!=genes.end())&&((*thegene)->enable==true))
		// ++thegene;
		for (Gene curgene : genes) {
			if (curgene.enable)
				continue;
			curgene.enable = true;
			break;
		}

		// Reenable it
		// if (thegene!=genes.end())
		// if (((*thegene)->enable)==false) (*thegene)->enable=true;

	}

	//
	// // These last kinds of mutations return false if they fail
	// // They can fail under certain conditions, being unable
	// // to find a suitable place to make the mutation.
	// // Generally, if they fail, they can be called again if desired.
	//
	// // Mutate genome by adding a node respresentation
	// bool mutate_add_node(std::vector<Innovation*> &innovs,int
	// &curnode_id,double &curinnov);
	public boolean mutate_add_node(Vector<Innovation> innovs, int curnode_id,
			double curinnov) {
		// std::vector<Gene*>::iterator thegene; //random gene containing the
		// original link
		Gene thegene = null;
		int genenum = 0; // The random gene number
		Nnode in_node = null; // Here are the nodes connected by the gene
		Nnode out_node = null;
		Link thelink = null; // The link inside the random gene

		// double randmult; //using a gaussian to find the random gene

		// std::vector<Innovation*>::iterator theinnov; //For finding a
		// historical match
		boolean done = false;

		Gene newgene1 = null; // The new Genes
		Gene newgene2 = null;
		Nnode newnode = null; // The new NNode
		Trait traitptr = null; // The original link's trait

		// double splitweight; //If used, Set to sqrt(oldweight of oldlink)
		double oldweight = 0.0; // The weight of the original link

		int trycount = 0; // Take a few tries to find an open node
		boolean found = false;

		// First, find a random gene already in the genome
		trycount = 0;

		// Split next link with a bias towards older links
		// NOTE: 7/2/01 - for robots, went back to random split
		// because of large # of inputs
		if (found) {
			// thegene=genes.begin();
			// while (((thegene!=genes.end())
			// &&(!((*thegene)->enable)))||
			// ((thegene!=genes.end())
			// &&(((*thegene)->lnk->in_node)->gen_node_label==BIAS)))
			for (Gene agene : genes) {
				if (agene.enable) {
					thegene = agene;
					break;
				}
				if ((agene.lnk.in_node.gen_node_label != Nnode.nodeplace.BIAS)) {
					thegene = agene;
					break;
				}
				// if (thegene == genes.lastElement()) thegene = null;
				// ++thegene;
			}

			// Now randomize which node is chosen at this point
			// We bias the search towards older genes because
			// this encourages splitting to distribute evenly
			// while (((thegene!=genes.end())&&
			for (Gene agene : genes) {
				// (randfloat()<0.3))||
				if (Neat.randfloat() >= 0.3) {
					thegene = agene;
					break;
				}
				// ((thegene!=genes.end())
				// &&(((*thegene)->lnk->in_node)->gen_node_label==BIAS)))
				if (thegene.lnk.in_node.gen_node_label != Nnode.nodeplace.BIAS) {
					thegene = agene;
					break;
				}

				// ++thegene;

				// if ((!(thegene==genes.end()))&&
				// ((*thegene)->enable))
				if (thegene != null) {
					if (thegene.enable) {
						found = true;
					}
				}
			}// end for loop
		} // end if(false)
			// In this else:
			// Alternative random gaussian choice of genes NOT USED in this
			// version of NEAT
			// NOTE: 7/2/01 now we use this after all
		else {
			while ((trycount < 20) && (!found)) {

				// Choose a random genenum
				// randmult=gaussrand()/4;
				// if (randmult>1.0) randmult=1.0;

				// This tends to select older genes for splitting
				// genenum=(int) floor((randmult*(genes.size()-1.0))+0.5);

				// This old totally random selection is bad- splitting
				// inside something recently splitted adds little power
				// to the system (should use a gaussian if doing it this way)
				int y = genes.size() -1;
				genenum = Neat.randint(0, y); // genes.size() - 1);

				// find the gene
				// thegene=genes.begin();
				for (int genecount = 0; genecount < genenum; genecount++)
					thegene = genes.get(genecount);
				// ++thegene;

				// If either the gene is disabled, or it has a bias input, try
				// again
				if (!thegene.enable == false || thegene.lnk.in_node.gen_node_label == Nnode.nodeplace.BIAS)
					found = true;

				++trycount;

			}
		}

		// If we couldn't find anything so say goodbye
		if (!found)
			return false;

		// Disabled the gene
		(thegene).enable = false;

		// Extract the link
		thelink = (thegene).lnk;
		oldweight = (thegene).lnk.weight;

		// Extract the nodes
		in_node = thelink.in_node;
		out_node = thelink.out_node;

		// Check to see if this innovation has already been done
		// in another genome
		// Innovations are used to make sure the same innovation in
		// two separate genomes in the same generation receives
		// the same innovation number.
		// theinnov=innovs.begin();

		// while(!done) {
		for (Innovation theinnov : innovs) {

			if (theinnov == innovs.lastElement()) {

				// The innovation is totally novel

				// Get the old link's trait
				traitptr = thelink.linktrait;

				// Create the new NNode
				// By convention, it will point to the first trait
				newnode = new Nnode(Nnode.nodetype.NEURON, curnode_id++,
						Nnode.nodeplace.HIDDEN);
				newnode.nodetrait = ((traits.firstElement()));

				// Create the new Genes
				if (thelink.is_recurrent) {
					newgene1 = new Gene(traitptr, 1.0, in_node, newnode, true,
							curinnov, 0);
					newgene2 = new Gene(traitptr, oldweight, newnode, out_node,
							false, curinnov + 1, 0);
					curinnov += 2.0;
				} else {
					newgene1 = new Gene(traitptr, 1.0, in_node, newnode, false,
							curinnov, 0);
					newgene2 = new Gene(traitptr, oldweight, newnode, out_node,
							false, curinnov + 1, 0);
					curinnov += 2.0;
				}

				// Add the innovations (remember what was done)
				innovs.add(new Innovation(in_node.node_id, out_node.node_id,
						curinnov - 2.0, curinnov - 1.0, newnode.node_id,
						(thegene).innovation_num));

				done = true;
			}

			// We check to see if an innovation already occured that was:
			// -A new node
			// -Stuck between the same nodes as were chosen for this mutation
			// -Splitting the same gene as chosen for this mutation
			// If so, we know this mutation is not a novel innovation
			// in this generation
			// so we make it match the original, identical mutation which
			// occured
			// elsewhere in the population by coincidence
			else if (((theinnov).innovation_type == Innovation.innovtype.NEWNODE)
					&& ((theinnov).node_in_id == (in_node.node_id))
					&& ((theinnov).node_out_id == (out_node.node_id))
					&& ((theinnov).old_innov_num == (thegene).innovation_num)) {

				// Here, the innovation has been done before

				// Get the old link's trait
				traitptr = thelink.linktrait;

				// Create the new NNode
				newnode = new Nnode(Nnode.nodetype.NEURON,
						(theinnov).newnode_id, Nnode.nodeplace.HIDDEN);
				// By convention, it will point to the first trait
				// Note: In future may want to change this
				newnode.nodetrait = ((traits.firstElement()));

				// Create the new Genes
				if (thelink.is_recurrent) {
					newgene1 = new Gene(traitptr, 1.0, in_node, newnode, true,
							(theinnov).innovation_num1, 0);
					newgene2 = new Gene(traitptr, oldweight, newnode, out_node,
							false, (theinnov).innovation_num2, 0);
				} else {
					newgene1 = new Gene(traitptr, 1.0, in_node, newnode, false,
							(theinnov).innovation_num1, 0);
					newgene2 = new Gene(traitptr, oldweight, newnode, out_node,
							false, (theinnov).innovation_num2, 0);
				}

				done = true;
			}
			// else ++theinnov;
		} // end for loop

		// Now add the new NNode and new Genes to the Genome
		// genes.push_back(newgene1); //Old way to add genes- may result in
		// genes becoming out of order
		// genes.push_back(newgene2);
		// add_gene(genes,newgene1); //Add genes in correct order
		// add_gene(genes,newgene2);
		// node_insert(nodes,newnode);
		genes.add(newgene1);
		genes.add(newgene2);
		nodes.add(newnode);

		return true;

	}

	//
	// // Mutate the genome by adding a new link between 2 random NNodes
	// bool mutate_add_link(std::vector<Innovation*> &innovs,double
	// &curinnov,int tries);
	boolean mutate_add_link(Vector<Innovation> innovs, double curinnov,
			int tries) {

		int nodenum1, nodenum2; // Random node numbers
		// std::vector<NNode*>::iterator thenode1,thenode2; //Random node
		// iterators
		Nnode thenode1 = null, thenode2 = null;
		int nodecount; // Counter for finding nodes
		int trycount; // Iterates over attempts to find an unconnected pair of
						// nodes
		Nnode nodep1 = null; // Pointers to the nodes
		Nnode nodep2 = null; // Pointers to the nodes
		// Vector<Gene>::iterator thegene; //Searches for existing link
		boolean found = false; // Tells whether an open pair was found
		// std::vector<Innovation*>::iterator theinnov; //For finding a
		// historical match
		boolean recurflag = false; // Indicates whether proposed link is
									// recurrent
		Gene newgene = null; // The new Gene

		int traitnum; // Random trait finder
		// std::vector<Trait*>::iterator thetrait;

		double newweight; // The new weight for the new link

		boolean done;
		boolean do_recur;
		boolean loop_recur;
		int first_nonsensor;

		// These are used to avoid getting stuck in an infinite loop checking
		// for recursion
		// Note that we check for recursion to control the frequency of
		// adding recurrent links rather than to prevent any paricular
		// kind of error
		int thresh = (nodes.size()) * (nodes.size());
		int count = 0;

		// Make attempts to find an unconnected pair
		trycount = 0;

		// Decide whether to make this recurrent
		if (Neat.randfloat() < Neat.recur_only_prob)
			do_recur = true;
		else
			do_recur = false;

		// Find the first non-sensor so that the to-node won't look at sensors
		// as
		// possible destinations
		first_nonsensor = 0;
		// thenode1=nodes.begin();
		// while(((thenode1).get_type())==SENSOR) {
		for (Nnode anode1 : nodes) {
			if (thenode1.get_type() != Nnode.nodetype.SENSOR)
				break;
			first_nonsensor++;
			// ++thenode1;
		}

		// Here is the recurrent finder loop- it is done separately
		if (do_recur) {

			while (trycount < tries) {

				// Some of the time try to make a recur loop
				if (Neat.randfloat() > 0.5) {
					loop_recur = true;
				} else
					loop_recur = false;

				if (loop_recur) {
					nodenum1 = Neat.randint(first_nonsensor, nodes.size() - 1);
					nodenum2 = nodenum1;
				} else {
					// Choose random nodenums
					nodenum1 = Neat.randint(0, nodes.size() - 1);
					nodenum2 = Neat.randint(first_nonsensor, nodes.size() - 1);
				}

				// Find the first node
				// thenode1=nodes.begin();
				// for(nodecount=0;nodecount<nodenum1;nodecount++)
				// ++thenode1;
				for (nodecount = 0; nodecount < nodenum1; nodecount++)
					thenode1 = nodes.get(nodecount);

				// Find the second node
				// thenode2=nodes.begin();
				// for(nodecount=0;nodecount<nodenum2;nodecount++)
				// ++thenode2;
				for (nodecount = 0; nodecount < nodenum2; nodecount++)
					thenode2 = nodes.get(nodecount);

				nodep1 = (thenode1);
				nodep2 = (thenode2);

				// See if a recur link already exists ALSO STOP AT END OF
				// GENES!!!!
				Iterator<Gene> thegeneIter = genes.iterator();
				Gene thegene = thegeneIter.next();
				while ((thegene != null)
						&& ((nodep2.type) != Nnode.nodetype.SENSOR) && // Don't
																		// allow
																		// SENSORS
																		// to
																		// get
																		// input
						(!((((thegene).lnk).in_node == nodep1)
								&& (((thegene).lnk).out_node == nodep2) && ((thegene).lnk).is_recurrent))) {
					thegene = thegeneIter.next();
				}

				if (thegene == genes.lastElement())
					trycount++;
				else {
					count = 0;
					recurflag = phenotype.is_recur(nodep1.analogue,
							nodep2.analogue, count, thresh);

					// ADDED: CONSIDER connections out of outputs recurrent
					// REMVED: Can't compare nodetype to nodeplace
					// if (((nodep1.type)==Nnode.nodeplace.OUTPUT)||
					// ((nodep2.type)==Nnode.nodeplace.OUTPUT))
					// recurflag=true;

					// Exit if the network is faulty (contains an infinite loop)
					// NOTE: A loop doesn't really matter
					// if (count>thresh) {
					// cout<<"LOOP DETECTED DURING A RECURRENCY CHECK"<<std::endl;
					// return false;
					// }

					// Make sure it finds the right kind of link (recur)
					if (!(recurflag))
						trycount++;
					else {
						trycount = tries;
						found = true;
					}

				}

			}
		} else {
			// Loop to find a nonrecurrent link
			while (trycount < tries) {

				// cout<<"TRY "<<trycount<<std::endl;

				// Choose random nodenums
				int y = genes.size() -1;
				nodenum1 = Neat.randint(0, y); //nodes.size() - 1);
				nodenum2 = Neat.randint(first_nonsensor, y);

				// Find the first node
				// thenode1=nodes.begin();
				// for(nodecount=0;nodecount<nodenum1;nodecount++)
				// ++thenode1;
				for (nodecount = 0; nodecount < nodenum1; nodecount++)
					thenode1 = nodes.get(nodecount);

				// cout<<"RETRIEVED NODE# "<<(*thenode1)->node_id<<std::endl;

				// Find the second node
				// thenode2=nodes.begin();
				// for(nodecount=0;nodecount<nodenum2;nodecount++)
				// ++thenode2;
				for (nodecount = 0; nodecount < nodenum2; nodecount++)
					thenode2 = nodes.get(nodecount);

				nodep1 = (thenode1);
				nodep2 = (thenode2);

				// See if a link already exists ALSO STOP AT END OF GENES!!!!
				Iterator<Gene> thegeneIter = genes.iterator();
				Gene thegene = thegeneIter.next();
				while ((thegene != genes.lastElement())
						&& ((nodep2.type) != Nnode.nodetype.SENSOR) && // Don't
																		// allow
																		// SENSORS
																		// to
																		// get
																		// input
						(!((((thegene).lnk).in_node == nodep1)
								&& (((thegene).lnk).out_node == nodep2) && (!(((thegene).lnk).is_recurrent))))) {
					thegene = thegeneIter.next();
				}

				if (thegene != genes.lastElement())
					trycount++;
				else {

					count = 0;
					recurflag = phenotype.is_recur(nodep1.analogue,
							nodep2.analogue, count, thresh);

					// ADDED: CONSIDER connections out of outputs recurrent
					// REMOVED: Why is it comparing nodetype to nodeplace???
					// if (((nodep1->type)==OUTPUT)||
					// ((nodep2->type)==OUTPUT))
					// recurflag=true;

					// Exit if the network is faulty (contains an infinite loop)
					if (count > thresh) {
						// cout<<"LOOP DETECTED DURING A RECURRENCY CHECK"<<std::endl;
						// return false;
					}

					// Make sure it finds the right kind of link (recur or not)
					if (recurflag)
						trycount++;
					else {
						trycount = tries;
						found = true;
					}

				}

			} // End of normal link finding loop
		}

		// Continue only if an open link was found
		if (found) {

			// Check to see if this innovation already occured in the population
			Iterator<Innovation> theinnovIter = innovs.iterator();
			Innovation theinnov = theinnovIter.next();

			// If it was supposed to be recurrent, make sure it gets labeled
			// that way
			if (do_recur)
				recurflag = true;

			done = false;

			while (!done) {

				// The innovation is totally novel
				if (theinnov == innovs.lastElement()) {

					// If the phenotype does not exist, exit on false,print
					// error
					// Note: This should never happen- if it does there is a bug
					if (phenotype == null) {
						// cout<<"ERROR: Attempt to add link to genome with no phenotype"<<std::endl;
						return false;
					}

					// Useful for debugging
					// cout<<"nodep1 id: "<<nodep1->node_id<<std::endl;
					// cout<<"nodep1: "<<nodep1<<std::endl;
					// cout<<"nodep1 analogue: "<<nodep1->analogue<<std::endl;
					// cout<<"nodep2 id: "<<nodep2->node_id<<std::endl;
					// cout<<"nodep2: "<<nodep2<<std::endl;
					// cout<<"nodep2 analogue: "<<nodep2->analogue<<std::endl;
					// cout<<"recurflag: "<<recurflag<<std::endl;

					// NOTE: Something like this could be used for time delays,
					// which are not yet supported. However, this does not
					// have an application with recurrency.
					// If not recurrent, randomize recurrency
					// if (!recurflag)
					// if (randfloat()<recur_prob) recurflag=1;

					// Choose a random trait
					int y = traits.size() -1 ;
					traitnum = Neat.randint(0, y); //(traits.size()) - 1);
					Iterator<Trait> thetraitIter = traits.iterator();
					Trait thetrait = thetraitIter.next();

					// Choose the new weight
					// newweight=(gaussrand())/1.5; //Could use a gaussian
					newweight = Neat.randposneg() * Neat.randfloat() * 1.0; // used
																			// to
																			// be
																			// 10.0

					// Create the new gene
					newgene = new Gene(((traits.get(traitnum))), newweight,
							nodep1, nodep2, recurflag, curinnov, newweight);

					// Add the innovation
					innovs.add(new Innovation(nodep1.node_id, nodep2.node_id,
							curinnov, newweight, traitnum));

					curinnov = curinnov + 1.0;

					done = true;
				}
				// OTHERWISE, match the innovation in the innovs list
				else if (((theinnov).innovation_type == Innovation.innovtype.NEWLINK)
						&& ((theinnov).node_in_id == (nodep1.node_id))
						&& ((theinnov).node_out_id == (nodep2.node_id))
						&& ((theinnov).recur_flag == recurflag)) {

					Iterator<Trait> thetraitIter = traits.iterator();
					Trait thetrait = thetraitIter.next();

					// Create new gene
					newgene = new Gene(((traits.get((theinnov).new_traitnum))),
							(theinnov).new_weight, nodep1, nodep2, recurflag,
							(theinnov).innovation_num1, 0);

					done = true;

				} else {
					// Keep looking for a matching innovation from this
					// generation
					theinnov = theinnovIter.next();
				}
			}

			// Now add the new Genes to the Genome
			// genes.push_back(newgene); //Old way - could result in
			// out-of-order innovation numbers in rtNEAT
			// add_gene(genes,newgene); //Adds the gene in correct order
			genes.add(newgene);

			return true;
		} else {
			return false;
		}

	}

	public double compatibility(Genome g) {
		//iterators for moving through the two potential parents' Genes
		Gene p1gene = null;
		Gene p2gene = null;

		//Innovation numbers
		double p1innov = 0.0;
		double p2innov = 0.0;

		//Intermediate value
		double mut_diff = 0.0;

		//Set up the counters
		double num_disjoint = 0.0;
		double num_excess = 0.0;
		double mut_diff_total = 0.0;
		double num_matching = 0.0;  //Used to normalize mutation_num differences

		double max_genome_size = 0.0; //Size of larger Genome

		//Get the length of the longest Genome for percentage computations
		if (genes.size()<(g.genes).size()) 
			max_genome_size=(g.genes).size();
		else max_genome_size=genes.size();

		//Now move through the Genes of each potential parent 
		//until both Genomes end
		p1gene=genes.get(0);
		p2gene=(g.genes).get(0);
		
		int k =0, l = 0;
		while(!((p1gene==genes.lastElement())&&
			(p2gene==g.genes.lastElement()))) {
				p1gene=genes.get(k);
				p2gene=(g.genes).get(l);

				if (p1gene==genes.lastElement()) {
					++l;
					num_excess+=1.0;
				}
				else if (p2gene==g.genes.lastElement()) {
					++k;
					num_excess+=1.0;
				}
				else {
					//Extract current innovation numbers
					p1innov=p1gene.innovation_num;
					p2innov=p2gene.innovation_num;

					if (p1innov==p2innov) {
						num_matching+=1.0;
						mut_diff=(p1gene.mutation_num)-(p2gene.mutation_num);
						if (mut_diff<0.0) mut_diff=0.0-mut_diff;
						//mut_diff+=trait_compare((*p1gene)->lnk->linktrait,(*p2gene)->lnk->linktrait); //CONSIDER TRAIT DIFFERENCES
						mut_diff_total+=mut_diff;

						++k;
						++l;
					}
					else if (p1innov<p2innov) {
						++k;
						num_disjoint+=1.0;
					}
					else if (p2innov<p1innov) {
						++l;
						num_disjoint+=1.0;
					}
				}
			} //End while

			//Return the compatibility number using compatibility formula
			//Note that mut_diff_total/num_matching gives the AVERAGE
			//difference between mutation_nums for any two matching Genes
			//in the Genome

			//Normalizing for genome size
			//return (disjoint_coeff*(num_disjoint/max_genome_size)+
			//  excess_coeff*(num_excess/max_genome_size)+
			//  mutdiff_coeff*(mut_diff_total/num_matching));


			//Look at disjointedness and excess in the absolute (ignoring size)

			//cout<<"COMPAT: size = "<<max_genome_size<<" disjoint = "<<num_disjoint<<" excess = "<<num_excess<<" diff = "<<mut_diff_total<<"  TOTAL = "<<(disjoint_coeff*(num_disjoint/1.0)+excess_coeff*(num_excess/1.0)+mutdiff_coeff*(mut_diff_total/num_matching))<<std::endl;

			return (Neat.disjoint_coeff*(num_disjoint/1.0)+
				Neat.excess_coeff*(num_excess/1.0)+
				Neat.mutdiff_coeff*(mut_diff_total/num_matching));
	}

	public Genome mate_multipoint(Genome g, int genomeid, double fitness1, double fitness2, boolean interspec_flag) {
		//The baby Genome will contain these new Traits, NNodes, and Genes
		//std::vector<Trait*> newtraits; 
		//std::vector<NNode*> newnodes;   
		//std::vector<Gene*> newgenes;    
		//Genome *new_genome;
		
		Vector<Trait> newtraits = new Vector<Trait>();
		Vector<Nnode> newnodes = new Vector<Nnode>();
		Vector<Gene> newgenes = new Vector<Gene>();
		Genome new_genome;

		//std::vector<Gene*>::iterator curgene2;  //Checks for link duplication
		Vector<Gene> curgene2 = new Vector<Gene>();

		//iterators for moving through the two parents' traits
		//std::vector<Trait*>::iterator p1trait;
		//std::vector<Trait*>::iterator p2trait;
		//Trait *newtrait;
		Trait p1trait; // = new Vector<Trait>();
		Trait p2trait; // = new Vector<Trait>();
		Trait newtrait;

		//iterators for moving through the two parents' genes
		//std::vector<Gene*>::iterator p1gene;
		//std::vector<Gene*>::iterator p2gene;
		Gene p1gene; // = new Vector<Gene>();
		Vector<Gene> p2gene = new Vector<Gene>();
		double p1innov;  //Innovation numbers for genes inside parents' Genomes
		double p2innov;
		//Gene *chosengene;  //Gene chosen for baby to inherit
		Gene chosengene = null;
		int traitnum;  //Number of trait new gene points to
		Nnode inode;  //NNodes connected to the chosen Gene
		Nnode onode;
		Nnode new_inode;
		Nnode new_onode;
		//std::vector<NNode*>::iterator curnode;  //For checking if NNodes exist already 
		Vector<Nnode> curnode = new Vector<Nnode>();
		int nodetraitnum = 0;  //Trait number for a NNode

		boolean disable;  //Set to true if we want to disabled a chosen gene

		disable = false;
		Gene newgene;

		boolean p1better; //Tells if the first genome (this one) has better fitness or not

		boolean skip;

		//First, average the Traits from the 2 parents to form the baby's Traits
		//It is assumed that trait lists are the same length
		//In the future, may decide on a different method for trait mating
		p2trait = g.traits.get(0);
		/*for(p1trait = traits.get(0); p1trait!=traits.lastElement(); p1trait.) {
			newtrait = new Trait(p1trait, p2trait);  //Construct by averaging
			newtraits.push_back(newtrait);
			p2trait = g.traits.get(1);
		}*/
		for(int i =0; i < traits.size(); i++ ){
			newtrait = new Trait(traits.get(i), p2trait);
			newtraits.add(newtrait);
			p2trait = g.traits.get(i+1);
		}

		//Figure out which genome is better
		//The worse genome should not be allowed to add extra structural baggage
		//If they are the same, use the smaller one's disjoint and excess genes only
		if (fitness1 > fitness2) 
			p1better = true;
		else if (fitness1 == fitness2) {
			if (genes.size() < (g.genes.size()))
				p1better = true;
			else p1better = false;
		}
		else 
			p1better = false;

		//NEW 3/17/03 Make sure all sensors and outputs are included
		/*for(curnode=(g->nodes).begin();curnode!=(g->nodes).end();++curnode) {
			if ((((*curnode)->gen_node_label)==INPUT)||
				(((*curnode)->gen_node_label)==BIAS)||
				(((*curnode)->gen_node_label)==OUTPUT)) {
					if (!((*curnode)->nodetrait)) nodetraitnum=0;
					else
						nodetraitnum=(((*curnode)->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;

					//Create a new node off the sensor or output
					new_onode=new NNode((*curnode),newtraits[nodetraitnum]);

					//Add the new node
					node_insert(newnodes,new_onode);

				}

		}*/
		for(int i=0; i<g.nodes.size(); i++){
			if(g.nodes.get(i).gen_node_label == nodeplace.INPUT || 
			g.nodes.get(i).gen_node_label == nodeplace.BIAS ||
			g.nodes.get(i).gen_node_label == nodeplace.OUTPUT){
				if(curnode.get(i).nodetrait == null) nodetraitnum = 0;
				else
					nodetraitnum = g.nodes.get(i).nodetrait.trait_id - traits.get(0).trait_id;
				//Create a new node off the sensor or output
				new_onode = new Nnode(g.nodes.get(i), newtraits.get(nodetraitnum));
				
				//Add the new node
				node_insert(newnodes, new_onode);
			}
		}

		//Now move through the Genes of each parent until both genomes end
		//p1gene = genes.get(0);
		//p2gene = g.genes.get(0); //(g->genes).begin();
		int p1i = 0;
		int p2i = 0;
		//while(!((p1gene==genes.end())&&
		//	(p2gene==(g->genes).end()))) {
		
		while(!(p1i == genes.size() && p2i == g.genes.size())){//)p1gene < genes.size() && p2gene < g.genes.size()){
				skip = false;  //Default to not skipping a chosen gene
				
				if(p1i == genes.size()){
					chosengene = g.genes.get(p2i);
					p2i++;
					if (p1better) skip=true;
				}
				else if(p2i == g.genes.size()){
					chosengene = genes.get(p1i);
					p1i++;
					if (!p1better) skip=true;
				}
				else {
					p1innov=genes.get(p1i).innovation_num;
					p2innov=g.genes.get(p2i).innovation_num;
					
					if (p1innov==p2innov) {
						if (Math.random()<0.5) {
							chosengene= genes.get(p1i);
						}
						else {
							chosengene=g.genes.get(p2i);
						}

						//If one is disabled, the corresponding gene in the offspring
						//will likely be disabled
						if (((genes.get(p1i).enable)==false)||
							((g.genes.get(p2i).enable)==false)) 
							if (Math.random()<0.75) disable=true;

						++p1i;
						++p2i;
					}
					else if (p1innov<p2innov) {
						chosengene=genes.get(p1i);
						++p1i;

						if (!p1better) skip=true;

					}
					else if (p2innov<p1innov) {
						chosengene=g.genes.get(p2i);
						++p2i;
						if (p1better) skip=true;
					}
				}
				/*if (p1gene==genes.end()) {
					chosengene=*p2gene;
					++p2gene;
					if (p1better) skip=true;  //Skip excess from the worse genome
				}
				else if (p2gene==(g->genes).end()) {
					chosengene=*p1gene;
					++p1gene;
					if (!p1better) skip=true; //Skip excess from the worse genome
				}
				else {
					//Extract current innovation numbers
					p1innov=(*p1gene)->innovation_num;
					p2innov=(*p2gene)->innovation_num;

					if (p1innov==p2innov) {
						if (randfloat()<0.5) {
							chosengene=*p1gene;
						}
						else {
							chosengene=*p2gene;
						}

						//If one is disabled, the corresponding gene in the offspring
						//will likely be disabled
						if ((((*p1gene)->enable)==false)||
							(((*p2gene)->enable)==false)) 
							if (randfloat()<0.75) disable=true;

						++p1gene;
						++p2gene;

					}
					else if (p1innov<p2innov) {
						chosengene=*p1gene;
						++p1gene;

						if (!p1better) skip=true;

					}
					else if (p2innov<p1innov) {
						chosengene=*p2gene;
						++p2gene;
						if (p1better) skip=true;
					}
				}*/

				/*
				//Uncomment this line to let growth go faster (from both parents excesses)
				skip=false;

				//For interspecies mating, allow all genes through:
				if (interspec_flag)
					skip=false;
				*/

				//Check to see if the chosengene conflicts with an already chosen gene
				//i.e. do they represent the same link    
				//curgene2 = newgenes.get(0);
				int curgene2i = 0;
				while (!(curgene2i == newgenes.size()) &&
					(!((newgenes.get(curgene2i).lnk.in_node.node_id)==(chosengene.lnk.in_node.node_id)))&&
					(newgenes.get(curgene2i).lnk.out_node.node_id)==(chosengene.lnk.out_node.node_id)&&((newgenes.get(curgene2i).lnk.is_recurrent)== chosengene.lnk.is_recurrent)&&
					(!((newgenes.get(curgene2i).lnk.in_node.node_id)== chosengene.lnk.out_node.node_id))&&
					((newgenes.get(curgene2i).lnk.out_node.node_id)== chosengene.lnk.in_node.node_id)&&
					(!newgenes.get(curgene2i).lnk.is_recurrent)&&
					(!(newgenes.get(curgene2i).lnk.is_recurrent)))
				{	
					++curgene2i;
				}

				if (curgene2i == newgenes.size()) skip=true;  //Links conflicts, abort adding

				if (!skip) {

					//Now add the chosengene to the baby

					//First, get the trait pointer
					if ((newgenes.get(curgene2i).lnk.linktrait == null)) traitnum = traits.get(0).trait_id - 1; 
					else
						traitnum = chosengene.lnk.linktrait.trait_id - traits.get(0).trait_id;  //The subtracted number normalizes depending on whether traits start counting at 1 or 0

					//Next check for the nodes, add them if not in the baby Genome already
					inode = chosengene.lnk.in_node;
					onode = chosengene.lnk.out_node;

					//Check for inode in the newnodes list
					if (inode.node_id < onode.node_id) {
						//inode before onode

						//Checking for inode's existence
						//curnode = newnodes.get(0);
						int curnodei = 0;
						while(!(curnodei == newnodes.size())&&
							(newnodes.get(curnodei).node_id != inode.node_id)) 
							++curnodei;

						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//(normalized trait number for new NNode)

							//old buggy version:
							// if (!(onode->nodetrait)) nodetraitnum=((*(traits.begin()))->trait_id);
							if (inode.nodetrait == null) nodetraitnum = 0;
							else
								nodetraitnum = (inode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

							new_inode = new Nnode(inode, newtraits.get(nodetraitnum));
							node_insert(newnodes, new_inode);

						}
						else {
							new_inode = newnodes.get(curnodei);

						}

						//Checking for onode's existence
						curnodei = 0; //newnodes.begin();
						while(!(curnodei == newnodes.size())&&
							(newnodes.get(curnodei).node_id != onode.node_id)) 
							++curnodei;
						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode

							if (onode.nodetrait == null) nodetraitnum = 0;
							else
								nodetraitnum = (onode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

							new_onode = new Nnode(onode, newtraits.get(nodetraitnum));

							node_insert(newnodes, new_onode);

						}
						else {
							new_onode = newnodes.get(curnodei);
						}

					}
					//If the onode has a higher id than the inode we want to add it first
					else {
						//Checking for onode's existence
						int curnodei = 0; //newnodes.begin();
						while((curnodei != newnodes.size())&&
							(newnodes.get(curnodei).node_id != onode.node_id)) 
							++curnodei;
						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (onode.nodetrait.trait_id == 0) nodetraitnum=0;
							else
								nodetraitnum=(onode.nodetrait.trait_id)-(traits.get(0).trait_id);			       

							new_onode = new Nnode(onode, newtraits.get(nodetraitnum));
							//newnodes.push_back(new_onode);
							node_insert(newnodes, new_onode);

						}
						else {
							new_onode=newnodes.get(curnodei);

						}

						//Checking for inode's existence
						curnodei = 0; //newnodes.begin();
						while(curnodei != newnodes.size()&&
							(newnodes.get(curnodei).node_id != inode.node_id)) 
							++curnodei;
						if (curnodei ==newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (!(inode.nodetrait == null)) nodetraitnum=0;
							else
								nodetraitnum=(inode.nodetrait.trait_id)-(traits.get(0).trait_id);			    

							new_inode = new Nnode(inode, newtraits.get(nodetraitnum));

							node_insert(newnodes, new_inode);

						}
						else {
							new_inode=newnodes.get(curnodei);

						}

					} //End NNode checking section- NNodes are now in new Genome

					//Add the Gene
					newgene = new Gene(chosengene, newtraits.get(nodetraitnum), new_inode,new_onode);
					if (disable) {
						newgene.enable = false;
						disable = false;
					}
					newgenes.add(newgene);
				}
		}

			new_genome = new Genome(genomeid, newtraits, newnodes, newgenes);

			//Return the baby Genome
			return (new_genome);
	}

	public Genome mate_multipoint_avg(Genome g, int genomeid, double fitness1, double fitness2, boolean interspec_flag) {
		//The baby Genome will contain these new Traits, NNodes, and Genes
		//std::vector<Trait*> newtraits;
		//std::vector<NNode*> newnodes;
		//td::vector<Gene*> newgenes;
		
		Vector<Trait> newtraits = new Vector<Trait>();
		Vector<Nnode> newnodes = new Vector<Nnode>();
		Vector<Gene> newgenes = new Vector<Gene>();

		//iterators for moving through the two parents' traits
		//std::vector<Trait*>::iterator p1trait;
		//std::vector<Trait*>::iterator p2trait;
		Vector<Trait> p1trait = new Vector<Trait>();
		Vector<Trait> p2trait = new Vector<Trait>();
		Trait newtrait;

		//std::vector<Gene*>::iterator curgene2;  //Checks for link duplication
		Vector<Gene> curgene2 = new Vector<Gene>();

		//iterators for moving through the two parents' genes
		//std::vector<Gene*>::iterator p1gene;
		//std::vector<Gene*>::iterator p2gene;
		Vector<Gene> p1gene = new Vector<Gene>();
		Vector<Gene> p2gene = new Vector<Gene>();
		double p1innov;  //Innovation numbers for genes inside parents' Genomes
		double p2innov;
		Gene chosengene = null;  //Gene chosen for baby to inherit
		int traitnum;  //Number of trait new gene points to
		Nnode inode;  //NNodes connected to the chosen Gene
		Nnode onode;
		Nnode new_inode;
		Nnode new_onode;

		//std::vector<NNode*>::iterator curnode;  //For checking if NNodes exist already 
		Vector<Nnode> curnode = new Vector<Nnode>();
		int nodetraitnum;  //Trait number for a NNode

		//This Gene is used to hold the average of the two genes to be averaged
		Gene avgene;
		Gene newgene;

		boolean skip;

		boolean p1better;  //Designate the better genome

		// BLX-alpha variables - for assigning weights within a good space 
		// This is for BLX-style mating, which isn't used in this implementation,
		//   but can easily be made from multipoint_avg 
		//double blx_alpha;
		//double w1,w2;
		//double blx_min, blx_max;
		//double blx_range;   //The space range
		//double blx_explore;  //Exploration space on left or right
		//double blx_pos;  //Decide where to put gnes distancewise
		//blx_pos=randfloat();

		//First, average the Traits from the 2 parents to form the baby's Traits
		//It is assumed that trait lists are the same length
		//In future, could be done differently
		int p2traiti = 0;
		int p1traiti = 0;
		for(p1traiti = 0; p1traiti != traits.size(); p1traiti++) {
			newtrait = new Trait(p1trait.get(p1traiti), p2trait.get(p2traiti));  //Construct by averaging
			newtraits.add(newtrait);
			++p2traiti;
		}

		//Set up the avgene
		avgene = new Gene(0,null,null,false,0,0);
		int curnodei = 0;
		//NEW 3/17/03 Make sure all sensors and outputs are included
		for(curnodei = 0; curnodei != g.nodes.size(); curnodei++) {
			if (((g.nodes.get(curnodei).gen_node_label) == nodeplace.INPUT)||
				((g.nodes.get(curnodei).gen_node_label) == nodeplace.OUTPUT)||
				((g.nodes.get(curnodei).gen_node_label) == nodeplace.BIAS)) {
					if (g.nodes.get(curnodei).nodetrait == null) nodetraitnum=0;
					else 
						nodetraitnum=(g.nodes.get(curnodei).nodetrait.trait_id)- (traits.get(0).trait_id);

					//Create a new node off the sensor or output
					new_onode = new Nnode(g.nodes.get(curnodei), newtraits.get(nodetraitnum));

					//Add the new node
					node_insert(newnodes, new_onode);

				}

		}

		//Figure out which genome is better
		//The worse genome should not be allowed to add extra structural baggage
		//If they are the same, use the smaller one's disjoint and excess genes only
		if (fitness1 > fitness2) 
			p1better = true;
		else if (fitness1 == fitness2) {
			if (genes.size() < g.genes.size())
				p1better = true;
			else p1better = false;
		}
		else 
			p1better = false;


		//Now move through the Genes of each parent until both genomes end
		
		int p1i = 0;
		int p2i = 0;
		//while(!((p1gene==genes.end())&&
		//	(p2gene==(g->genes).end()))) {
		
		while(!(p1i == genes.size() && p2i == g.genes.size())){//)p1gene < genes.size() && p2gene < g.genes.size()){
				skip = false;  //Default to not skipping a chosen gene
				
				if(p1i == genes.size()){
					chosengene = g.genes.get(p2i);
					p2i++;
					if (p1better) skip=true;
				}
				else if(p2i == g.genes.size()){
					chosengene = genes.get(p1i);
					p1i++;
					if (!p1better) skip=true;
				}
				else {
					p1innov = genes.get(p1i).innovation_num;
					p2innov = g.genes.get(p2i).innovation_num;
					
					if (p1innov == p2innov) {
						if (Math.random() > 0.5) avgene.lnk.linktrait = genes.get(p1i).lnk.linktrait; //((*p1gene)->lnk)->linktrait;
						else avgene.lnk.linktrait = g.genes.get(p2i).lnk.linktrait; //((*p2gene)->lnk)->linktrait;
						
						//WEIGHTS AVERAGED HERE
						avgene.lnk.weight = (genes.get(p1i).lnk.weight + g.genes.get(p2i).lnk.weight) / 2.0; //(((*p1gene)->lnk)->weight+((*p2gene)->lnk)->weight)/2.0;

						if (Math.random()>0.5) avgene.lnk.in_node = genes.get(p1i).lnk.in_node; //((*p1gene)->lnk)->in_node;
						else avgene.lnk.in_node = g.genes.get(p2i).lnk.in_node; //((*p2gene)->lnk)->in_node;

						if (Math.random()>0.5) avgene.lnk.out_node = genes.get(p1i).lnk.out_node;
						else avgene.lnk.out_node = g.genes.get(p2i).lnk.out_node;

						if (Math.random()>0.5) avgene.lnk.is_recurrent = genes.get(p1i).lnk.is_recurrent;
						else avgene.lnk.is_recurrent = g.genes.get(p2i).lnk.is_recurrent;

						avgene.innovation_num = genes.get(p1i).innovation_num; //(*p1gene)->innovation_num;
						avgene.mutation_num = genes.get(p1i).mutation_num/2.0; //((*p1gene)->mutation_num+(*p2gene)->mutation_num)/2.0;

						//If one is disabled, the corresponding gene in the offspring
						//will likely be disabled
						if (((genes.get(p1i).enable)==false)||
							((g.genes.get(p2i).enable)==false)) 
							if (Math.random()<0.75) avgene.enable=false;

						++p1i;
						++p2i;
					}
					else if (p1innov<p2innov) {
						chosengene=genes.get(p1i);
						++p1i;

						if (!p1better) skip=true;

					}
					else if (p2innov<p1innov) {
						chosengene=g.genes.get(p2i);
						++p2i;
						if (p1better) skip=true;
					}
				}
				
				/*if (((((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
						(((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&
						((((*curgene2)->lnk)->is_recurrent)== (((chosengene)->lnk)->is_recurrent)))||
						((((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
						(((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&
						(!((((*curgene2)->lnk)->is_recurrent)))&&
						(!((((chosengene)->lnk)->is_recurrent)))     ))
					{ 
						skip=true;

					}
					++curgene2;
				}*/
				
				int curgene2i = 0;
				if (!(curgene2i == newgenes.size()) &&
					(((newgenes.get(curgene2i).lnk.in_node.node_id)==(chosengene.lnk.in_node.node_id)))&&
					(newgenes.get(curgene2i).lnk.out_node.node_id)==(chosengene.lnk.out_node.node_id)&&((newgenes.get(curgene2i).lnk.is_recurrent)== chosengene.lnk.is_recurrent)&&
					(((newgenes.get(curgene2i).lnk.in_node.node_id)== chosengene.lnk.out_node.node_id))&&
					((newgenes.get(curgene2i).lnk.out_node.node_id)== chosengene.lnk.in_node.node_id)&&
					(!newgenes.get(curgene2i).lnk.is_recurrent)&&
					(!(newgenes.get(curgene2i).lnk.is_recurrent)))
				{	
					skip = true	;
					++curgene2i;	
				}
		
		
/*		p1gene=genes.begin();
		p2gene=(g->genes).begin();
		while(!((p1gene==genes.end())&&
			(p2gene==(g->genes).end()))) {

				avgene->enable=true;  //Default to enabled

				skip=false;

				if (p1gene==genes.end()) {
					chosengene=*p2gene;
					++p2gene;

					if (p1better) skip=true;

				}
				else if (p2gene==(g->genes).end()) {
					chosengene=*p1gene;
					++p1gene;

					if (!p1better) skip=true;
				}
				else {
					//Extract current innovation numbers
					p1innov=(*p1gene)->innovation_num;
					p2innov=(*p2gene)->innovation_num;

					if (p1innov==p2innov) {
						//Average them into the avgene
						if (randfloat()>0.5) (avgene->lnk)->linktrait=((*p1gene)->lnk)->linktrait;
						else (avgene->lnk)->linktrait=((*p2gene)->lnk)->linktrait;

						//WEIGHTS AVERAGED HERE
						(avgene->lnk)->weight=(((*p1gene)->lnk)->weight+((*p2gene)->lnk)->weight)/2.0;

					

						////BLX-alpha method (Eschelman et al 1993)
						////Not used in this implementation, but the commented code works
						////with alpha=0.5, this will produce babies evenly in exploitation and exploration space
						////and uniformly distributed throughout
						//blx_alpha=-0.4;
						//w1=(((*p1gene)->lnk)->weight);
						//w2=(((*p2gene)->lnk)->weight);
						//if (w1>w2) {
						//blx_max=w1; blx_min=w2;
						//}
						//else {
						//blx_max=w2; blx_min=w1;
						//}
						//blx_range=blx_max-blx_min;
						//blx_explore=blx_alpha*blx_range;
						////Now extend the range into the exploraton space
						//blx_min-=blx_explore;
						//blx_max+=blx_explore;
						//blx_range=blx_max-blx_min;
						////Set the weight in the new range
						//(avgene->lnk)->weight=blx_min+blx_pos*blx_range;
						//

						if (randfloat()>0.5) (avgene->lnk)->in_node=((*p1gene)->lnk)->in_node;
						else (avgene->lnk)->in_node=((*p2gene)->lnk)->in_node;

						if (randfloat()>0.5) (avgene->lnk)->out_node=((*p1gene)->lnk)->out_node;
						else (avgene->lnk)->out_node=((*p2gene)->lnk)->out_node;

						if (randfloat()>0.5) (avgene->lnk)->is_recurrent=((*p1gene)->lnk)->is_recurrent;
						else (avgene->lnk)->is_recurrent=((*p2gene)->lnk)->is_recurrent;

						avgene->innovation_num=(*p1gene)->innovation_num;
						avgene->mutation_num=((*p1gene)->mutation_num+(*p2gene)->mutation_num)/2.0;

						if ((((*p1gene)->enable)==false)||
							(((*p2gene)->enable)==false)) 
							if (randfloat()<0.75) avgene->enable=false;

						chosengene=avgene;
						++p1gene;
						++p2gene;
					}
					else if (p1innov<p2innov) {
						chosengene=*p1gene;
						++p1gene;

						if (!p1better) skip=true;
					}
					else if (p2innov<p1innov) {
						chosengene=*p2gene;
						++p2gene;

						if (p1better) skip=true;
					}
				}*/

				/*
				//THIS LINE MUST BE DELETED TO SLOW GROWTH
				skip=false;

				//For interspecies mating, allow all genes through:
				if (interspec_flag)
					skip=false;
				

				//Check to see if the chosengene conflicts with an already chosen gene
				//i.e. do they represent the same link    
				curgene2=newgenes.begin();
				while ((curgene2!=newgenes.end()))

				{

					if (((((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
						(((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&
						((((*curgene2)->lnk)->is_recurrent)== (((chosengene)->lnk)->is_recurrent)))||
						((((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
						(((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&
						(!((((*curgene2)->lnk)->is_recurrent)))&&
						(!((((chosengene)->lnk)->is_recurrent)))     ))
					{ 
						skip=true;

					}
					++curgene2;
				}

				if (!skip) {

					//Now add the chosengene to the baby

					//First, get the trait pointer
					if ((((chosengene->lnk)->linktrait))==0) traitnum=(*(traits.begin()))->trait_id - 1;
					else
						traitnum=(((chosengene->lnk)->linktrait)->trait_id)-(*(traits.begin()))->trait_id;  //The subtracted number normalizes depending on whether traits start counting at 1 or 0

					//Next check for the nodes, add them if not in the baby Genome already
					inode=(chosengene->lnk)->in_node;
					onode=(chosengene->lnk)->out_node;

					//Check for inode in the newnodes list
					if (inode->node_id<onode->node_id) {

						//Checking for inode's existence
						curnode=newnodes.begin();
						while((curnode!=newnodes.end())&&
							((*curnode)->node_id!=inode->node_id)) 
							++curnode;

						if (curnode==newnodes.end()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode

							if (!(inode->nodetrait)) nodetraitnum=0;
							else
								nodetraitnum=((inode->nodetrait)->trait_id)-((*(traits.begin()))->trait_id);			       

							new_inode=new NNode(inode,newtraits[nodetraitnum]);

							node_insert(newnodes,new_inode);
						}
						else {
							new_inode=(*curnode);

						}

						//Checking for onode's existence
						curnode=newnodes.begin();
						while((curnode!=newnodes.end())&&
							((*curnode)->node_id!=onode->node_id)) 
							++curnode;
						if (curnode==newnodes.end()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode

							if (!(onode->nodetrait)) nodetraitnum=0;
							else
								nodetraitnum=((onode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			
							new_onode=new NNode(onode,newtraits[nodetraitnum]);

							node_insert(newnodes,new_onode);
						}
						else {
							new_onode=(*curnode);
						}
					}
					//If the onode has a higher id than the inode we want to add it first
					else {
						//Checking for onode's existence
						curnode=newnodes.begin();
						while((curnode!=newnodes.end())&&
							((*curnode)->node_id!=onode->node_id)) 
							++curnode;
						if (curnode==newnodes.end()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (!(onode->nodetrait)) nodetraitnum=0;
							else
								nodetraitnum=((onode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

							new_onode=new NNode(onode,newtraits[nodetraitnum]);

							node_insert(newnodes,new_onode);
						}
						else {
							new_onode=(*curnode);
						}

						//Checking for inode's existence
						curnode=newnodes.begin();
						while((curnode!=newnodes.end())&&
							((*curnode)->node_id!=inode->node_id)) 
							++curnode;
						if (curnode==newnodes.end()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (!(inode->nodetrait)) nodetraitnum=0;
							else
								nodetraitnum=((inode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

							new_inode=new NNode(inode,newtraits[nodetraitnum]);

							node_insert(newnodes,new_inode);
						}
						else {
							new_inode=(*curnode);

						}

					} //End NNode checking section- NNodes are now in new Genome

					//Add the Gene
					newgene=new Gene(chosengene,newtraits[traitnum],new_inode,new_onode);

					newgenes.push_back(newgene);

				}  //End if which checked for link duplicationb

			}

			//delete avgene;  //Clean up used object

			//Return the baby Genome
			return (new Genome(genomeid,newtraits,newnodes,newgenes));*/
				
				if (!skip) {

					//Now add the chosengene to the baby

					//First, get the trait pointer
					if ((newgenes.get(curgene2i).lnk.linktrait == null)) traitnum = traits.get(0).trait_id - 1; 
					else
						traitnum = chosengene.lnk.linktrait.trait_id - traits.get(0).trait_id;  //The subtracted number normalizes depending on whether traits start counting at 1 or 0

					//Next check for the nodes, add them if not in the baby Genome already
					inode = chosengene.lnk.in_node;
					onode = chosengene.lnk.out_node;

					//Check for inode in the newnodes list
					if (inode.node_id < onode.node_id) {
						//inode before onode

						//Checking for inode's existence
						//curnode = newnodes.get(0);
						curnodei = 0;
						while(!(curnodei == newnodes.size())&&
							(newnodes.get(curnodei).node_id != inode.node_id)) 
							++curnodei;

						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//(normalized trait number for new NNode)

							//old buggy version:
							// if (!(onode->nodetrait)) nodetraitnum=((*(traits.begin()))->trait_id);
							if (inode.nodetrait == null) nodetraitnum = 0;
							else
								nodetraitnum = (inode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

							new_inode = new Nnode(inode, newtraits.get(nodetraitnum));
							node_insert(newnodes, new_inode);

						}
						else {
							new_inode = newnodes.get(curnodei);

						}

						//Checking for onode's existence
						curnodei = 0; //newnodes.begin();
						while(!(curnodei == newnodes.size())&&
							(newnodes.get(curnodei).node_id != onode.node_id)) 
							++curnodei;
						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode

							if (onode.nodetrait == null) nodetraitnum = 0;
							else
								nodetraitnum = (onode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

							new_onode = new Nnode(onode, newtraits.get(nodetraitnum));

							node_insert(newnodes, new_onode);

						}
						else {
							new_onode = newnodes.get(curnodei);
						}

					}
					//If the onode has a higher id than the inode we want to add it first
					else {
						//Checking for onode's existence
						curnodei = 0; //newnodes.begin();
						while((curnodei != newnodes.size())&&
							(newnodes.get(curnodei).node_id != onode.node_id)) 
							++curnodei;
						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (onode.nodetrait.trait_id == 0) nodetraitnum=0;
							else
								nodetraitnum=(onode.nodetrait.trait_id)-(traits.get(0).trait_id);			       

							new_onode = new Nnode(onode, newtraits.get(nodetraitnum));
							//newnodes.push_back(new_onode);
							node_insert(newnodes, new_onode);

						}
						else {
							new_onode=newnodes.get(curnodei);

						}
						//Checking for inode's existence
						/*curnode=newnodes.begin();
						while((curnode!=newnodes.end())&&
							((*curnode)->node_id!=inode->node_id)) 
							++curnode;
						if (curnode==newnodes.end()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (!(inode->nodetrait)) nodetraitnum=0;
							else
								nodetraitnum=((inode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

							new_inode=new NNode(inode,newtraits[nodetraitnum]);

							node_insert(newnodes,new_inode);
						}
						else {
							new_inode=(*curnode);

						}*/
						//Checking for inode's existence
						curnodei = 0; //newnodes.begin();
						while(curnodei != newnodes.size()&&
							(newnodes.get(curnodei).node_id != inode.node_id)) 
							++curnodei;
						if (curnodei == newnodes.size()) {
							//Here we know the node doesn't exist so we have to add it
							//normalized trait number for new NNode
							if (!(inode.nodetrait == null)) nodetraitnum=0;
							else
								nodetraitnum=(inode.nodetrait.trait_id)-(traits.get(0).trait_id);			    

							new_inode = new Nnode(inode, newtraits.get(nodetraitnum));

							node_insert(newnodes, new_inode);

						}
						else {
							new_inode=newnodes.get(curnodei);

						}

					} //End NNode checking section- NNodes are now in new Genome

					//Add the Gene
					newgene = new Gene(chosengene, newtraits.get(traitnum), new_inode, new_onode);

					newgenes.add(newgene);
				}		
			}
			return (new Genome(genomeid, newtraits, newnodes, newgenes));
		}

	public Genome mate_singlepoint(Genome g, int genomeid) {
		//The baby Genome will contain these new Traits, NNodes, and Genes
		//std::vector<Trait*> newtraits; 
		//std::vector<NNode*> newnodes;   
		//std::vector<Gene*> newgenes;
		Vector<Trait> newtraits = new Vector<Trait>();
		Vector<Nnode> newnodes = new Vector<Nnode>();
		Vector<Gene> newgenes = new Vector<Gene>();

		//iterators for moving through the two parents' traits
		//std::vector<Trait*>::iterator p1trait;
		//std::vector<Trait*>::iterator p2trait;
		Trait newtrait;
		
		//iterators for moving through the two parents' traits
		//std::vector<Trait*>::iterator p1trait;
		//std::vector<Trait*>::iterator p2trait;
		Vector<Trait> p1trait = new Vector<Trait>();
		Vector<Trait> p2trait = new Vector<Trait>();

		//std::vector<Gene*>::iterator curgene2;  //Checks for link duplication
		Vector<Gene> curgene2 = new Vector<Gene>();

		//iterators for moving through the two parents' genes
		//std::vector<Gene*>::iterator p1gene;
		//std::vector<Gene*>::iterator p2gene;
		Gene p1gene; // = new Vector<Gene>();
		Gene p2gene; // = new Vector<Gene>();
		Gene stopper; // = new Vector<Gene>();
		Gene p1stop; // = new Vector<Gene>();
		Gene p2stop; // = new Vector<Gene>();
		double p1innov;  //Innovation numbers for genes inside parents' Genomes
		double p2innov;
		Gene chosengene = null;  //Gene chosen for baby to inherit
		int traitnum;  //Number of trait new gene points to
		Nnode inode;  //NNodes connected to the chosen Gene
		Nnode onode;
		Nnode new_inode;
		Nnode new_onode;
		
		Gene avgene;	//This Gene is used to hold the average of the two genes to be averaged
		Gene newgene; 		

		//std::vector<NNode*>::iterator curnode;  //For checking if NNodes exist already 
		Vector<Nnode> curnode = new Vector<Nnode>();
		int nodetraitnum;  //Trait number for a NNode
		
/*		std::vector<Gene*>::iterator curgene2;  //Check for link duplication

		//iterators for moving through the two parents' genes
		std::vector<Gene*>::iterator p1gene;
		std::vector<Gene*>::iterator p2gene;
		std::vector<Gene*>::iterator stopper;  //To tell when finished
		std::vector<Gene*>::iterator p2stop;
		std::vector<Gene*>::iterator p1stop;
		double p1innov;  //Innovation numbers for genes inside parents' Genomes
		double p2innov;
		Gene *chosengene;  //Gene chosen for baby to inherit
		int traitnum;  //Number of trait new gene points to
		NNode *inode;  //NNodes connected to the chosen Gene
		NNode *onode;
		NNode *new_inode;
		NNode *new_onode;
		std::vector<NNode*>::iterator curnode;  //For checking if NNodes exist already 
		int nodetraitnum;  //Trait number for a NNode
*/

		int crosspoint; //The point in the Genome to cross at
		int genecounter; //Counts up to the crosspoint
		boolean skip; //Used for skipping unwanted genes
		
		int p1i = 0;
		int p2i = 0;

		//First, average the Traits from the 2 parents to form the baby's Traits
		//It is assumed that trait lists are the same length
/*		p2trait=(g->traits).begin();
		for(p1trait=traits.begin();p1trait!=traits.end();++p1trait) {
			newtrait=new Trait(*p1trait,*p2trait);  //Construct by averaging
			newtraits.push_back(newtrait);
			++p2trait;
		}

		//Set up the avgene
		avgene=new Gene(0,0,0,0,0,0,0);*/
		
		int p2traiti = 0;
		int p1traiti = 0;
		for(p1traiti = 0; p1traiti != traits.size(); p1traiti++) {
			newtrait = new Trait(p1trait.get(p1traiti), p2trait.get(p2traiti));  //Construct by averaging
			newtraits.add(newtrait);
			++p2traiti;
		}

		//Set up the avgene
		avgene = new Gene(0,null,null,false,0,0);

		//Decide where to cross  (p1gene will always be in smaller Genome)
		if (genes.size() < g.genes.size()) {
			crosspoint = (int) Math.random() * genes.size(); // (0,(genes.size())-1);
			p1gene = genes.get(p1i); //genes.begin();
			p2gene = g.genes.get(p2i);
			stopper = g.genes.lastElement();
			p1stop = genes.lastElement();
			p2stop = g.genes.lastElement();
		}
		else {
			crosspoint = (int) Math.random() * genes.size(); //randint(0,((g->genes).size())-1);
			p2gene = genes.get(p2i); //genes.begin();
			p1gene = g.genes.get(p1i);
			stopper = genes.lastElement();
			p1stop = g.genes.lastElement();
			p2stop = genes.lastElement();
		}

		genecounter = 0;  //Ready to count to crosspoint

		skip = false;  //Default to not skip a Gene
		//Note that we skip when we are on the wrong Genome before
		//crossing

		//Now move through the Genes of each parent until both genomes end
		while(!(p2i == genes.size())) {

			avgene.enable = true;  //Default to true

			if (p1i == genes.size()) {
				chosengene = g.genes.get(p2i);
				p2i++;
			}
			else if (p2i == g.genes.size()) {
				chosengene = genes.get(p1i);
				p1i++;
			}
			else {
					p1innov = genes.get(p1i).innovation_num;
					p2innov = g.genes.get(p2i).innovation_num;
					
					if (p1innov == p2innov) {
						if (Math.random() > 0.5) avgene.lnk.linktrait = genes.get(p1i).lnk.linktrait; //((*p1gene)->lnk)->linktrait;
						else avgene.lnk.linktrait = g.genes.get(p2i).lnk.linktrait; //((*p2gene)->lnk)->linktrait;
						
						//WEIGHTS AVERAGED HERE
						avgene.lnk.weight = (genes.get(p1i).lnk.weight + g.genes.get(p2i).lnk.weight) / 2.0; //(((*p1gene)->lnk)->weight+((*p2gene)->lnk)->weight)/2.0;

						if (Math.random()>0.5) avgene.lnk.in_node = genes.get(p1i).lnk.in_node; //((*p1gene)->lnk)->in_node;
						else avgene.lnk.in_node = g.genes.get(p2i).lnk.in_node; //((*p2gene)->lnk)->in_node;

						if (Math.random()>0.5) avgene.lnk.out_node = genes.get(p1i).lnk.out_node;
						else avgene.lnk.out_node = g.genes.get(p2i).lnk.out_node;

						if (Math.random()>0.5) avgene.lnk.is_recurrent = genes.get(p1i).lnk.is_recurrent;
						else avgene.lnk.is_recurrent = g.genes.get(p2i).lnk.is_recurrent;

						avgene.innovation_num = genes.get(p1i).innovation_num; //(*p1gene)->innovation_num;
						avgene.mutation_num = genes.get(p1i).mutation_num/2.0; //((*p1gene)->mutation_num+(*p2gene)->mutation_num)/2.0;

						//If one is disabled, the corresponding gene in the offspring
						//will likely be disabled
						if (((genes.get(p1i).enable)==false)||
							((g.genes.get(p2i).enable)==false)) 
							if (Math.random()<0.75) avgene.enable=false;

						++p1i;
						++p2i;
					}
					
					else if (p1innov<p2innov) {
						if (genecounter < crosspoint) {
							chosengene=genes.get(p1i);
							++p1i;
							++genecounter;
						}
						else{
							chosengene=g.genes.get(p2i);
							++p2i;
						}
					}
					else if (p2innov<p1innov) {
						++p2i;
						skip = true; //Special case: we need to skip to the next iteration
						//becase this Gene is before the crosspoint on the wrong Genome
					}
				}
				
				//Extract current innovation numbers

				//if (p1gene==g->genes.end()) cout<<"WARNING p1"<<std::endl;
				//if (p2gene==g->genes.end()) cout<<"WARNING p2"<<std::endl;

/*				p1innov = p1gene.innovation_num;
				p2innov = p2gene.innovation_num;

				if (p1innov == p2innov) {

					//Pick the chosengene depending on whether we've crossed yet
					if (genecounter < crosspoint) {
						chosengene = genes.get(p1i);
					}
					else if (genecounter > crosspoint) {
						chosengene = g.genes.get(p2i);
					}
					//We are at the crosspoint here
					else {

						//Average them into the avgene
						if (Math.random() > 0.5) avgene.lnk.linktrait = p1gene.lnk.linktrait;
						else avgene.lnk.linktrait = p2gene.lnk.linktrait;

						//WEIGHTS AVERAGED HERE
						(avgene->lnk)->weight=(((*p1gene)->lnk)->weight+((*p2gene)->lnk)->weight)/2.0;


						if (randfloat()>0.5) (avgene->lnk)->in_node=((*p1gene)->lnk)->in_node;
						else (avgene->lnk)->in_node=((*p2gene)->lnk)->in_node;

						if (randfloat()>0.5) (avgene->lnk)->out_node=((*p1gene)->lnk)->out_node;
						else (avgene->lnk)->out_node=((*p2gene)->lnk)->out_node;

						if (randfloat()>0.5) (avgene->lnk)->is_recurrent=((*p1gene)->lnk)->is_recurrent;
						else (avgene->lnk)->is_recurrent=((*p2gene)->lnk)->is_recurrent;

						avgene->innovation_num=(*p1gene)->innovation_num;
						avgene->mutation_num=((*p1gene)->mutation_num+(*p2gene)->mutation_num)/2.0;

						if ((((*p1gene)->enable)==false)||
							(((*p2gene)->enable)==false)) 
							avgene->enable=false;

						chosengene=avgene;
					}

					++p1gene;
					++p2gene;
					++genecounter;
				}
				else if (p1innov<p2innov) {
					if (genecounter<crosspoint) {
						chosengene=*p1gene;
						++p1gene;
						++genecounter;
					}
					else {
						chosengene=*p2gene;
						++p2gene;
					}
				}
				else if (p2innov<p1innov) {
					++p2gene;
					skip=true;  //Special case: we need to skip to the next iteration
					//becase this Gene is before the crosspoint on the wrong Genome
				}
			}*/

			//Check to see if the chosengene conflicts with an already chosen gene
			//i.e. do they represent the same link    
			//curgene2 = newgenes.begin();

			/*while ((curgene2!=newgenes.end())&&
				(!((((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
				(((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&((((*curgene2)->lnk)->is_recurrent)== (((chosengene)->lnk)->is_recurrent)) ))&&
				(!((((((*curgene2)->lnk)->in_node)->node_id)==((((chosengene)->lnk)->out_node)->node_id))&&
				(((((*curgene2)->lnk)->out_node)->node_id)==((((chosengene)->lnk)->in_node)->node_id))&&
				(!((((*curgene2)->lnk)->is_recurrent)))&&
				(!((((chosengene)->lnk)->is_recurrent))) )))
			{

				++curgene2;
			}*/
			
			int curgene2i = 0;
			if (!(curgene2i == newgenes.size()) &&
				(!((newgenes.get(curgene2i).lnk.in_node.node_id)==(chosengene.lnk.in_node.node_id)))&&
				(newgenes.get(curgene2i).lnk.out_node.node_id)==(chosengene.lnk.out_node.node_id)&&((newgenes.get(curgene2i).lnk.is_recurrent)== chosengene.lnk.is_recurrent)&&
				(!((newgenes.get(curgene2i).lnk.in_node.node_id)== chosengene.lnk.out_node.node_id))&&
				((newgenes.get(curgene2i).lnk.out_node.node_id)== chosengene.lnk.in_node.node_id)&&
				(!newgenes.get(curgene2i).lnk.is_recurrent)&&
				(!(newgenes.get(curgene2i).lnk.is_recurrent)))
			{	
				skip = true	;
				++curgene2i;	
			}


			if (curgene2i != newgenes.size()) skip = true;  //Link is a duplicate
			
			if (!skip) {

				//Now add the chosengene to the baby

				//First, get the trait pointer
				if ((newgenes.get(curgene2i).lnk.linktrait == null)) traitnum = traits.get(0).trait_id - 1; 
				else
					traitnum = chosengene.lnk.linktrait.trait_id - traits.get(0).trait_id;  //The subtracted number normalizes depending on whether traits start counting at 1 or 0

				//Next check for the nodes, add them if not in the baby Genome already
				inode = chosengene.lnk.in_node;
				onode = chosengene.lnk.out_node;

				//Check for inode in the newnodes list
				if (inode.node_id < onode.node_id) {
					//inode before onode

					//Checking for inode's existence
					//curnode = newnodes.get(0);
					int curnodei = 0;
					while(!(curnodei == newnodes.size())&&
						(newnodes.get(curnodei).node_id != inode.node_id)) 
						++curnodei;

					if (curnodei == newnodes.size()) {
						//Here we know the node doesn't exist so we have to add it
						//(normalized trait number for new NNode)

						//old buggy version:
						// if (!(onode->nodetrait)) nodetraitnum=((*(traits.begin()))->trait_id);
						if (inode.nodetrait == null) nodetraitnum = 0;
						else
							nodetraitnum = (inode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

						new_inode = new Nnode(inode, newtraits.get(nodetraitnum));
						node_insert(newnodes, new_inode);

					}
					else {
						new_inode = newnodes.get(curnodei);

					}

					//Checking for onode's existence
					curnodei = 0; //newnodes.begin();
					while(!(curnodei == newnodes.size())&&
						(newnodes.get(curnodei).node_id != onode.node_id)) 
						++curnodei;
					if (curnodei == newnodes.size()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode

						if (onode.nodetrait == null) nodetraitnum = 0;
						else
							nodetraitnum = (onode.nodetrait.trait_id)-(traits.get(curnodei).trait_id);			       

						new_onode = new Nnode(onode, newtraits.get(nodetraitnum));

						node_insert(newnodes, new_onode);

					}
					else {
						new_onode = newnodes.get(curnodei);
					}

				}
				//If the onode has a higher id than the inode we want to add it first
				else {
					//Checking for onode's existence
					int curnodei = 0; //newnodes.begin();
					while((curnodei != newnodes.size())&&
						(newnodes.get(curnodei).node_id != onode.node_id)) 
						++curnodei;
					if (curnodei == newnodes.size()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode
						if (onode.nodetrait.trait_id == 0) nodetraitnum=0;
						else
							nodetraitnum=(onode.nodetrait.trait_id)-(traits.get(0).trait_id);			       

						new_onode = new Nnode(onode, newtraits.get(nodetraitnum));
						//newnodes.push_back(new_onode);
						node_insert(newnodes, new_onode);

					}
					else {
						new_onode=newnodes.get(curnodei);

					}
					//Checking for inode's existence
					/*curnode=newnodes.begin();
					while((curnode!=newnodes.end())&&
						((*curnode)->node_id!=inode->node_id)) 
						++curnode;
					if (curnode==newnodes.end()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode
						if (!(inode->nodetrait)) nodetraitnum=0;
						else
							nodetraitnum=((inode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

						new_inode=new NNode(inode,newtraits[nodetraitnum]);

						node_insert(newnodes,new_inode);
					}
					else {
						new_inode=(*curnode);

					}*/
					//Checking for inode's existence
					curnodei = 0; //newnodes.begin();
					while(curnodei != newnodes.size()&&
						(newnodes.get(curnodei).node_id != inode.node_id)) 
						++curnodei;
					if (curnodei == newnodes.size()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode
						if (!(inode.nodetrait == null)) nodetraitnum=0;
						else
							nodetraitnum=(inode.nodetrait.trait_id)-(traits.get(0).trait_id);			    

						new_inode = new Nnode(inode, newtraits.get(nodetraitnum));

						node_insert(newnodes, new_inode);

					}
					else {
						new_inode=newnodes.get(curnodei);

					}

				} //End NNode checking section- NNodes are now in new Genome

				//Add the Gene
				newgene = new Gene(chosengene, newtraits.get(traitnum), new_inode, new_onode);

				newgenes.add(newgene);
			}	
			skip = false;
		}
		return (new Genome(genomeid, newtraits, newnodes, newgenes));

/*			if (!skip) {
				//Now add the chosengene to the baby

				//First, get the trait pointer
				if ((((chosengene->lnk)->linktrait))==0) traitnum=(*(traits.begin()))->trait_id - 1;
				else
					traitnum=(((chosengene->lnk)->linktrait)->trait_id)-(*(traits.begin()))->trait_id;  //The subtracted number normalizes depending on whether traits start counting at 1 or 0

				//Next check for the nodes, add them if not in the baby Genome already
				inode=(chosengene->lnk)->in_node;
				onode=(chosengene->lnk)->out_node;

				//Check for inode in the newnodes list
				if (inode->node_id<onode->node_id) {
					//cout<<"inode before onode"<<std::endl;
					//Checking for inode's existence
					curnode=newnodes.begin();
					while((curnode!=newnodes.end())&&
						((*curnode)->node_id!=inode->node_id)) 
						++curnode;

					if (curnode==newnodes.end()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode

						if (!(inode->nodetrait)) nodetraitnum=0;
						else
							nodetraitnum=((inode->nodetrait)->trait_id)-((*(traits.begin()))->trait_id);			       

						new_inode=new NNode(inode,newtraits[nodetraitnum]);

						node_insert(newnodes,new_inode);
					}
					else {
						new_inode=(*curnode);
					}

					//Checking for onode's existence
					curnode=newnodes.begin();
					while((curnode!=newnodes.end())&&
						((*curnode)->node_id!=onode->node_id)) 
						++curnode;
					if (curnode==newnodes.end()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode

						if (!(onode->nodetrait)) nodetraitnum=0;
						else
							nodetraitnum=((onode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			     

						new_onode=new NNode(onode,newtraits[nodetraitnum]);
						node_insert(newnodes,new_onode);

					}
					else {
						new_onode=(*curnode);
					}
				}
				//If the onode has a higher id than the inode we want to add it first
				else {
					//Checking for onode's existence
					curnode=newnodes.begin();
					while((curnode!=newnodes.end())&&
						((*curnode)->node_id!=onode->node_id)) 
						++curnode;
					if (curnode==newnodes.end()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode
						if (!(onode->nodetrait)) nodetraitnum=0;
						else
							nodetraitnum=((onode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

						new_onode=new NNode(onode,newtraits[nodetraitnum]);
						node_insert(newnodes,new_onode);
					}
					else {
						new_onode=(*curnode);
					}

					//Checking for inode's existence
					curnode=newnodes.begin();

					while((curnode!=newnodes.end())&&
						((*curnode)->node_id!=inode->node_id)) 
						++curnode;
					if (curnode==newnodes.end()) {
						//Here we know the node doesn't exist so we have to add it
						//normalized trait number for new NNode
						if (!(inode->nodetrait)) nodetraitnum=0;
						else
							nodetraitnum=((inode->nodetrait)->trait_id)-(*(traits.begin()))->trait_id;			       

						new_inode=new NNode(inode,newtraits[nodetraitnum]);
						//newnodes.push_back(new_inode);
						node_insert(newnodes,new_inode);
					}
					else {
						new_inode=(*curnode);
					}

				} //End NNode checking section- NNodes are now in new Genome

				//Add the Gene
				newgenes.push_back(new Gene(chosengene,newtraits[traitnum],new_inode,new_onode));

			}  //End of if (!skip)

			skip=false;
		}

		//delete avgene;  //Clean up used object

		//Return the baby Genome
		return (new Genome(genomeid,newtraits,newnodes,newgenes));*/
	}
	
	void node_insert(Vector<Nnode> nlist, Nnode n) {
		//std::vector<NNode*>::iterator curnode;
		int curnodei = 0;

		int id = n.node_id;
		
		/*while ((curnode!=nlist.end())&&
			(((*curnode)->node_id)<id)) 
			++curnode;*/
		
		for(curnodei = 0; curnodei < nlist.size() && nlist.get(curnodei).node_id < id; curnodei++){
			//curnode = nlist.get(curnodei);
		}

		nlist.add(curnodei, n);

	}
	
	
	//
	// void mutate_add_sensor(std::vector<Innovation*> &innovs, double
	// &curinnov);
	//
	// // ****** MATING METHODS *****
	//
	// // This method mates this Genome with another Genome g.
	// // For every point in each Genome, where each Genome shares
	// // the innovation number, the Gene is chosen randomly from
	// // either parent. If one parent has an innovation absent in
	// // the other, the baby will inherit the innovation
	// // Interspecies mating leads to all genes being inherited.
	// // Otherwise, excess genes come from most fit parent.
	// Genome *mate_multipoint(Genome *g,int genomeid,double fitness1, double
	// fitness2, bool interspec_flag);
	//
	// //This method mates like multipoint but instead of selecting one
	// // or the other when the innovation numbers match, it averages their
	// // weights
	// Genome *mate_multipoint_avg(Genome *g,int genomeid,double fitness1,double
	// fitness2, bool interspec_flag);
	//
	// // This method is similar to a standard single point CROSSOVER
	// // operator. Traits are averaged as in the previous 2 mating
	// // methods. A point is chosen in the smaller Genome for crossing
	// // with the bigger one.
	// Genome *mate_singlepoint(Genome *g,int genomeid);
	//
	//
	// // ******** COMPATIBILITY CHECKING METHODS ********
	//
	// // This function gives a measure of compatibility between
	// // two Genomes by computing a linear combination of 3
	// // characterizing variables of their compatibilty.
	// // The 3 variables represent PERCENT DISJOINT GENES,
	// // PERCENT EXCESS GENES, MUTATIONAL DIFFERENCE WITHIN
	// // MATCHING GENES. So the formula for compatibility
	// // is: disjoint_coeff*pdg+excess_coeff*peg+mutdiff_coeff*mdmg.
	// // The 3 coefficients are global system parameters
	// double compatibility(Genome *g);
	//
	// double trait_compare(Trait *t1,Trait *t2);
	//
	// // Return number of non-disabled genes
	// int extrons();
	//
	// // Randomize the trait pointers of all the node and connection genes
	// void randomize_traits();
	//
	// protected:
	// //Inserts a NNode into a given ordered list of NNodes in order
	// void node_insert(std::vector<NNode*> &nlist, NNode *n);
	//
	// //Adds a new gene that has been created through a mutation in the
	// //*correct order* into the list of genes in the genome
	// void add_gene(std::vector<Gene*> &glist,Gene *g);
	//
	// };

} // end of class genome

//
// //Calls special constructor that creates a Genome of 3 possible types:
// //0 - Fully linked, no hidden nodes
// //1 - Fully linked, one hidden node splitting each link
// //2 - Fully connected with a hidden layer
// //num_hidden is only used in type 2
// //Saves to file "auto_genome"
// Genome *new_Genome_auto(int num_in,int num_out,int num_hidden,int type, const
// char *filename);
//
// void print_Genome_tofile(Genome *g,const char *filename);
//
// } // namespace NEAT
//
// #endif
//

package nea.minecraft.tex.brain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nea.rtNEAT.*;

public class NeatCore {
	public TexBrain brain;
	
	private long lastupdate = 0;
	private double random = 0;
	
	public Network neatnet;
	
	public NeatCore(TexBrain brain){
		this.brain = brain;
		lastupdate = System.currentTimeMillis();	
		Population p = new Population();
		
		InputStream is = null;
		try {
			is = new FileInputStream("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\minecraft_startgenes");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader iFile = new BufferedReader(new InputStreamReader(is));
		
		Genome g = new Genome(0,iFile);
		Network n = g.genesis(1);
		Organism org = new Organism(0, g, 1);
		random = Neat.randfloat();
		Neat.load_neat_params("C:\\Users\\Reaver\\Desktop\\minecraft\\mcp910-pre1\\src\\minecraft\\nea\\rtNEAT\\NEAT_params.ne", true);
	}
}
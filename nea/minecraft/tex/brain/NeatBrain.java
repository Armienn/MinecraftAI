package nea.minecraft.tex.brain;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nea.rtNEAT.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class NeatBrain {
	public TexBrain brain;
	
	private long lastupdate = 0;
	private double random = 0;
	
	public NeatBrain(TexBrain brain){
		this.brain = brain;
		lastupdate = System.currentTimeMillis();
		random = Neat.randfloat();
		Neat.load_neat_params("NEAT_params.ne", true);
		Population p = new Population();
	}
}
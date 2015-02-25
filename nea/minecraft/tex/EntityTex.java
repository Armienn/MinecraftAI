package nea.minecraft.tex;

import nea.minecraft.tex.ai.TexAnalysis;
import nea.minecraft.tex.ai.TexLearning;
import nea.minecraft.tex.ai.TexMainAI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTex extends EntityLiving implements net.minecraft.entity.passive.IAnimals {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	TexMainAI aithread;
	TexLearning learningthread;
	TexAnalysis analysisthread;
	
	public EntityTex(World worldIn){
		super(worldIn);
		logger.info("Constructor of Tex.");
		if (!this.worldObj.isRemote){ // if this is server
			brain = new TexBrain(worldObj, ((Entity)this).getEntityId());
			aithread = new TexMainAI(brain);
			learningthread = new TexLearning(brain);
			analysisthread = new TexAnalysis(brain);
			aithread.start();
			learningthread.start();
			analysisthread.start();
		}
	}
	
	public void onUpdate(){
		super.onUpdate();
		if (!this.worldObj.isRemote){ // if this is server
			synchronized(brain.senses){
				brain.senses.Update(this); }
			synchronized(brain.actions){
				brain.actions.Update(this); }
			//updateSenses();
			//updateSensoryMemory();
			//updateRewards();
			//updateBehaviour();
			brain.Updated();
		}
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund){
		super.readEntityFromNBT(tagCompund);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound){
		super.writeEntityToNBT(tagCompound);
	}
}

package nea.minecraft.tex;

import nea.minecraft.tex.ai.TexAnalysis;
import nea.minecraft.tex.ai.TexLearning;
import nea.minecraft.tex.ai.TexMainAI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTex extends EntityLiving implements net.minecraft.entity.passive.IAnimals {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	TexMainAI aithread;
	TexLearning learningthread;
	TexAnalysis analysisthread;
	
	public ItemStack[] inventory = new ItemStack[8];
	public double hunger = 0;
	
	public EntityTex(World worldIn){
		super(worldIn);
		logger.info("Tex #" + ((Entity)this).getEntityId() + ": Constructor");
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
			//update input to ai
			synchronized(brain){
				brain.senses.Update(this);
				brain.rewards.Update(this);
				brain.sensememory.UpdateExternal();
			}
			
			//update output to world
			synchronized(brain){
				brain.actions.Update(this);
				brain.sensememory.UpdateInternal();
			}
			
			//tell brain that it's been updated
			brain.Updated();
		}
	}
	
	public void onDeath(DamageSource cause){
		for(int i = 0; i<inventory.length; i++){
			if(inventory[i] != null){
				this.entityDropItem(inventory[i], 1);
				inventory[i] = null;
			}
		}
		brain.logger.info("Tex #" + brain.id  + " has been killed. All items dropped!");
		brain.Say("Goodbye cruel World!");
		brain.OnDeath();
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund){
		super.readEntityFromNBT(tagCompund);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound){
		super.writeEntityToNBT(tagCompound);
	}
}

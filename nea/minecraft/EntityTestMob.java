/*
 * package nea.minecraft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public class EntityTestMobCreature extends EntityCreature {

	public EntityTestMobCreature(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

}

 * */

package nea.minecraft;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMob extends EntityLiving implements net.minecraft.entity.passive.IAnimals {
	
	Logger logger = LogManager.getLogger();
	EntityTestMobAI aithread;

	public EntityTestMob(World worldIn) {
		super(worldIn);
		logger.info("constructor of testmob.");
		if (!this.worldObj.isRemote){
			aithread = new EntityTestMobAI(worldIn, ((Entity)this).getEntityId());
			aithread.start();
		}
	}
	
	public void onUpdate(){
		super.onUpdate();
		if (!this.worldObj.isRemote){ // if this is server
			//logger.info("onUpdate of testmob.");
			updateSenses();
			updateBehaviour();
		}
	}
	
	private void updateSenses(){
		if(aithread != null && aithread.isAlive()){
			synchronized(aithread.senses){
				aithread.senses.lastupdate = System.currentTimeMillis();
				
				///// Update senses with current position
				aithread.senses.posX = this.posX;
				aithread.senses.posY = this.posY;
				aithread.senses.posZ = this.posZ;
				
				///// Find entities nearby
				List var1 = this.worldObj.loadedEntityList;
				Iterator var2 = var1.iterator();

	            while (var2.hasNext()) {
	                //EntityItem var3 = (EntityItem)var2.next();
	                Entity var3 = (Entity)var2.next();
	                //if(var3.getDistanceSqToEntity(this) < 100) {
		    		//	logger.info("Entity nearby: " + var3.toString());
	                //}

	                /*if (!var3.isDead && var3.getEntityItem() != null && !var3.func_174874_s())
	                {
	                    this.func_175445_a(var3);
	                }*/
	            }
				/*///*/
			}
		}
		else {
			logger.info("Cannot update senses.");
		}
	}
	
	private void updateBehaviour(){
		if(aithread != null && aithread.isAlive()){
			synchronized(aithread.behaviour){
				this.rotationYaw = 30.0F;
				if(aithread.behaviour.fly){
					this.motionX = aithread.behaviour.motionX;
					this.motionY = aithread.behaviour.motionY;
					this.motionZ = aithread.behaviour.motionZ;
				}
				else if (this.onGround) {
					this.motionX = aithread.behaviour.motionX;
					this.motionY = aithread.behaviour.motionY;
					this.motionZ = aithread.behaviour.motionZ;
					aithread.behaviour.motionY = 0;
				}
			}
		}
		else {
			logger.info("Cannot update behaviour.");
		}
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund){
		super.readEntityFromNBT(tagCompund);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound){
		super.writeEntityToNBT(tagCompound);
	}
}

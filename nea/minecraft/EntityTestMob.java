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

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
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
			aithread = new EntityTestMobAI();
			aithread.start();
		}
	}
	
	public void onUpdate(){
		super.onUpdate();
		if (!this.worldObj.isRemote){ // if this is server
			logger.info("onUpdate of testmob.");
			updateSenses();
			updateBehaviour();
		}
	}
	
	private void updateSenses(){
		if(aithread != null && aithread.isAlive()){
			synchronized(aithread.senses){
				aithread.senses.lastupdate = System.currentTimeMillis();
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
				this.motionX = aithread.behaviour.motionX;//this.rand.nextDouble()*0.2 - 0.1;
				this.motionZ = aithread.behaviour.motionZ;//this.rand.nextDouble()*0.2 - 0.1;
				if(aithread.behaviour.motionY > 0 && this.onGround){
					this.motionY = aithread.behaviour.motionY;
					aithread.behaviour.motionY = 0;
				}
			}
		}
		else {
			logger.info("Cannot update senses.");
		}
	}
}

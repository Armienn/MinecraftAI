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

public class EntityTestMob extends EntityLiving {
	
	Logger logger = LogManager.getLogger();

	public EntityTestMob(World worldIn) {
		super(worldIn);
		logger.info("constructor of testmob.");
		// TODO Auto-generated constructor stub
	}
	
	public void onUpdate(){
		logger.info("onUpdate of testmob.");
		super.onUpdate();
	}
	
	public void onLivingUpdate(){
		logger.info("onLivingUpdate of testmob.");
		super.onLivingUpdate();
	}
}

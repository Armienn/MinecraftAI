package nea.minecraft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMobCreature extends EntityCreature {
	
	Logger logger = LogManager.getLogger();

	public EntityTestMobCreature(World worldIn) {
		super(worldIn);
		logger.info("constructor of testmob");
		// TODO Auto-generated constructor stub
	}

}
/*
package nea.minecraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMob extends EntityLivingBase {
	
	Logger logger = LogManager.getLogger();

	public EntityTestMob(World worldIn) {
		super(worldIn);
		logger.info("constructor of testmob");
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack getHeldItem() {
		logger.info("getHeldItem of testmob");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		logger.info("getEquipmentInSlot of testmob");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getCurrentArmor(int p_82169_1_) {
		logger.info("getCurrentArmor of testmob");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
		logger.info("setCurrentItemOrArmor of testmob");
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack[] getInventory() {
		logger.info("getInventory of testmob");
		// TODO Auto-generated method stub
		return null;
	}

}

*/
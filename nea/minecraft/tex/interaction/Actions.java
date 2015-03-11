package nea.minecraft.tex.interaction;

import java.util.ArrayList;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class Actions {
	TexBrain brain;
	public long time; 
	/// Takeable actions : ///
	public ArrayList<Action> actions = new ArrayList<Action>();
	
	// Utility :
	double dx = 0;
	double dz = 0;
	
	public Actions(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(EntityTex entity) {
		time = brain.worldObj.getTotalWorldTime();
		for(Action action : actions){
			int slot = 0;
			EntityItem entityitem = null;
			ItemStack itemstack = null;
			Item item = null;
			switch(action.GetType()){
			case Move:
				if (entity.onGround) {
					float angle = action.GetParameter(0);
					float speed = action.GetParameter(1);
					dx = speed*Math.cos(angle*Math.PI);
					dz = speed*Math.sin(angle*Math.PI);
					entity.rotationYaw = angle*360;
					brain.logger.info("Walking with speed " + speed + " and direction " + (int)(angle*360));
				}
				break;
			case Jump:
				if (entity.onGround) {
					entity.motionY = 0.45;
					brain.logger.info("Jumping");
				}
				break;
			case PickUp:
				slot = (int)(action.GetParameter(0)*8);
				if(entity.inventory[slot] == null)
					entityitem = (EntityItem)brain.worldObj.findNearestEntityWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D), entity);
				if(entityitem != null){
					entity.inventory[slot] = entityitem.getEntityItem();
					entity.onItemPickup(entityitem, 1);
					entityitem.setDead();
					brain.logger.info("Picking up " + entityitem.getEntityItem().stackSize + " " + entityitem.getName());
					brain.Say("Picked up a " + entityitem.getName());
				}
				break;
			case Drop:
				slot = (int)(action.GetParameter(0)*8);
				itemstack = entity.inventory[slot];
				if(entity.inventory[slot] != null){
					entity.entityDropItem(itemstack, 0);
					brain.logger.info("Dropping " + entity.inventory[slot].stackSize + " " + entity.inventory[slot].getDisplayName());
					entity.inventory[slot] = null;
				}
				break;
			case Use:
				slot = (int)(action.GetParameter(0)*8);
				itemstack = entity.inventory[slot];
				if(itemstack != null){
					item = itemstack.getItem();
					if(item instanceof ItemFood){
						entity.hunger -= 1;
						if(entity.hunger < 0)
							entity.hunger = 0;
						brain.Say("Omnomnom!");
						itemstack.stackSize--;
						if(itemstack.stackSize <= 0)
							entity.inventory[slot] = null;
					}
				}
				break;
			default:
				break;
			}
			entity.motionX = dx;
			entity.motionZ = dz;
			if(dz+dx > 0.1){
				entity.hunger += 0.01;
			}
			else{
				entity.hunger += 0.001;
			}
		}
	}
	
	public Actions Copy(){
		Actions actions = new Actions(brain);
		actions.time = time;
		actions.actions.addAll(this.actions);
		this.actions.clear();
		return actions;
	}
}

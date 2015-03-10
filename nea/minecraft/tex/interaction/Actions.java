package nea.minecraft.tex.interaction;

import java.util.ArrayList;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
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
				int slot = (int)(action.GetParameter(0)*8);
				EntityItem item = (EntityItem)brain.worldObj.findNearestEntityWithinAABB(EntityItem.class, entity.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D), entity);
				if(item != null && entity.inventory[slot] == null){
					entity.inventory[slot] = item.getEntityItem();
					entity.onItemPickup(item, 1);
					item.setDead();
					brain.logger.info("Picking up " + item.getEntityItem().stackSize + " " + item.getName());
					brain.Say("Picked up a " + item.getName());
				}
				break;
			case Drop:
				int slot = (int)(action.GetParameter(0)*8);
				ItemStack inventoryItem = entity.inventory[slot];
				if(entity.inventory[slot] != null)
					entity.entityDropItem(inventoryItem, 0);
				entity.inventory[slot] = null;
				break;
			case Use:
				
				break;
			default:
				break;
			}
			entity.motionX = dx;
			entity.motionZ = dz;
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

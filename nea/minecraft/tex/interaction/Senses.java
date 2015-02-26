package nea.minecraft.tex.interaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;
import nea.minecraft.utility.ItemInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class Senses {
	TexBrain brain;
	public long time; 
	public ArrayList<ItemInfo> nearbyitems = new ArrayList<ItemInfo>();
	
	public Senses(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(EntityTex entity){
		time = brain.worldObj.getTotalWorldTime();
		///// Find entities nearby
		nearbyitems.clear();
		List var1 = brain.worldObj.loadedEntityList;
		Iterator var2 = var1.iterator();

        while (var2.hasNext()) {
            //EntityItem var3 = (EntityItem)var2.next();
            Entity var3 = (Entity)var2.next();
            if(var3.getDistanceSqToEntity(entity) < 10*10) {
            	if(var3 instanceof EntityItem) {
            		nearbyitems.add(new ItemInfo((EntityItem)var3));
            	}
    			//logger.info("Entity nearby: " + var3.toString());
            }
        }
		/*///*/
	}
	
	public Senses Copy(){
		Senses sense = new Senses(brain);
		sense.time = time;
		sense.nearbyitems.addAll(nearbyitems);
		return sense;
	}
}

package nea.minecraft.tex.interaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.memory.utility.EntityMemory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class Senses {
	TexBrain brain;
	public long time; 
	public ArrayList<EntityMemory> entityinfo = new ArrayList<EntityMemory>();
	
	public Senses(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(EntityTex entity){
		time = brain.worldObj.getTotalWorldTime();
		///// Find entities nearby
		entityinfo.clear();
		List var1 = brain.worldObj.loadedEntityList;
		Iterator var2 = var1.iterator();

        while (var2.hasNext()) {
            //EntityItem var3 = (EntityItem)var2.next();
            Entity var3 = (Entity)var2.next();
            if(var3.getDistanceSqToEntity(entity) < 10*10) {
            	if(var3 instanceof EntityItem) {
            		AddItem((EntityItem)var3);
            	}
    			//logger.info("Entity nearby: " + var3.toString());
            }
        }
		/*///*/
	}
	
	private void AddItem(EntityItem item){
		EntityMemory info = new EntityMemory(item.getEntityId(),"item",time); 
		entityinfo.add(info);
	}
	
	public Senses Copy(){
		Senses sense = new Senses(brain);
		sense.time = time;
		sense.entityinfo.addAll(entityinfo);
		return sense;
	}
}

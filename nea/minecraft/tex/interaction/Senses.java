package nea.minecraft.tex.interaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nea.minecraft.tex.EntityTex;
import nea.minecraft.tex.TexBrain;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemParameter;
import nea.minecraft.tex.memory.utility.MemReward;
import nea.minecraft.tex.memory.utility.ParameterValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class Senses {
	TexBrain brain;
	public long time; 
	public ArrayList<MemReward> rewards = new ArrayList<MemReward>();
	public ArrayList<MemEntity> entityinfo = new ArrayList<MemEntity>();
	public MemEntity self;
	
	public Senses(TexBrain brain){
		this.brain = brain;
	}
	
	public void Update(EntityTex entity){
		time = brain.worldObj.getTotalWorldTime();
		rewards.clear();
		if(self != null)
			FindRewards(entity);
		entityinfo.clear();
		SenseSelf(entity);
		SenseEntities(entity);
	}
	
	private void FindRewards(EntityTex entity){
		double hungerdelta = entity.hunger - self.GetParameter("Hunger").GetParameter().value;
		if(hungerdelta < 0){
			rewards.add(new MemReward(TexBrain.RewardType.EatingReward, -hungerdelta, time));
		}
		//TODO
	}
	
	private void SenseSelf(EntityTex entity){
		self = new MemEntity(brain.id, "Tex", time);
		self.AddParameter(new MemParameter("PositionX", new ParameterValue(entity.posX)));
		self.AddParameter(new MemParameter("PositionY", new ParameterValue(entity.posY)));
		self.AddParameter(new MemParameter("PositionZ", new ParameterValue(entity.posZ)));
		self.AddParameter(new MemParameter("Hunger", new ParameterValue(entity.hunger)));
		for(int i=0;i<8;i++){
			ItemStack item = entity.inventory[i];
			if(item != null){
				MemEntity ent = new MemEntity(-i,item.getUnlocalizedName(),time);
				ent.AddProperty("Item");
				//ent.AddParameter(new MemParameter("PositionX", new ParameterValue(entity.posX)));
				//ent.AddParameter(new MemParameter("PositionY", new ParameterValue(entity.posY)));
				//ent.AddParameter(new MemParameter("PositionZ", new ParameterValue(entity.posZ)));
				ent.AddParameter(new MemParameter("PositionInventory", new ParameterValue((((double)i)/8)+(1.0/16.0))));
				entityinfo.add(ent);
			}
		}
	}
	
	private void SenseEntities(EntityTex entity){
		///// Find entities nearby
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
	}
	
	private void AddItem(EntityItem item){
		MemEntity info = new MemEntity(item.getEntityId(),item.getEntityItem().getUnlocalizedName(),time);
		info.AddProperty("Item");
		info.AddParameter(new MemParameter("PositionX", new ParameterValue(item.posX)));
		info.AddParameter(new MemParameter("PositionY", new ParameterValue(item.posY)));
		info.AddParameter(new MemParameter("PositionZ", new ParameterValue(item.posZ)));
		entityinfo.add(info);
	}
	
	public Senses Copy(){
		Senses sense = new Senses(brain);
		sense.time = time;
		sense.rewards.addAll(rewards);
		sense.entityinfo.addAll(entityinfo);
		sense.self = self;
		return sense;
	}
}

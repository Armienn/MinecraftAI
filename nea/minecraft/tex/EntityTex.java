package nea.minecraft.tex;

import nea.minecraft.tex.ai.TexAnalysis;
import nea.minecraft.tex.ai.TexLearning;
import nea.minecraft.tex.ai.TexMainAI;
import nea.minecraft.tex.brain.TexBrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTex extends EntityLiving implements net.minecraft.entity.passive.IAnimals {
	Logger logger = LogManager.getLogger();
	TexBrain brain;
	TexMainAI aithread;
	TexLearning learningthread;
	TexAnalysis analysisthread;
	
	public ItemStack[] inventory = new ItemStack[8];
	public ItemStack[] previousInventory = new ItemStack[8];
	public double hunger = 0;
	
	private NBTTagCompound leashNBTTag;
	
	public EntityTex(World worldIn){
		super(worldIn);
		logger.info("Tex #" + ((Entity)this).getEntityId() + ": Constructor");
		if (!this.worldObj.isRemote){ // if this is server
			brain = new TexBrain(worldObj, ((Entity)this).getEntityId());
			aithread = new TexMainAI(brain);
			boolean useNEAT = false;
			if(useNEAT){
				learningthread = new TexLearning(brain, true);
				learningthread.start();
			}
			else{
				learningthread = new TexLearning(brain, false);
				analysisthread = new TexAnalysis(brain);
				learningthread.start();
				analysisthread.start();
			}
			aithread.start();
		}
	}
	
	public void onUpdate(){
		super.onUpdate();
		if (!this.worldObj.isRemote){ // if this is server
			//update input to ai
			synchronized(brain){
				brain.senses.senses.Update(this);
				brain.senses.rewards.Update(this);
				brain.memory.sensory.UpdateExternal();
			}
			
			//update output to world
			synchronized(brain){
				brain.senses.actions.Update(this);
				brain.memory.sensory.UpdateInternal();
			}
			
			//tell brain that it's been updated
			brain.Updated();
			
			for(int i = 0; i < 8; i++){
				if(!ItemStack.areItemStacksEqual(inventory[i], previousInventory[i])){

                    ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(this.getEntityId(), i, inventory[i]));
                    this.previousInventory[i] = inventory[i] == null ? null : inventory[i].copy();
				}
			}
		}
	}
	
	 /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
    {
        this.inventory[slotIn] = itemStackIn;
    }
	
	public void onDeath(DamageSource cause){
		for(int i = 0; i < inventory.length; i++){
			if(inventory[i] != null){
				this.entityDropItem(inventory[i], 1);
				inventory[i] = null;
			}
		}
		brain.Log("Tex has been killed. All items dropped!");
		brain.Say("Goodbye cruel world!");
		brain.OnDeath();
	}
	
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        NBTTagList var2 = new NBTTagList();
        NBTTagCompound var4;

        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            var4 = new NBTTagCompound();

            if (this.inventory[var3] != null)
            {
                this.inventory[var3].writeToNBT(var4);
            }

            var2.appendTag(var4);
        }

        tagCompound.setTag("TexInventory", var2);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    	super.readEntityFromNBT(tagCompund);

        NBTTagList var2;
        int var3;

        if (tagCompund.hasKey("TexInventory", 9))
        {
            var2 = tagCompund.getTagList("TexInventory", 10);

            for (var3 = 0; var3 < this.inventory.length; ++var3)
            {
                this.inventory[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
            }
        }
    }
}

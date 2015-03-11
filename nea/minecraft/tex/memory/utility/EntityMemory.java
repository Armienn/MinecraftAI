package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Actions;

public class EntityMemory {
	public int id;
	public long appearTime;
	public long disappearTime;
	private String type;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<EntityMemoryParameter> parameters = new ArrayList<EntityMemoryParameter>();
	EntityMemory[] inventory = new EntityMemory[0];
	ArrayList<EntityAction> actions = new ArrayList<EntityAction>();
	
	public long lastUpdate;
	
	public EntityMemory(int id, String type, long appeartime){
		this.id = id;
		this.type = type;
		this.appearTime = appeartime;
		lastUpdate = appeartime;
	}
	
	public void AddProperty(String property){
		properties.add(property);
	}
	
	public void AddParameter(EntityMemoryParameter parameter){
		parameters.add(parameter);
	}
	
	public void SetInventorySpaces(int size){
		inventory = new EntityMemory[size];
	}
	
	public void AddInventory(EntityMemory entity, int index){
		inventory[index] = entity;
	}
	
	public void AddAction(EntityAction action){
		actions.add(action);
	}
	
	public EntityMemoryParameter GetParameter(String type){
		for(EntityMemoryParameter param : parameters){
			if(param.GetType() == type)
				return param;
		}
		return null;
	}
	
	public String[] GetParameters(){
		String[] list = new String[parameters.size()];
		for(int i=0; i<parameters.size();i++){
			list[i] = parameters.get(i).GetType();
		}
		return list;
	}
	
	public String[] GetProperties(){
		return (String[]) properties.toArray();
	}
	
	private EntityMemory GetInInventory(EntityMemory ent){
		if(ent == null) return null;
		for(EntityMemory t : inventory){
			if(t != null && t.id == ent.id && t.disappearTime == 0)
				return t;
		}
		return null;
	}
	
	public int GetInventorySize(){
		return inventory.length;
	}
	
	public EntityMemory GetInventory(int index){
		return inventory[index];
	}
	
	public void Update(EntityMemory memory, long time){
		for(EntityMemoryParameter parameter : parameters){
			EntityMemoryParameter param = memory.GetParameter(parameter.GetType());
			if(param != null)
				parameter.UpdateValue(param.GetParameter(), lastUpdate, time);
		}
		for(int i=0;i<memory.GetInventorySize(); i++){
			EntityMemory currentnew = memory.GetInventory(i);
			EntityMemory currentold = GetInInventory(currentnew);
			if(currentold != null){
				currentold.Update(currentnew, lastUpdate);
			}
			else{
				inventory[i] = currentnew;
			}
		}
		
		lastUpdate = time;
	}
}

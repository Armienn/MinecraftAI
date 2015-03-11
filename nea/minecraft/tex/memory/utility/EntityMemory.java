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
	InventorySlotMemory[] inventory = new InventorySlotMemory[0];
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
		inventory = new InventorySlotMemory[size];
		for(int i=0;i<size;i++){
			inventory[i] = new InventorySlotMemory();
		}
	}
	
	public InventorySlotMemory GetInventorySlot(int index){
		return inventory[index];
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
	
	public int GetInventorySize(){
		return inventory.length;
	}
	
	public EntityMemory GetInventoryContent(int index){
		return inventory[index].GetCurrentItem();
	}
	
	public void Update(EntityMemory memory, long time){
		for(EntityMemoryParameter parameter : parameters){
			EntityMemoryParameter param = memory.GetParameter(parameter.GetType());
			if(param != null)
				parameter.UpdateValue(param.GetParameter(), lastUpdate, time);
		}
		for(int i=0;i<memory.GetInventorySize(); i++){
			EntityMemory currentnew = memory.GetInventoryContent(i);
			inventory[i].Update(currentnew, time);
		}
		
		lastUpdate = time;
	}
}

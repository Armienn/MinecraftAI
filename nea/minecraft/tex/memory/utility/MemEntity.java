package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Actions;

public class MemEntity {
	public int id;
	public long appearTime;
	public long disappearTime;
	private String type;
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<MemParameter> parameters = new ArrayList<MemParameter>();
	ArrayList<MemAction> actions = new ArrayList<MemAction>();
	
	public long lastUpdate;
	
	public MemEntity(int id, String type, long appeartime){
		this.id = id;
		this.type = type;
		this.appearTime = appeartime;
		lastUpdate = appeartime;
	}
	
	public void AddProperty(String property){
		properties.add(property);
	}
	
	public void AddParameter(MemParameter parameter){
		parameters.add(parameter);
	}
	
	/*public void SetInventorySpaces(int size){
		inventory = new InventorySlotMemory[size];
		for(int i=0;i<size;i++){
			inventory[i] = new InventorySlotMemory();
		}
	}
	
	public InventorySlotMemory GetInventorySlot(int index){
		return inventory[index];
	}*/
	
	public void AddAction(MemAction action){
		actions.add(action);
	}
	
	public MemParameter GetParameter(String type){
		for(MemParameter param : parameters){
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
	
	/*public int GetInventorySize(){
		return inventory.length;
	}
	
	public MemEntity GetInventoryContent(int index){
		return inventory[index].GetCurrentItem();
	}*/
	
	public void Update(MemEntity memory, long time){
		for(MemParameter parameter : parameters){
			MemParameter param = memory.GetParameter(parameter.GetType());
			if(param != null)
				parameter.UpdateValue(param.GetParameter(), lastUpdate, time);
		}
		/*for(int i=0;i<memory.GetInventorySize(); i++){
			MemEntity currentnew = memory.GetInventoryContent(i);
			inventory[i].Update(currentnew, time);
		}*/
		
		lastUpdate = time;
	}
}

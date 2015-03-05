package nea.minecraft.tex.memory.utility;

import java.util.ArrayList;

public class EntityMemory {
	public int id;
	public long appearTime;
	public long disappearTime;
	private String type;
	ArrayList<EntityMemoryParameter> parameters = new ArrayList<EntityMemoryParameter>();
	ArrayList<String> properties = new ArrayList<String>();
	
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
	
	public void Update(EntityMemory memory, long time){
		//TODO: figure out where to move this
		/*
		public void Update(ItemInfo item, long time){
			double deltaX = item.posX - CurrentPosX();
			double deltaY = item.posY - CurrentPosY();
			double deltaZ = item.posZ - CurrentPosZ();
			
			double delta = Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaZ);
			if(delta > 0.0001){
				if(movements.size() > 0){
					Movement prevmove = movements.get(movements.size()-1);
					if( prevmove.endTime < lastUpdate ){ // if last movement ended before last update
						movements.add(new Movement(lastUpdate,CurrentPosX(),CurrentPosY(),CurrentPosZ(),time,item.posX,item.posY,item.posZ));
					}
					else { // if last movement was still ongoing last update
						prevmove.endTime = time;
						prevmove.endPosX = item.posX;
						prevmove.endPosY = item.posY;
						prevmove.endPosZ = item.posZ;
					}
				}
				else {
					movements.add(new Movement(lastUpdate,CurrentPosX(),CurrentPosY(),CurrentPosZ(),time,item.posX,item.posY,item.posZ));
				}
			}
			lastUpdate = time;
		}
		 */
	}
}

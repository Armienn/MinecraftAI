package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.memory.Episode;
import nea.minecraft.tex.memory.utility.MemEntity;
import nea.minecraft.tex.memory.utility.MemEvent;
import nea.minecraft.tex.memory.utility.MemParameter;

public class Effect {
	int observations;
	// Conditions that hold for the subject of this effect:
	ArrayList<String> properties = new ArrayList<String>();
	ArrayList<ParameterCondition> parameterConditions = new ArrayList<ParameterCondition>();
	// The change that happens to the subject of this effect: 
	EventEffect eventEffect;
	
	public Effect(SnapEntity entity, ActionMemory memory){
		observations = 1;
		properties.addAll(entity.properties);
		for(ComplexParameter comparam : ComplexParameter.GetComplexParameters(entity, memory)){
			parameterConditions.add(new ParameterCondition(comparam, entity, memory));
		}
	}
	
	public void SetEvent(EventEffect event){
		eventEffect = event;
	}
	
	/**
	 * Checks whether this Effect occurs in the given ActionMemory
	 * @param memory
	 * @return
	 */
	public boolean Fits(ActionMemory memory){
		ArrayList<MemEntity> entities = new ArrayList<MemEntity>();
		for(MemEntity entity : memory.trailingEpisode.entityMemories)
			if(memory.action.interval.startTime <= entity.appearanceInterval.endTime)
				entities.add(entity);
		if(observations == 1){
			entities = FilterByProperties(entities, true);
			entities = FilterByParameters(entities, true);
			//TODO
		}
		else{
			//TODO
		}
		//entities = GetFittingEntities(memory);
		
		//TODO
		return false;
	}
	
	/**
	 * Filters the given list of entities. Only entities with one (loosely=true)
	 * or all (loosely=false) properties remain in the returned list.
	 * @param source
	 * @param loosely
	 * @return A filtered list of entities
	 */
	public ArrayList<MemEntity> FilterByProperties(ArrayList<MemEntity> source, boolean loosely){
		ArrayList<MemEntity> entities = new ArrayList<MemEntity>();
		for(MemEntity entity : source){
			ArrayList<String> props = new ArrayList<String>();
			for(String s : entity.GetProperties()){
				for(String t : properties){
					if(s.equalsIgnoreCase(t)){
						props.add(s);
						break;
					}
				}
			}
			if(loosely){
				if(props.size() > 0)
					entities.add(entity);
			}
			else{
				if(props.size() == properties.size())
					entities.add(entity);
			}
		}
		return entities;
	}
	
	/**
	 * 
	 * @param source
	 * @param loosely
	 * @return
	 */
	public ArrayList<MemEntity> FilterByParameters(ArrayList<MemEntity> source, boolean loosely){
		ArrayList<MemEntity> entities = new ArrayList<MemEntity>();
		
		return entities;
	}
	
	/* *
	 * Returns a list of Entities which loosely fit as subjects for this
	 * Effect. "Loosely" in this case means that only one property needs to
	 * fit, rather than every one.
	 * @param memory
	 * @return
	 *
	private ArrayList<MemEntity> GetFittingEntities(ActionMemory memory) {
		ArrayList<MemEntity> entities = new ArrayList<MemEntity>();
		for(MemEntity entity : memory.trailingEpisode.entityMemories){
			ArrayList<String> props = new ArrayList<String>();
			for(String s : entity.GetProperties()){
				for(String t : properties){
					if(s.equalsIgnoreCase(t)){
						props.add(s);
						break;
					}
				}
			}
			if(props.size() > 0){
				entities.add(entity);
			}
		}
		for(entity)
		return entities;
	}///*///
	
	private static ArrayList<Effect> GetEffects(MemEntity entity, ActionMemory memory){
		ArrayList<Effect> effects = new ArrayList<Effect>();
		if(memory.action.interval.startTime <= entity.appearanceInterval.startTime){ // all entities that appear
			Effect effect = new Effect(new SnapEntity(entity), memory);
			effect.SetEvent(new EventEffect(EventEffect.EventType.Appearance));
			effects.add(effect);
		}
		else if(memory.action.interval.startTime <= entity.appearanceInterval.endTime && entity.appearanceInterval.endTime < memory.trailingEpisode.interval.endTime){ // all entities that disappear
			Effect effect = new Effect(new SnapEntity(entity), memory);
			effect.SetEvent(new EventEffect(EventEffect.EventType.Disappearance));
			effects.add(effect);
		}
		
		for(String s : entity.GetParameters()){
			MemParameter param = entity.GetParameter(s);
			for(MemEvent event : param.GetEvents()){
				if(memory.action.interval.startTime <= event.interval.startTime){
					Effect effect = new Effect(new SnapEntity(entity), memory);
					effect.SetEvent(new EventEffect(s, event, memory));
					effects.add(effect);
				}
			}
		}
		
		return effects;
	}
	
	public static ArrayList<Effect> GetEffects(ActionMemory memory){
		ArrayList<Effect> effects = new ArrayList<Effect>();
		
		for(MemEntity entity : memory.trailingEpisode.entityMemories){
			effects.addAll(GetEffects(entity, memory));
		}
		effects.addAll(GetEffects(memory.trailingEpisode.selfMemory, memory));
		
		return effects;
	}

	@Override
	public String toString(){
		return toString(0);
	}
	
	public String toString(int level){
		String tab = "";
		for(int i=0; i<level; i++){
			tab += "\t";
		}
		if(level > 2) return "Effect";
		
		String result = "Effect - \n";
		result += tab + "\tProperties " + properties + "\n";
		for(int i=0; i<parameterConditions.size(); i++){
			result += tab + "\t" + i + ": " + parameterConditions.get(i).toString(level+1) + "\n";
		}
		result += tab + "\t" + eventEffect.toString(level+1) + "\n";
		result += tab + "\tObservations " + observations;
		return result;
	}
}

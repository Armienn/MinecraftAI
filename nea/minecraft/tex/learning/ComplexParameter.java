package nea.minecraft.tex.learning;

import java.util.ArrayList;

import nea.minecraft.tex.interaction.Action;
import nea.minecraft.tex.memory.MemorySnapshot;

public class ComplexParameter {
	public final ComplexType type;
	public final String parameterPrimary;
	public final String parameterSecondary;
	
	public ComplexParameter(String primary){
		type = ComplexType.Basic;
		parameterPrimary = primary;
		parameterSecondary = null;
	}
	
	public ComplexParameter(String primary, boolean actiondelta){
		type = actiondelta ? ComplexType.ActionDelta : ComplexType.Basic;
		parameterPrimary = primary;
		parameterSecondary = null;
	}
	
	public ComplexParameter(String primary, String secondary, boolean inter){
		type = inter ? ComplexType.InterDelta : ComplexType.IntraDelta;
		parameterPrimary = primary;
		parameterSecondary = secondary;
	}
	
	public ComplexParameter(){
		type = ComplexType.DistanceToAI;
		parameterPrimary = null;
		parameterSecondary = null;
	}
	
	public static ComplexParameter[] GetComplexParameters(SnapEntity entity, ActionMemory memory){
		ArrayList<ComplexParameter> parameters = new ArrayList<ComplexParameter>();
		
		AddBasics(parameters, entity, memory);
		AddActionDeltas(parameters, entity, memory);
		//AddIntraDeltas(parameters, entity, memory); // we don't expect to actually learn anything useful from this, so lets skip it for now
		AddInterDeltas(parameters, entity, memory);
		//Finally distance
		parameters.add(new ComplexParameter());
		ComplexParameter[] result = new ComplexParameter[parameters.size()];
		parameters.toArray(result);
		return result;
	}
	
	public double Evaluate(SnapEntity entity, ActionMemory memory){
		double one, two, three;
		switch(type){
		case Basic:
			return entity.GetParameter(parameterPrimary).value.value;
		case ActionDelta:
			one = memory.action.parameters[0];
			two = entity.GetParameter(parameterPrimary).value.value;
			return one-two;
		case IntraDelta:
			one = entity.GetParameter(parameterPrimary).value.value;
			two = entity.GetParameter(parameterSecondary).value.value;
			return one-two;
		case InterDelta:
			one = entity.GetParameter(parameterPrimary).value.value;
			two = memory.snapshot.self.GetParameter(parameterPrimary).value.value;
			return one-two;
		case DistanceToAI:
			try{
				one = Math.pow(entity.GetParameter("PositionX").value.value - memory.snapshot.self.GetParameter("PositionX").value.value, 2);
				two = Math.pow(entity.GetParameter("PositionY").value.value - memory.snapshot.self.GetParameter("PositionY").value.value, 2);
				three = Math.pow(entity.GetParameter("PositionZ").value.value - memory.snapshot.self.GetParameter("PositionZ").value.value, 2);
				return Math.sqrt(one+two+three);
			}
			catch(Exception e){
				return 0;
			}
		default:
			throw new RuntimeException("Is this even a possible error?");
		}
	}
	
	public double EvaluateVelocity(SnapEntity entity, ActionMemory memory){
		double one, two, three;
		switch(type){
		case Basic:
			return entity.GetParameter(parameterPrimary).GetVelocity();
		case ActionDelta:
			throw new RuntimeException("Invalid velocity");
		case IntraDelta:
			one = entity.GetParameter(parameterPrimary).GetVelocity();
			two = entity.GetParameter(parameterSecondary).GetVelocity();
			return one-two;
		case InterDelta:
			one = entity.GetParameter(parameterPrimary).GetVelocity();
			two = memory.snapshot.self.GetParameter(parameterPrimary).GetVelocity();
			return one-two;
		case DistanceToAI:
			try{
				one = Math.pow(entity.GetParameter("PositionX").GetVelocity() - memory.snapshot.self.GetParameter("PositionX").GetVelocity(), 2);
				two = Math.pow(entity.GetParameter("PositionY").GetVelocity() - memory.snapshot.self.GetParameter("PositionY").GetVelocity(), 2);
				three = Math.pow(entity.GetParameter("PositionZ").GetVelocity() - memory.snapshot.self.GetParameter("PositionZ").GetVelocity(), 2);
				return Math.sqrt(one+two+three);
			}
			catch(Exception e){
				return 0;
			}
		default:
			throw new RuntimeException("Is this even a possible error?");
		}
	}
	
	public boolean DefinesVelocity(){
		if(type == ComplexType.ActionDelta)
			return false;
		return true;
	}
	
	private static void AddBasics(ArrayList<ComplexParameter> list, SnapEntity entity, ActionMemory memory){
		for(SnapParameter param : entity.parameters){
			list.add(new ComplexParameter(param.GetType()));
		}
	}
	
	private static void AddActionDeltas(ArrayList<ComplexParameter> list, SnapEntity entity, ActionMemory memory){
		if(Action.DegreesOfFreedom(memory.action.type) < 1)
			return;
		for(SnapParameter param : entity.parameters){
			String primarytype = param.GetType();
			if(primarytype.equalsIgnoreCase("positionInventory")) //remove this line to add actiondelta for all parameters
				list.add(new ComplexParameter(primarytype, true));
		}
	}
	
	private static void AddIntraDeltas(ArrayList<ComplexParameter> list, SnapEntity entity, ActionMemory memory){
		for(int i=0; i<entity.parameters.size(); i++){
			SnapParameter param = entity.parameters.get(i);
			String primarytype = param.GetType();
			for(int j=i+1; j<entity.parameters.size(); j++){
				String secondarytype = entity.parameters.get(j).GetType();
				list.add(new ComplexParameter(primarytype, secondarytype, false));
			}
		}
	}
	
	private static void AddInterDeltas(ArrayList<ComplexParameter> list, SnapEntity entity, ActionMemory memory){
		for(SnapParameter param : entity.parameters){
			for(SnapParameter paramtwo : memory.snapshot.self.parameters){
				if(param.GetType().equalsIgnoreCase(paramtwo.GetType())) //remove this line to add interdelta for all parameters
					list.add(new ComplexParameter(param.GetType(), paramtwo.GetType(), true));
			}
		}
	}
	
	public enum ComplexType { Basic, ActionDelta, IntraDelta, InterDelta, DistanceToAI }
}

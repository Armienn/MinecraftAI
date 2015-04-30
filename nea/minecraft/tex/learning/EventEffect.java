package nea.minecraft.tex.learning;

public class EventEffect {
	public final EventType type;
	public final ComplexParameter parameter;
	//valuestuff
	
	public EventEffect(EventType type){
		this.type = type;
		parameter = null;
	}
	
	public EventEffect(EventType type, ComplexParameter parameter){
		this.type = type;
		this.parameter = parameter;
	}
	
	public enum EventType { Appearance, Disappearance, Step, Velocity }
}

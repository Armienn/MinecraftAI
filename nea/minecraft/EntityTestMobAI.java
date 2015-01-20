package nea.minecraft;

import java.sql.Time;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMobAI extends Thread {
	Logger logger = LogManager.getLogger();
	public SensesTestMob senses;
	public BehaveTestMob behaviour;
	
	public EntityTestMobAI(){
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Constructor!"));
		behaviour = new BehaveTestMob();
		senses = new SensesTestMob();
		senses.lastupdate = System.currentTimeMillis();
	}

	public void run(){
		while(timeSinceLastUpdate() < 1000){
			logger.info("Custom AI update");
			try {
				walkAndJump();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("Ending custom AI thread");
	}
	
	private long timeSinceLastUpdate(){
		long result = 0;
		synchronized(senses){
			result = System.currentTimeMillis() - senses.lastupdate;
		}
		return result;
	}
	
	private void walkAndJump(){
		synchronized(behaviour){
			behaviour.motionZ = 0.1;
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(behaviour){
			behaviour.motionY = 0.45; //jumps one block
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
		}
		
	}
}

package nea.minecraft;

import java.sql.Time;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMobAI extends Thread {
	Logger logger = LogManager.getLogger();
	public SensesTestMob senses;
	public BehaveTestMob behaviour;
	
	public EntityTestMobAI(World worldIn){
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Constructor!"));
		behaviour = new BehaveTestMob();
		senses = new SensesTestMob(worldIn);
		senses.lastupdate = System.currentTimeMillis();
	}

	public void run(){
		while(timeSinceLastUpdate() < 1000){
			logger.info("Custom AI update");
			//try {
				walkInSquares();
			//	Thread.sleep(2000);
			//} catch (InterruptedException e) {
			//	e.printStackTrace();
			//}
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
	
	private void walkInSquares(){
		synchronized(behaviour){
			behaviour.fly = false;
			behaviour.motionZ = 0.1;
			behaviour.motionX = 0.0;
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
			behaviour.motionX = 0.1;
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized(behaviour){
			behaviour.motionZ = -0.1;
			behaviour.motionX = 0.0;
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
			behaviour.motionX = -0.1;
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void walkAndJump(){
		synchronized(behaviour){
			behaviour.fly = false;
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
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(behaviour){
			behaviour.fly = true;
			behaviour.motionY = 0.3;
		}
		
	}
}

package nea.minecraft;

import java.sql.Time;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMobAI extends Thread {
	Logger logger = LogManager.getLogger();
	int id;
	public SensesTestMob senses;
	public BehaveTestMob behaviour;
	
	public EntityTestMobAI(World worldIn, int id){
		this.id = id;
		say("Constructor!");
		behaviour = new BehaveTestMob();
		senses = new SensesTestMob(worldIn);
		senses.lastupdate = System.currentTimeMillis();
	}

	public void run(){
		while(timeSinceLastUpdate() < 1000){
			logger.info("Custom AI update");
			//walkInSquares();
			tellOfBlocksHere();
			trySleep(3000);
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
	
	private void tellOfBlocksHere(){
		synchronized(senses){
			say("At -2: " + senses.GetBlockAtRelativePosition(0, -2, 0).getUnlocalizedName());
			say("At -1: " + senses.GetBlockAtRelativePosition(0, -1, 0).getUnlocalizedName());
			say("At 0: " + senses.GetBlockAtRelativePosition(0, 0, 0).getUnlocalizedName());
			say("At 1: " + senses.GetBlockAtRelativePosition(0, 1, 0).getUnlocalizedName());
			say("At 2: " + senses.GetBlockAtRelativePosition(0, 2, 0).getUnlocalizedName());
			say("At 3: " + senses.GetBlockAtRelativePosition(0, 3, 0).getUnlocalizedName());
		}
	}
	
	private void walkInSquares(){
		synchronized(behaviour){
			behaviour.fly = false;
			behaviour.motionZ = 0.1;
			behaviour.motionX = 0.0;
		}
		trySleep(3000);
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
			behaviour.motionX = 0.1;
		}
		trySleep(3000);
		synchronized(behaviour){
			behaviour.motionZ = -0.1;
			behaviour.motionX = 0.0;
		}
		trySleep(3000);
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
			behaviour.motionX = -0.1;
		}
		trySleep(3000);
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
			behaviour.motionX = 0.0;
		}
	}
	
	private void walkAndJump(){
		synchronized(behaviour){
			behaviour.fly = false;
			behaviour.motionZ = 0.1;
		}
		trySleep(5000);
		synchronized(behaviour){
			behaviour.motionY = 0.45; //jumps one block
		}

		trySleep(500);
		synchronized(behaviour){
			behaviour.motionZ = 0.0;
		}
		
		trySleep(2000);
		synchronized(behaviour){
			behaviour.fly = true;
			behaviour.motionY = 0.3;
		}
		
	}
	
	private void say(String text){
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("TestMob-" + id + ": " + text));
	}
	
	private boolean trySleep(long milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

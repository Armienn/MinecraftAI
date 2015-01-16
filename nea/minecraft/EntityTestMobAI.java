package nea.minecraft;

import java.sql.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTestMobAI extends Thread {
	Logger logger = LogManager.getLogger();
	public SensesTestMob senses;
	public BehaveTestMob behaviour;
	
	public EntityTestMobAI(){
		behaviour = new BehaveTestMob();
		senses = new SensesTestMob();
		senses.lastupdate = System.currentTimeMillis();
	}

	public void run(){
		while(timeSinceLastUpdate() < 1000){
			logger.info("Custom AI update");
			try {
				Thread.sleep(500);
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
}

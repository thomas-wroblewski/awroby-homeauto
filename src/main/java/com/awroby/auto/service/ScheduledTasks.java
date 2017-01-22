package com.awroby.auto.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.awroby.auto.config.Properties;
import com.awroby.auto.dao.OutletRepository;
import com.awroby.auto.objects.Outlet;

@Service
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired private Properties properties;
	@Autowired private RaspPiInterface commands;
	@Autowired private OutletRepository outletRepo;
	
	private int timeout = 1000 * 60 * 10;
	private boolean isSomeoneHome = false;
	private long lastCheckIn = System.currentTimeMillis();
	

	private Map<String, Long> timeoutMap = new ConcurrentHashMap<String, Long>();
	private int leftHomeTimeout = 1000 * 60 * 5;
	
	
	public void updateUsedTime(String id){
		if(timeoutMap.containsKey(id)){
			logger.info("Key already exists in map - updating time");
			timeoutMap.replace(id, System.currentTimeMillis());
		}else{
			logger.info("Adding time for auto off tracking");
			timeoutMap.put(id, System.currentTimeMillis());
		}
	}
	
	public void removeEntry(String id){
		if(timeoutMap.containsKey(id)){
			timeoutMap.remove(id);
		}
	}
	
	public void autoTurnOn(){
		if(!isSomeoneHome){
			isSomeoneHome = true;
			
			List<Outlet> outlets = outletRepo.findAll();
			for(Outlet o: outlets){
				
				commands.turnOnOutlet(o.getOnCode(), o.getPulse());
				timeoutMap.put(o.getId(), System.currentTimeMillis());
				o.setRunning(true);
				outletRepo.save(o);
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Scheduled(initialDelay=1000, fixedRate=60000)
	public void hbtLatch(){
		if(isSomeoneHome){
			if(System.currentTimeMillis() - this.lastCheckIn > leftHomeTimeout){
				logger.info("HBT not recieved - left home");
				isSomeoneHome = false;
			}
		}
	}

	
	@Scheduled(initialDelay=1000, fixedRate=60000)
	public void autoShutoff(){
		if(!isSomeoneHome && !properties.isDisableTimeCheck()){
			for (String key : timeoutMap.keySet()) {			
				Outlet o = outletRepo.findById(key);
			    long time = timeoutMap.get(key);
			    logger.info("time: " + time );
			    logger.info(o.toString());
			    
			    if(System.currentTimeMillis() - time > timeout && o.isRunning()){
			    	logger.info("Outlet was left on - shutting down");
			    	
			    	commands.turnOffOutlet(o.getOffCode(), o.getPulse());
			    	o.setRunning(false);
			    	outletRepo.save(o);
			    	timeoutMap.remove(key);
			    }else if(System.currentTimeMillis() - time > timeout && !o.isRunning()){
			    	logger.info("Outlet was turned off");
			    	timeoutMap.remove(key);
			    }
			}
		}
	}

	public boolean isSomeoneHome() {
		return isSomeoneHome;
	}

	public void setSomeoneHome(boolean isSomeoneHome) {
		this.isSomeoneHome = isSomeoneHome;
	}
	
	public long getLastCheckIn() {
		return lastCheckIn;
	}

	public void setLastCheckIn(long lastCheckIn) {
		this.lastCheckIn = lastCheckIn;
	}

}

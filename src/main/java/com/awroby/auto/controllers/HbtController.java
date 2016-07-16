package com.awroby.auto.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.service.ScheduledTasks;

@RestController
public class HbtController {

	private static final Logger logger = LoggerFactory.getLogger(HbtController.class);
	@Autowired ScheduledTasks schedule;
	
	@RequestMapping(value="/hbt", method=RequestMethod.POST)
	public String receiveHbt(){
		
		logger.info("HBT received");
		//schedule.setSomeoneHome(true);
		schedule.autoTurnOn();
		schedule.setLastCheckIn(System.currentTimeMillis());
		return "hbt received";
	}
	
	
	
}

package com.awroby.auto.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.objects.Sound;
import com.awroby.auto.service.ScheduledTasks;

@RestController
public class SoundController {

	
	private static final Logger logger = LoggerFactory.getLogger(HbtController.class);
	@Autowired ScheduledTasks schedule;
	
	
	@RequestMapping(value="/sound", method=RequestMethod.POST)
	public String receiveHbt(){
		
		logger.info("Sound triggered");
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Sound klaxon = new Sound("http://192.168.0.10:8081/homeauto/klaxon_siren.mp3");
				Sound klaxon =  new Sound("/home/pi/horn.mp3");
				klaxon.play();
			}
		});
		
		return "sound triggered";
	}
}

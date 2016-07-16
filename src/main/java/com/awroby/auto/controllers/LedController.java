package com.awroby.auto.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.service.RaspPiInterface;
@RestController
public class LedController {

	private static final Logger logger = LoggerFactory.getLogger(RfOutletController.class);
	
	@Autowired private RaspPiInterface pi;
	
	@RequestMapping(value="/toggleLed/{color}", method=RequestMethod.GET)
	public String toggleLed(@PathVariable("color") String led){
		
		if(led.matches("red")){
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					logger.info("Toggling Red LED");
					pi.toggleLED("ledRed");
				}
			});
			t.start();
		}else if(led.matches("green")){
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					logger.info("Toggling Green LED");
					pi.toggleLED("ledGreen");
				}
			});
			t.start();
		}else if(led.matches("blue")){
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					logger.info("Toggling Blue LED");
					pi.toggleLED("ledBlue");		
				}
			});
			t.start();
		}else{
			return "fail - wrong color";
		}

		return "success";
	}
	
	
	@RequestMapping(value="/triggerPWM", method=RequestMethod.GET)
	public String triggerPWN(){
		
		
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					logger.info("Trigger PWM");
					pi.triggerPWM();
				}
			});
			t.start();
		

		return "success";
	}
}

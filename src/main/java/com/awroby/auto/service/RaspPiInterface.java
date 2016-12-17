package com.awroby.auto.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.awroby.auto.config.Properties;
import com.awroby.auto.dao.OutletRepository;
import com.awroby.auto.objects.Outlet;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

@Service
public class RaspPiInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(RaspPiInterface.class);
	 
	//This uses wiring pi convention
	//To view appropriate pin numbering convention run the following
	//sudo ~/wiringPi/gpio/gpio readall
    private final GpioController gpio = GpioFactory.getInstance(); // create gpio controller
    final GpioPinDigitalOutput ledRed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "ledRed", PinState.HIGH);
    final GpioPinDigitalOutput ledGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "ledGreen", PinState.HIGH);
    final GpioPinDigitalOutput ledBlue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "ledBlue", PinState.HIGH);
    final GpioPinDigitalInput pushButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "pushButton", PinPullResistance.PULL_UP);
    final int pwmPin = 26; 
    
	@Autowired private OutletRepository outletRepo;
	@Autowired private ScheduledTasks tasks;
	@Autowired private SlackIntegrations slack;
	@Autowired Properties props;
	
	@PostConstruct
	public void init(){
		String command = "/var/www/rfoutlet/codesend";
		
		for(Outlet outlet : outletRepo.findAll()){
			logger.info("Turning off outlet: " + outlet.getName());
			this.executePiScriptCommand(command + " " + outlet.getOffCode() + " -i " + outlet.getPulse());
			outlet.setRunning(false);
			//tasks.updateUsedTime(outlet.getId());
			outletRepo.save(outlet);
		}
		
		//Provision PWM
		Gpio.wiringPiSetup();
		SoftPwm.softPwmCreate(pwmPin, 0, 100);
		
		//Provision GPIO Pins
		ledRed.setShutdownOptions(true, PinState.LOW);
		ledRed.low();
		ledGreen.setShutdownOptions(true, PinState.LOW);
		ledGreen.low();
		ledBlue.setShutdownOptions(true, PinState.LOW);
		ledBlue.low();
		
		pushButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                logger.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try{
                	if(event.getState().isLow()){ 
//                		toggleLED("ledBlue"); 
                		slack.sendWebHook("WebHook", ":rat:", props.getButtonChannel(), "<http://frontdoor.lexray.com/mjpg/video.mjpg|Someone's Here - Click to find out who>");
                		blinker();              
                	}
                }catch(Exception ex){
                	ex.printStackTrace();
                }
            }
        });
		
		//Setup Button Blinker
		ledGreen.high();
    
	}
	
	public void blinker(){
		//Using the Green Pin as its convenient 
		try{
			for(int i = 0; i < 25; i++){
				
				ledGreen.low();
				Thread.sleep(500);
				ledGreen.high();
				Thread.sleep(500);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void setupPushButton(){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
//					logger.info(pushButton.getState().toString());
					if(pushButton.isHigh()){
//						triggerPWM();
						toggleLED("ledBlue");
					}
				}
			}
			
		});
		t.start();
	}
	
	public void triggerPWM(){
		try{
			for (int i=0; i <100; i++){
				logger.info("Turn");
				SoftPwm.softPwmWrite(pwmPin, i);
				Thread.sleep(25);
			}
			
			
			for(int i=100; i > 0; i--){
				SoftPwm.softPwmWrite(pwmPin, i);
				Thread.sleep(25);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void toggleLED(String led){
		
		try{
			if(led.matches("ledRed")){
				logger.info("togging on");
				ledRed.high();
				Thread.sleep(5000);
				logger.info("togging off");
				ledRed.low();
			}else if(led.matches("ledGreen")){
				logger.info("togging on");
				ledGreen.high();
				Thread.sleep(5000);
				logger.info("togging off");
				ledGreen.low();
				
			}else if(led.matches("ledBlue")){
				
				logger.info("togging on");
				ledBlue.high();
				Thread.sleep(5000);
				logger.info("togging off");
				ledBlue.low();
//				for (int i=0; i <100; i++){
//					logger.info("Turn");
//					SoftPwm.softPwmWrite(pwmPin, i);
//					Thread.sleep(25);
//				}
//				
//				
//				for(int i=100; i > 0; i--){
//					SoftPwm.softPwmWrite(pwmPin, i);
//					Thread.sleep(25);
//				}
				
			}else{
				logger.error("Incorrect LED Name - Ignore");
			}
			
			//gpio.shutdown();
		}catch(Exception ex){
			logger.error("Problem toggling LED: " + led);
		}
	}
	
	
	
	
	
	public void turnOffOutlet(int code, int pulse){
		try{
			String command = "/var/www/rfoutlet/codesend";
			this.executePiScriptCommand(command + " " + code + " -i " + (pulse-1));
			Thread.sleep(1000);
			this.executePiScriptCommand(command + " " + code + " -i " + pulse);
			Thread.sleep(1000);
			this.executePiScriptCommand(command + " " + code + " -i " + (pulse+1));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void turnOnOutlet(int code, int pulse){
		try{
			String command = "/var/www/rfoutlet/codesend";
			this.executePiScriptCommand(command + " " + code + " -i " + (pulse-1));
			Thread.sleep(1000);
			this.executePiScriptCommand(command + " " + code + " -i " + pulse);
			Thread.sleep(1000);
			this.executePiScriptCommand(command + " " + code + " -i " + (pulse+1));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String executePiScriptCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
}

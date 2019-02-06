package com.awroby.auto.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedSignService {

	private String scriptPath = "/home/pi/Led.pl"; 
	private static final Logger logger = LoggerFactory.getLogger(LedSignService.class);
	@Autowired ScheduledTasks scheduler;

	
	public void DisplayMessage(String msg, String color, String effect) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder();
		
		
		
		builder.command("perl", this.scriptPath, msg, this.getColor(color), this.getEffect(effect));
		
		//builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();

		//Process process = Runtime.getRuntime().exec("perl " + this.scriptPath + " " + msg + " " + this.getColor(color) + " " + this.getEffect(effect));
		StreamGobbler streamGobbler = 
				  new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = process.waitFor();
		
		if(exitCode != 0) {
			throw new IOException();
		}
		
		scheduler.setLedLatch(System.currentTimeMillis());
		scheduler.setSwitched(true);
		
		
	}
	
	private String getColor(String subColor) {
		String color = "";
		switch(subColor.toUpperCase()) {
		
			case "RED":
				color = "RED";
				
				break;
			case "BRIGHTRED":
				color = "BRIGHTRED";
				break;
			case "ORANGE":
				color = "ORANGE";
				break;
			case "BRIGHTORANGE":
				color = "BRIGHTORANGE";
				break;
			case "YELLOW":
				color = "YELLOW";
				break;
			case "BRIGHTYELLOW":
				color = "BRIGHTYELLOW";
				break;
			case "GREEN":
				color = "GREEN";
				break;
			case "BRIGHTGREEN":
				color = "BRIGHTGREEN";
				break;
			case "LAYERMIX":
				color = "LAYERMIX";
				break;
			case "BRIGHTLAYERMIX":
				color = "BRIGHTLAYERMIX";
				break;
			case "VERTICALMIX":
				color = "VERTICALMIX";
				break;
			case "SAWTOOTHMIX":
				color = "SAWTOOTHMIX";
				break;
			case "REDONGREEN":
				color = "REDONGREEN";
				break;
			case "YELLOWONGREEN":
				color = "YELLOWONGREEN";
				break;
			default:
				color = "BRIGHTRED";
				logger.error("Fell into Color Default");
				break;
		}
		
		logger.info("Setting Color = " + color);
		
		return color;
	}
	private String getEffect(String subEffect) {
		String effect = "";
		
		switch (subEffect.toUpperCase()){
		case "AUTO":
			effect = "AUTO";
			break;
		case "COVERDOWN":
			effect = "COVERDOWN";
			break;
		case "COVERFROMCENTER":
			effect = "COVERFROMCENTER";
			break;
		case "COVERFROMLEFT": 
			effect = "COVERFROMLEFT";
			break;
		case "COVERFROMRIGHT":
			effect = "COVERFROMRIGHT";
			break;
		case "COVERTOCENTER":
			effect = "COVERTOCENTER";
			break;
		case "COVERUP":
			effect = "COVERUP";
			break;
		case "CYCLIC":
			effect = "CYCLIC";
			break;
		case "EXPLODE":
			effect = "EXPLODE";
			break;
		case "FLASH":
			effect = "FLASH";
			break;
		case "IMMEDIATE":
			effect = "IMMEDIATE";
			break;
		case "INTERLACE1":
			effect = "INTERLACE1";
			break;
		case "INTERLACE2":
			effect = "INTERLACE2";
			break;
		case "OPENFROMCENTER":
			effect = "OPENFROMCENTER";
			break;
		case "OPENFROMLEFT":
			effect = "OPENFROMLEFT";
			break;
		case "OPENFROMRIGHT":
			effect = "OPENFROMRIGHT";
			break;			
		case "OPENTOCENTER":
			effect = "OPENTOCENTER";
			break;
		case "PACMAN":
			effect = "PACMAN";
			break;
		case "RANDOM":
			effect = "RANDOM";
			break;
		case "SCANLINE":
			effect = "SCANLINE";
			break;
		case "SCROLLDOWN":
			effect = "SCROLLDOWN";
			break;
		case "SCROLLUP":
			effect = "SCROLLUP";
			break;
		case "SHOOT":
			effect = "SHOOT";
			break;
		case "SLIDEIN":
			effect = "SLIDEIN";
			break;
		case "STACK":
			effect = "STACK";
			break;
		default:
			effect = "AUTO";
			logger.error("Fell into Color Default");
			break;
		}
		logger.info("Setting Effect = " + effect);
		return effect;
	}
}

class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;
 
    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }
 
    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
          .forEach(consumer);
    }
}
package com.awroby.auto.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.config.Properties;
import com.awroby.auto.dao.OutletRepository;
import com.awroby.auto.objects.Outlet;
import com.awroby.auto.service.RaspPiInterface;
import com.awroby.auto.service.ScheduledTasks;

@RestController
public class RfOutletController {
	private static final Logger logger = LoggerFactory.getLogger(RfOutletController.class);
	
	@Autowired private OutletRepository outletRepo;
	@Autowired private RaspPiInterface commands;
	@Autowired private ScheduledTasks tasks;
	@Autowired private Properties props;
	
	@RequestMapping(value="resetOutlet", method=RequestMethod.GET)
	public String resetOutletCodes(){
		String command = "/var/www/rfoutlet/codesend";
		for(Outlet outlet : outletRepo.findAll()){
			logger.info("Turning off outlet: " + outlet.getName());
			commands.executePiScriptCommand(command + " " + outlet.getOffCode() + " -i " + outlet.getPulse());
			outlet.setRunning(false);
			outletRepo.save(outlet);
		}
		
		outletRepo.deleteAll();
		
//		outletRepo.save(new Outlet(4265267, 4265276, 188, "Outlet 1"));
//		outletRepo.save(new Outlet(4265411, 4265420, 188, "Outlet 2"));
//		outletRepo.save(new Outlet(4265731, 4265740, 188, "Outlet 3"));
//		outletRepo.save(new Outlet(4267267, 4267276, 188, "Outlet 4"));
//		outletRepo.save(new Outlet(4273411, 4273420, 188, "Outlet 5"));
		
		for(String o : props.getOutlets()){
			try{
				String[] split = o.split(";");
				Outlet outlet = new Outlet(Integer.valueOf(split[1]),Integer.valueOf(split[2]), Integer.valueOf(split[3]), split[0]);
				commands.executePiScriptCommand(command + " " + outlet.getOffCode() + " -i " + outlet.getPulse());
				outlet.setRunning(false);
				logger.info("Added outlet: " + outlet.toString());
				outletRepo.save(outlet);
			}catch(Exception ex){
				logger.error("Bad Outlet Property");
				logger.error("Outlet: <name;onCode;offCode;pulse>");
				logger.error("Outlet: " + o);
			}
		}
		
		
		return "success";
	}
		
	@RequestMapping(value="/rfoutlet", method=RequestMethod.GET)
	public String getOutlets(){
		

		JSONObject json = new JSONObject();
		JSONArray outlets = new JSONArray();
		
		for(Outlet outlet : outletRepo.findAll()){
			JSONObject o = new JSONObject();
			o.put("on", outlet.getOnCode());
			o.put("off", outlet.getOffCode());
			o.put("pulse", outlet.getPulse());
			o.put("name", outlet.getName());
			o.put("id", outlet.getId());
			o.put("running", outlet.isRunning());
			outlets.put(o);
		}

		json.put("outlets", outlets);
		
		
		
		return json.toString();
	}
	
	
	@RequestMapping(value="/rfoutlet", method=RequestMethod.POST)
	public String flipOutlets(@RequestBody byte[] request){
		System.out.println("in post");
		try{
			String command = "/var/www/rfoutlet/codesend";
			logger.info("request = " + new String(request));
			System.out.println("in post");
			JSONObject outlet = new JSONObject(new String(request));
			
			Outlet o = outletRepo.findById(outlet.getString("id"));
			
			int code = outlet.getInt("code");
			int pulse = outlet.getInt("pulse");
			
			
			if(code == o.getOnCode()){
				logger.info("Turning on " + o.getName());
				commands.turnOnOutlet(code, pulse);
				o.setRunning(true);
				tasks.updateUsedTime(o.getId());
			}else{
				logger.info("Turning off " + o.getName());
				commands.turnOffOutlet(code, pulse);
				tasks.removeEntry(o.getId());
				o.setRunning(false);
			}
			
			
			
			outletRepo.save(o);
		}catch(Exception ex){
			ex.printStackTrace();
			return "failed";
		}
		
		
		return "success";
	}

}

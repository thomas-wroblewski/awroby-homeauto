package com.awroby.auto.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.dao.OutletRepository;
import com.awroby.auto.objects.Outlet;

@RestController
public class OutletRestController {


	private static final Logger logger = LoggerFactory.getLogger(OutletRestController.class);
	@Autowired private OutletRepository outletRepo;
	
	@RequestMapping(value="/outlets", method=RequestMethod.GET)
	public String get(){
		
		logger.info("Get Outlets");
		JSONArray json = new JSONArray();
		
		List<Outlet> outlets = outletRepo.findAll();
		
		for(Outlet o: outlets){
			JSONObject j = new JSONObject();
			j.put("id", o.getId());
			j.put("name", o.getName());
			j.put("onCode", o.getOnCode());
			j.put("offCode", o.getOffCode());
			j.put("pulse", o.getPulse());
			j.put("running", o.isRunning());
			
			json.put(j);
		}
		
		return json.toString();
	}
	
	@RequestMapping(value="/outlets", method=RequestMethod.POST)
	public String post(@RequestBody String request){
		
		logger.info("Create Outlets");
		
		
		//logger.info(request);
		//Outlet o = new Outlet(int onCode, int offCode, int pulse, String name))
		JSONObject json = new JSONObject(request);
		int onCode = json.getInt("onCode");
		int offCode = json.getInt("offCode");
		int pulse = json.getInt("pulse");
		String name = json.getString("name");
		

		
		outletRepo.save(new Outlet(onCode, offCode, pulse, name));
		
		
		//JSONArray json = new JSONArray();
		
		return json.toString();
	}
	
	@RequestMapping(value="/outlets/{id}", method=RequestMethod.GET)
	public String getSingle(@PathVariable(value="id") final String id){
		
		logger.info("Get Outlet");
		
		
		JSONObject j = new JSONObject();
		Outlet o = outletRepo.findById(id);
		
		
		
		j.put("id", o.getId());
		j.put("name", o.getName());
		j.put("onCode", o.getOnCode());
		j.put("offCode", o.getOffCode());
		j.put("pulse", o.getPulse());
		j.put("running", o.isRunning());
		

		
		return j.toString();
	}
	
	@RequestMapping(value="/outlets/{id}", method=RequestMethod.PUT)
	public String edit(@PathVariable(value="id") final String id, @RequestBody String request){
		
		logger.info("Edit Outlets");
		
		JSONObject json = new JSONObject(request);
		int onCode = json.getInt("onCode");
		int offCode = json.getInt("offCode");
		int pulse = json.getInt("pulse");
		String name = json.getString("name");
		
		Outlet o = outletRepo.findById(id);
		o.setName(name);
		o.setOnCode(onCode);
		o.setOffCode(offCode);
		o.setPulse(pulse);
		
		outletRepo.save(o);

		
		return json.toString();
	}
	
	@RequestMapping(value="/outlets/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable(value="id") final String id){
		
		logger.info("Delete Outlets");
		JSONArray json = new JSONArray();
		
		Outlet o = outletRepo.findById(id);
		outletRepo.delete(o);
	
		return json.toString();
	}
}

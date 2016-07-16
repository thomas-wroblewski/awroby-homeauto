package com.awroby.auto.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	
	@RequestMapping(value="/HelloHome", method=RequestMethod.GET)
	public String HelloHome(){
		
		JSONObject json = new JSONObject();
		json.put("result", "success");
		json.put("message", "Hello");
		
		
		return json.toString();
	}
	    
}

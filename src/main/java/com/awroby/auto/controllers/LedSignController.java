package com.awroby.auto.controllers;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.awroby.auto.service.LedSignService;

@RestController
public class LedSignController {

	@Autowired public LedSignService ledService;
	private static final Logger logger = LoggerFactory.getLogger(LedSignController.class);

		
	@RequestMapping(value="/triggerLedMsg", method=RequestMethod.POST)
	public String TriggerMessage(@RequestBody byte[] request){
		JSONObject message = new JSONObject(new String(request));
		JSONObject resp = new JSONObject();
		
		String color = message.getString("color");
		String msg = message.getString("msg");
		String effect = message.getString("effect");
		
		logger.info("Triggering Message: " + message.toString());
		
		
		
		try {
			ledService.DisplayMessage(msg, color, effect);
			resp.put("result", "success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.put("result", "error");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.put("result", "error");
		}


		
		return resp.toString();
	}

}

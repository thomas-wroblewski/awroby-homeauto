package com.awroby.auto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SlackIntegrations {

	private final String USER_AGENT = "Mozilla/5.0";
	private static final Logger logger = LoggerFactory.getLogger(SlackIntegrations.class);
	
	@PostConstruct
	public void init(){
		try {
			sendWebHook("WebHook", ":rat:", "@thomas.wroblewski", "Starting Up");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void sendWebHook(String username, String emoji, String channel, String message) throws IOException{
		
		String webhook = "https://hooks.slack.com/services/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";
		
		JSONObject json = new JSONObject();
		json.put("text", message);
		json.put("username", username);
		json.put("channel", channel);
		json.put("icon_emoji", emoji);
		String urlParameters = "payload="+ json.toString();
		
		URL obj = new URL(webhook);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

				
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		logger.info("\nSending 'POST' request to URL : " + webhook);
		logger.info("Post parameters : " + urlParameters);
		logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
				
		//print result
		logger.info(response.toString());
	}

}

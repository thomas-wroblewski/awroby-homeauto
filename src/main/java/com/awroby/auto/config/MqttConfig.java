//package com.awroby.auto.config;
//
//import javax.annotation.PostConstruct;
//
//import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttSecurityException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.awroby.auto.mqtt.MqttConnectionListener;
//import com.awroby.auto.mqtt.MqttMessageHandler;
//
//@Configuration
//public class MqttConfig {
//
//	private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
//	@Autowired private MqttAsyncClient mqttAsyncClient;
//	
//	private String hostName = "localhost";
//	private String portNumber = "1883";
//
//	
//	
//	@PostConstruct
//	public void init() throws MqttSecurityException, MqttException {
//		
//		mqttAsyncClient.connect(null, new MqttConnectionListener(mqttAsyncClient));
//		mqttAsyncClient.setCallback(new MqttMessageHandler());
//		
//		
//	}
//	
//	
//
//	@Bean
//	public MqttAsyncClient mqttAsyncClient() throws MqttException{
//		return new MqttAsyncClient("tcp://" + hostName +":" + portNumber, MqttAsyncClient.generateClientId());
//	}
//
//		
//
//}
//

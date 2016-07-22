//package com.awroby.auto.mqtt;
//
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.MqttCallback;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.awroby.auto.dao.OutletRepository;
//import com.awroby.auto.objects.Outlet;
//import com.awroby.auto.service.RaspPiInterface;
//import com.awroby.auto.service.ScheduledTasks;
//
//public class MqttMessageHandler implements MqttCallback {
//
//	private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);
//	@Autowired private ScheduledTasks schedule;
//	@Autowired private OutletRepository outletRepo;
//	@Autowired private RaspPiInterface commands;
//	@Autowired private ScheduledTasks tasks;
//
//	
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//    	logger.info("Complete");
//    }
// 
//	@Override
//    public void connectionLost(Throwable cause) {
//
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        logger.info("Received message on topic: " + topic + " -> " + new String(message.getPayload()));
//        
//        String[] split = topic.split("/");
//        
//        
//        if(split.length == 3){
//        	if(split[2].matches("hbt")){
//            	schedule.autoTurnOn();
//        		schedule.setLastCheckIn(System.currentTimeMillis());
//            
//            }else if(split[2].matches("io")){
//            	handleIo(message);
//            }
//        }else if(split.length == 4){
//        	
//        	if(split[2].matches("rfoutlet")){      	
//        		
//        		if(split[3].matches("flip")){
//                
//        			handleRfOutlet(message);
//        		}
//        		
//            }
//        }
//    }
//    
//    private void handleIo(MqttMessage message){
//    	
//	    try{
//	    	JSONObject json = new JSONObject(new String(message.getPayload()));
//	    	String request = json.getString("request");
//	    	
//	    	if(request.matches("led")){
//	    		String led = json.getString("ledColor");		    	
//		    	if(led.matches("red")){
//					Thread t = new Thread(new Runnable(){
//		
//						@Override
//						public void run() {
//		
//							logger.info("Toggling Red LED");
//							commands.toggleLED("ledRed");
//						}
//					});
//					t.start();
//				}else if(led.matches("green")){
//					
//					logger.info("Toggling Green LED");
//					commands.toggleLED("ledGreen");
//			
//			
//			
//				}else if(led.matches("blue")){
//					Thread t = new Thread(new Runnable(){
//		
//						@Override
//						public void run() {
//		
//							logger.info("Toggling Blue LED");
//							commands.toggleLED("ledBlue");		
//						}
//					});
//					t.start();
//				}
//	    	}else if(request.matches("pwm")){
//	    		Thread t = new Thread(new Runnable(){
//					@Override
//					public void run() {
//						
//						logger.info("Trigger PWM");
//						commands.triggerPWM();
//					}
//				});
//				t.start();
//	    	}else{
//	    		
//	    	}
//	    	
//	    }catch(Exception ex){
//	    	ex.printStackTrace();
//	    }
//    }
//
//  
//    
//	private void handleRfOutlet(MqttMessage message) {
//		try{
//
//			JSONObject outlet = new JSONObject(new String(message.getPayload()));
//			
//			Outlet o = outletRepo.findById(outlet.getString("id"));
//			
//			int code = outlet.getInt("code");
//			int pulse = outlet.getInt("pulse");
//			
//			
//			if(code == o.getOnCode()){
//				logger.info("Turning on " + o.getName());
//				commands.turnOnOutlet(code, pulse);
//				o.setRunning(true);
//				tasks.updateUsedTime(o.getId());
//			}else{
//				logger.info("Turning off " + o.getName());
//				commands.turnOffOutlet(code, pulse);
//				tasks.removeEntry(o.getId());
//				o.setRunning(false);
//			}
//
//			outletRepo.save(o);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
//}

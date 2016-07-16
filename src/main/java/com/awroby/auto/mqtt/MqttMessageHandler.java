//package com.awroby.auto.mqtt;
//
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttCallback;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.awroby.auto.config.MqttConfig;
//import com.awroby.auto.dao.OutletRepository;
//import com.awroby.auto.objects.Outlet;
//import com.awroby.auto.service.RaspPiInterface;
//import com.awroby.auto.service.ScheduledTasks;
//
//public class MqttMessageHandler implements MqttCallback {
//
//	private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);
//	@Autowired ScheduledTasks schedule;
//	@Autowired private OutletRepository outletRepo;
//	@Autowired private RaspPiInterface commands;
//	@Autowired private ScheduledTasks tasks;
//	
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
//        if(topic.matches("/raspi1/hbt")){
//        	schedule.autoTurnOn();
//    		schedule.setLastCheckIn(System.currentTimeMillis());
//        }else if(topic.matches("/raspi1/rfoutlet/flip")){
//        	
//    		handleRfOutlet(message);
//        }
//    }
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//
//    }
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
//
//
//
//
//}

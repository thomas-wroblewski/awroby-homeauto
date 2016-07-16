//package com.awroby.auto.mqtt;
//
//import org.eclipse.paho.client.mqttv3.IMqttActionListener;
//import org.eclipse.paho.client.mqttv3.IMqttToken;
//import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.awroby.auto.config.MqttConfig;
//
//public class MqttConnectionListener implements IMqttActionListener {
//	private static final Logger logger = LoggerFactory.getLogger(MqttConnectionListener.class);
//	private  MqttAsyncClient mqttAsyncClient;
//	
//	public MqttConnectionListener(MqttAsyncClient mqttAsyncClient){
//		this.mqttAsyncClient = mqttAsyncClient;
//	}
//
//	 @Override
//     public void onSuccess(IMqttToken asyncActionToken) {
//         try {
//         	mqttAsyncClient.subscribe("/raspi1/sensor/test", 0, null, new IMqttActionListener() {
//                 @Override
//                 public void onSuccess(IMqttToken asyncActionToken) {
//                 	logger.info("Subscribed to /raspi1/sensor/test");
//                 }
//
//                 @Override
//                 public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                 }
//             });
//         	mqttAsyncClient.subscribe("/raspi1/hbt", 0, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                	logger.info("Subscribed to /raspi1/hbt");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                }
//         	});
//         	
//         	mqttAsyncClient.subscribe("/raspi1/rfoutlet/flip", 0, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                	logger.info("Subscribed to /raspi1/rfoutlet/flip");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                }
//         	});
//         	
//         	
//
//         } catch (MqttException e) {
//             e.printStackTrace();
//         }
//     }
//
//	@Override
//	public void onFailure(IMqttToken arg0, Throwable arg1) {
//		// TODO Auto-generated method stub
//			
//	}
//}

package com.electra.util;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public final class PushNotification {

	private PushNotification() {
	}
	private static final String APP_KEY = "AIzaSyBEbdcF-V-SlwWVM5v_bn3BYLPb6jQesaE";
	private static PushNotification pushNotifications = new PushNotification();
	public static PushNotification getPushNotificationInstance() {
		return pushNotifications;

	}

	public boolean pushNotification(String message,String deviceId) {
		
		try {
            // Register the sender
            Sender sender = new Sender(APP_KEY);
            // prepate the message
            Message gcmMessage = new Message.Builder().timeToLive(30)
                .delayWhileIdle(true).addData("message", message).build();
            // send the message to the GCM server.
            Result result = sender.send(gcmMessage, deviceId, 1); 
            System.out.println("push status " + result.toString());
            
		} catch (IOException ex) {
			System.out.println("Error occured while sending notification : "+ex.getMessage());
			ex.printStackTrace();
		}
		return true;

	}
}

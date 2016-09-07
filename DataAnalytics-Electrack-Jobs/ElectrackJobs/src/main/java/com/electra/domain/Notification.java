package com.electra.domain;



public class Notification {
	private String deviceToken;
	private String message;
	public Notification(String deviceToken, String message) {
		super();
		this.deviceToken = deviceToken;
		this.message = message;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public String getMessage() {
		return message;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}

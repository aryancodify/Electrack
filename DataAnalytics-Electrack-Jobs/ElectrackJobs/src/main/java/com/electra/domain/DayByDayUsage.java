package com.electra.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="usage")
public class DayByDayUsage {
	@Id
	private String userId;
	private double consumptionThisMonth;
	private double loadLimit;
	private String deviceId;
	public DayByDayUsage(String userId, double consumptionThisMonth,
			double loadLimit,String deviceId) {
		super();
		this.userId = userId;
		this.consumptionThisMonth = consumptionThisMonth;
		this.loadLimit = loadLimit;
		this.deviceId = deviceId;
	}
	public String getUserId() {
		return userId;
	}
	public double getConsumptionThisMonth() {
		return consumptionThisMonth;
	}
	public double getLoadLimit() {
		return loadLimit;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setConsumptionThisMonth(double consumptionThisMonth) {
		this.consumptionThisMonth = consumptionThisMonth;
	}
	public void setLoadLimit(double loadLimit) {
		this.loadLimit = loadLimit;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}

/**
 * 
 */
package com.electra.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sahu
 *
 */
@Document(collection = "prediction")
public class PredictionBean {

	

	@Id
	private String userId;
	private double p1;
	private double p2;
	private double p3;
	private double currentMonth;
	private double nextMonth;
	private double nextToMonth;
	private int startMonth;
	private String start;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getP1() {
		return p1;
	}
	public void setP1(double p1) {
		this.p1 = (double) Math.round(p1 * 100) / 100;
	}
	public double getP2() {
		return p2;
	}
	public void setP2(double p2) {
		this.p2 = (double) Math.round(p2 * 100) / 100;
	}
	public double getP3() {
		return p3;
	}
	public void setP3(double p3) {
		this.p3 = (double) Math.round(p3 * 100) / 100;
	}
	public double getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(double currentMonth) {
		this.currentMonth = (double) Math.round(currentMonth * 100) / 100;
	}
	public double getNextMonth() {
		return nextMonth;
	}
	public void setNextMonth(double nextMonth) {
		this.nextMonth = (double) Math.round(nextMonth * 100) / 100;
	}
	public double getNextToMonth() {
		return nextToMonth;
	}
	public void setNextToMonth(double nextToMonth) {
		this.nextToMonth = (double) Math.round(nextToMonth * 100) / 100;
	}
	public int getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}

	}

/**
 * 
 */
package com.electra.domain;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sahu
 *
 */
@Document(collection = "userBill")
public class UserBillBean {
	private String id;
	private String userId;
	private double totalAmount;
	private boolean paid;
	private LocalDate dueDate;
	private LocalDate expiryDate;
	private double totalUnit;
	private LocalDate timestamp;
	private double mainUnit;
	
	public UserBillBean() {
		// Auto-generated constructor stub
	}

	/**
	 * @param userId
	 * @param totalAmount
	 * @param paid
	 * @param dueDate
	 * @param expiryDate
	 * @param totalUnit
	 */
	public UserBillBean(String userId, double totalAmount, boolean paid, double totalUnit) {
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.paid = paid;
		this.dueDate = LocalDate.now().plusDays(16);;
		this.expiryDate = LocalDate.now().plusDays(21);
		this.timestamp = LocalDate.now().plusDays(1);
		this.totalUnit = totalUnit;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the paid
	 */
	public boolean isPaid() {
		return paid;
	}

	/**
	 * @param paid
	 *            the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	/**
	 * @return the totalUnit
	 */
	public double getTotalUnit() {
		return totalUnit;
	}

	/**
	 * @param totalUnit the totalUnit to set
	 */
	public void setTotalUnit(double totalUnit) {
		this.totalUnit = totalUnit;
	}

	/**
	 * @return the dueDate
	 */
	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the expiryDate
	 */
	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the timestamp
	 */
	public LocalDate getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the mainUnit
	 */
	public double getMainUnit() {
		return mainUnit;
	}

	/**
	 * @param mainUnit the mainUnit to set
	 */
	public void setMainUnit(double mainUnit) {
		this.mainUnit = mainUnit;
	}
}
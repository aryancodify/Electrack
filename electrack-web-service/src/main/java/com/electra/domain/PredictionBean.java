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
	private String currentMonth;
	private String nextMonth;
	private String nextToMonth;

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
	 * @return the currentMonth
	 */
	public String getCurrentMonth() {
		return currentMonth;
	}

	/**
	 * @param currentMonth
	 *            the currentMonth to set
	 */
	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	/**
	 * @return the nextMonth
	 */
	public String getNextMonth() {
		return nextMonth;
	}

	/**
	 * @param nextMonth
	 *            the nextMonth to set
	 */
	public void setNextMonth(String nextMonth) {
		this.nextMonth = nextMonth;
	}

	/**
	 * @return the nextToMonth
	 */
	public String getNextToMonth() {
		return nextToMonth;
	}

	/**
	 * @param nextToMonth
	 *            the nextToMonth to set
	 */
	public void setNextToMonth(String nextToMonth) {
		this.nextToMonth = nextToMonth;
	}
}

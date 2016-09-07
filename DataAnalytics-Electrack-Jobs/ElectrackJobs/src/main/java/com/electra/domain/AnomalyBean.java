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
@Document(collection = "anomaly")
public class AnomalyBean {

	@Id
	private String userId;
	private String address;
	private String status;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public AnomalyBean(){
		
	}

	public AnomalyBean(String userId, String address) {
		this.userId = userId;
		this.address = address;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

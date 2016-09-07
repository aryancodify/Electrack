package com.electra.domain;

import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <strong>UserBean</strong><br>
 * UserBean Class used to store and populate userDetail.
 * 
 * @author sahu
 *
 */
@Document(collection = "user")
@JsonInclude(Include.NON_NULL)
public class UserBean {
	@Id
	private String userId;

	@NotBlank(message = "name is blank")
	private String name;

	@NotBlank(message = "address is blank")
	private String address;

	@NotBlank(message = "city is blank")
	private String city;

	@NotBlank(message = "state is blank")
	private String state;

	@NotBlank(message = "type is blank")
	private String type;

	@NotBlank(message = "deveiceId is blank")
	private String deveiceId;

	@JsonIgnore
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date timestamp= new Date();
	
	@JsonIgnore
	private String password = "electra1234";

	private double billCycleDate;
	private double loadLimit;
	
	@Valid
	private Location location;

	private String totalUnits;
	
	public UserBean() {
		// default constructor
	}
	
	/**
	 * @param userId
	 * @param name
	 * @param address
	 * @param city
	 * @param state
	 * @param type
	 * @param deveiceId
	 * @param location
	 */
	public UserBean(String userId, String name, String address, String city, String state, String type,
			String deveiceId, Location location) {
		super();
		this.userId = userId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.type = type;
		this.deveiceId = deveiceId;
		this.location = location;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the deveiceId
	 */
	public String getDeveiceId() {
		return deveiceId;
	}

	/**
	 * @param deveiceId
	 *            the deveiceId to set
	 */
	public void setDeveiceId(String deveiceId) {
		this.deveiceId = deveiceId;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return userId ;
	}

	/**
	 * @return the billCycleDate
	 */
	public double getBillCycleDate() {
		return billCycleDate;
	}

	/**
	 * @param billCycleDate the billCycleDate to set
	 */
	public void setBillCycleDate(double billCycleDate) {
		this.billCycleDate = billCycleDate;
	}

	/**
	 * @return the loadLimit
	 */
	public double getLoadLimit() {
		return loadLimit;
	}

	/**
	 * @param loadLimit the loadLimit to set
	 */
	public void setLoadLimit(double loadLimit) {
		this.loadLimit = loadLimit;
	}

	/**
	 * @return the totalUnits
	 */
	public String getTotalUnits() {
		return totalUnits;
	}

	/**
	 * @param totalUnits the totalUnits to set
	 */
	public void setTotalUnits(String totalUnits) {
		this.totalUnits = totalUnits;
	}
}

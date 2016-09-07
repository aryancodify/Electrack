package com.electra.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <strong>UserBean</strong><br>
 * UserBean Class used to store and populate userDetail.
 * 
 * @author sahu
 *
 */
@Document(collection = "user")
public class UserBean {
	@Id
	private String userId;

	private String name;

	private String address;
	
	private String city;

	private String state;

	private String type;
	
	private String deveiceId;

	private int billCycleDate;
	@JsonIgnore
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date timestamp= new Date();
	
	private double loadLimit;
	
	@JsonIgnore
	private String password = "electra1234";

	private double latitude;
	private double longitude;
	
	private Location location;

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

	public int getBillCycleDate() {
		return billCycleDate;
	}

	public void setBillCycleDate(int billCycleDate) {
		this.billCycleDate = billCycleDate;
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
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLoadLimit() {
		return loadLimit;
	}

	public void setLoadLimit(double loadLimit) {
		this.loadLimit = loadLimit;
	}
}

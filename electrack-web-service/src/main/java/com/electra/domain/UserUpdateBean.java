package com.electra.domain;

import javax.validation.Valid;

/**
 * <strong>UserBean</strong><br>
 * UserBean Class used to store and populate userDetail.
 * 
 * @author sahu
 *
 */
public class UserUpdateBean {

	private String name;

	private String address;

	private String city;

	private String state;

	private String deveiceId;

	private String password;

	private double loadLimit;
	
	@Valid
	private Location location;

	public UserUpdateBean() {
		// default constructor
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
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
	 * @param address the address to set
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
	 * @param city the city to set
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
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the deveiceId
	 */
	public String getDeveiceId() {
		return deveiceId;
	}

	/**
	 * @param deveiceId the deveiceId to set
	 */
	public void setDeveiceId(String deveiceId) {
		this.deveiceId = deveiceId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
}
package com.electra.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Location {

	@JsonIgnore
	private String type = "Point";

	private List<Double> coordinates;
	
	public Location() {
		// constructor stub
	}
	
	/**
	 * @param coordinates
	 */
	public Location(List<Double> coordinates) {
		this.coordinates = coordinates;
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
	 * @return the coordinates
	 */
	public List<Double> getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

}

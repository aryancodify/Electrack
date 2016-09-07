package com.electra.domain;

import java.util.Comparator;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * <strong>UnitBean</strong><br>
 * UnitsBean Class is used to store unit and time
 * 
 * @author sahu
 *
 */
public class UnitBean{

	
	private double unit;
	@DateTimeFormat(iso = ISO.TIME)
	private Date time;

	public UnitBean() {
		// default constructor
	}

	public UnitBean(double unit) {
		this.unit = unit;
		time = new Date();
	}

	/**
	 * @return the unit
	 */
	public double getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(double unit) {
		this.unit = unit;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	public static Comparator<UnitBean> COMPARE_TIME = new Comparator<UnitBean>() {
		  @Override
		  public int compare(UnitBean one, UnitBean other) {
		   return one.time.compareTo(other.time);
		  }
		 };
}

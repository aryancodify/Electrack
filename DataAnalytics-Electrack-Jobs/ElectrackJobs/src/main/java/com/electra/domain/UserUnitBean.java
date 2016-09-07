/**
 * 
 */
package com.electra.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <strong>UserUnitBean</strong><br>
 * UserUnitBean Class is used to store unit and time
 * 
 * @author sahu
 *
 */
/**
 * @author sahu
 *
 */
@Document(collection = "userUnit")
@CompoundIndexes(value = { @CompoundIndex(name = "user_units _ind", def = "{'userId': 1, 'date': 1}", unique = true) })
public class UserUnitBean {

	@Id
	private String id;
	private String userId;
	private List<UnitBean> units;
	
	private LocalDate date;

	public UserUnitBean() {
		// default constructor
	}

	@SuppressWarnings("serial")
	public UserUnitBean(String userId, double unit) {
		this.userId = userId;
		this.units = new ArrayList<UnitBean>() {
			{
				add(new UnitBean(unit));
			}
		};
		date = LocalDateTime.now().toLocalDate();
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
	 * @return the units
	 */
	public List<UnitBean> getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(List<UnitBean> units) {
		this.units = units;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
}

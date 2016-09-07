package com.electra.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.electra.domain.UnitBean;
import com.electra.domain.UserUnitBean;

/**
 * @author sahu
 *
 */
public interface UnitsRepositoryCustom {

	void insertUnit(LocalDate date, UnitBean unitBean);
	 public UserUnitBean getCurrentMonthUnit(String userId, Date date);
	 List<UserUnitBean> getMonthUnit(String userId, LocalDate from , LocalDate to);
}

package com.electra.repository;

import java.util.List;

import org.springframework.data.geo.GeoResults;

import com.electra.domain.UserBean;

/**
 * @author sahu
 *
 */
public interface UserRepositoryCustom {

	GeoResults getnearby(double longitude, double latitude);
	
	List<UserBean> getTodayCycleUser(int date);
}

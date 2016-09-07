package com.electra.repository;

import org.springframework.data.geo.GeoResults;

/**
 * @author sahu
 *
 */
public interface UserRepositoryCustom {

	GeoResults getnearby(double longitude, double latitude);
	
}

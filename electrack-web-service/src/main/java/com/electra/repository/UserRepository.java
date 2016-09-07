/**
 * 
 */
package com.electra.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.electra.domain.UserBean;

public interface UserRepository extends MongoRepository<UserBean, String>, UserRepositoryCustom {

	@Query(value = "{ '_id': ?0 , 'password': ?1 }")
	UserBean userDetail(String userId, String password);

	@Query(value = "{ 'state': ?0 }", fields = "{ _id : 1}")
	List<UserBean> userOfState(String state);
	
	//@Query(value = "{ location: { $nearSphere: { $geometry: { type: 'Point', coordinates: [ ?0, ?1 ] }, $maxDistance: 500 } } }")
	@Query(value = "{ location: { $geoIntersects: { $geometry: { type: 'Point', coordinates: [ ?0, ?1 ] } } } }")
	List<UserBean> userNearMe(double lat, double lon);
	
 	
	//List<UserBean> findByPositionNear(Point p, Distance d);
}

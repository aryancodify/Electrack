/**
 * 
 */
package com.electra.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.electra.domain.UserUnitBean;

public interface UnitsRepository extends MongoRepository<UserUnitBean, String>, UnitsRepositoryCustom {
 
	@Query(value = "{ 'userId': ?0 , 'date': ?1 }")
	UserUnitBean findUserUnits(String userId, Date date);
	
	@Query(value = "{ 'userId': ?0 , 'date': {$gte: ?1, $lte : ?2 } }")
	List<UserUnitBean> findUnitsBetween(String userId, Date from,Date to);
	
	@Query(value = "{ 'userId': { $in : ?0 } , 'date': {$gte: ?1, $lte : ?2 } }")
	List<UserUnitBean> findUnitsBetween(List<String> userIds, Date from,Date to);
	
	/*@Query(value = "{ 'userId': ?0 , 'date': {$lte : ?1 }} , {$sort: {'date':-1}}")
	UserUnitBean findLastUnits(String userId,Date date);
	
	@Query(value = "{ 'userId': ?0 , 'date': {$lte : ?1 }} , {$sort: {'date':-1}}")
	List<UserUnitBean> findLastUnits(String userId,Date date,Pageable pageable);*/
}

/**
 * 
 */
package com.electra.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.electra.domain.UserBillBean;

public interface BillRepository extends MongoRepository<UserBillBean, String> {

	@Query(value = "{ 'userId': ?0}")
	List<UserBillBean> userBillsDetail(String userId);
	
	@Query(value = "{  $query: {'userId': ?0},$orderby: { timestamp : -1 }}")
	List<UserBillBean> userBill(String userId);
	
	@Query(value = "{ 'userId': ?0 , timestamp : { $gte: ?1 , $lte : ?2} }")
	List<UserBillBean>  a(String userId,String from , String to);
	
	@Query(value = "{  $query: {'userId': ?0 } }, $orderby: { timestamp : 1 }}")
	UserBillBean userCurrentMonthBill(String userId);
}

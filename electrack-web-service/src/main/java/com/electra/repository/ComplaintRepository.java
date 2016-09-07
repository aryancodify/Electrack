/**
 * 
 */
package com.electra.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.electra.domain.ComplaintBean;

public interface ComplaintRepository extends MongoRepository<ComplaintBean, String> {

	@Query(value = "{ 'userId': ?0}")
	List<ComplaintBean> userAllComplaint(String userId);
	
	@Query(value = "{ 'userId': ?0, 'complaintId': ?1 }")
	ComplaintBean userComplaint(String userId,String complaintId);
	
}

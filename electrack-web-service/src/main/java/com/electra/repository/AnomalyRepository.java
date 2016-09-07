/**
 * 
 */
package com.electra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.electra.domain.AnomalyBean;

public interface AnomalyRepository extends MongoRepository<AnomalyBean, String> {

}

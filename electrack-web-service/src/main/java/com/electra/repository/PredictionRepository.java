/**
 * 
 */
package com.electra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.electra.domain.PredictionBean;

public interface PredictionRepository extends MongoRepository<PredictionBean, String> {

}

/**
 * 
 */
package com.electra.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.electra.domain.UnitBean;
import com.electra.domain.UserUnitBean;

/**
 * @author sahu
 *
 */
@Service
public class UnitsRepositoryImpl implements UnitsRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitsRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insertUnit(LocalDate date, UnitBean unitBean) {
		LOGGER.info("push new unit");
		Query query = new Query();
		query.addCriteria(Criteria.where("date").is(date));
		Update update = new Update();
		update.push("units", unitBean);
		mongoTemplate.updateFirst(query, update, UserUnitBean.class);
	}
	
	@Override
	 public UserUnitBean getCurrentMonthUnit(String userId, Date date) {
	  LOGGER.info("push new unit");
	  Query query = new Query();
	  query.addCriteria(Criteria.where("userId").is(userId));
	  query.addCriteria(Criteria.where("date").lte(date));
	  query.with(new Sort(Sort.Direction.DESC,"date"));
	  return mongoTemplate.findOne(query,UserUnitBean.class );
	 }
	
	@Override
	public List<UserUnitBean> getMonthUnit(String userId, LocalDate from , LocalDate to) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		query.addCriteria(Criteria.where("date").gte(from).lt(to));
		query.with(new Sort(Sort.Direction.ASC,"date"));
		return mongoTemplate.find(query,UserUnitBean.class );
	}
}

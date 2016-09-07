/**
 * 
 */
package com.electra.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.electra.domain.UserBean;

/**
 * @author sahu
 *
 */
@Service
public class UserRepositoryImpl implements UserRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public GeoResults getnearby(double longitude, double latitude) {
		Point point = new Point(longitude, latitude);
		NearQuery queryN = NearQuery.near(point).maxDistance(new Distance(500, Metrics.KILOMETERS)).num(20);
		GeoResults<UserBean> geoPoints = mongoTemplate.geoNear(queryN, UserBean.class);

		List<UserBean> beans = new ArrayList<>();

		if (!geoPoints.getContent().isEmpty()) {
			for (GeoResult<UserBean> geoResult : geoPoints) {
				UserBean userBean = new UserBean();
				userBean.setUserId(geoResult.getContent().getUserId());
				beans.add(userBean);
			}

		}
		Criteria criteria = new Criteria("location").near(new Point(longitude, latitude)).maxDistance(500);

		 List<UserBean> tracks = mongoTemplate.find(new Query(criteria),
				 UserBean.class);
		
		return geoPoints;
	}

	
}

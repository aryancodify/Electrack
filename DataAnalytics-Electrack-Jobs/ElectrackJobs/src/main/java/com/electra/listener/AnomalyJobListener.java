package com.electra.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.electra.domain.AnomalyBean;
import com.electra.domain.UserBean;
import com.electra.repository.UserRepository;
import com.electra.util.PushNotification;

public class AnomalyJobListener extends JobExecutionListenerSupport {
	
	private MongoTemplate mongoTemplate;
	private UserRepository userRepository;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		List<AnomalyBean> anomalies = new ArrayList<AnomalyBean>(0);
		anomalies = mongoTemplate.findAll(AnomalyBean.class);
		int numAnomalies = anomalies.size();
		List<UserBean> admins = new ArrayList<UserBean>(0);
		admins = userRepository.userByType("admin");
		if (numAnomalies > 0) {
			for (UserBean admin : admins) {
				pushNotification.pushNotification(numAnomalies
						+ " anomalies detected ! Keep an eye on that.",
						admin.getDeveiceId());
			}

		}
	}

	private PushNotification pushNotification = PushNotification
			.getPushNotificationInstance();

	@Autowired
	public AnomalyJobListener(MongoTemplate mongoTemplate,
			UserRepository userRepository) {
		this.mongoTemplate = mongoTemplate;
		this.userRepository = userRepository;
	}
}

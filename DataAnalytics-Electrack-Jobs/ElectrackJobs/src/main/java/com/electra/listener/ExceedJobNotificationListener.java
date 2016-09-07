package com.electra.listener;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.electra.domain.DayByDayUsage;
import com.electra.util.PushNotification;

public class ExceedJobNotificationListener extends JobExecutionListenerSupport{
	private PushNotification pushNotification = PushNotification.getPushNotificationInstance();
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public ExceedJobNotificationListener(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		List<DayByDayUsage> dayByUaByDayUsages = mongoTemplate.findAll(DayByDayUsage.class);
		for(DayByDayUsage byDayUsage:dayByUaByDayUsages){
			double consumptionThisMonth = byDayUsage.getConsumptionThisMonth();
			double loadLimit = byDayUsage.getLoadLimit();
			String message = "";
			if(consumptionThisMonth>loadLimit){
				message = "You have exceeded your limit by "+(consumptionThisMonth-loadLimit)+" units.";
			}else{
				message = "Your consumption till now is "+(consumptionThisMonth)+" units. You still have "
						+ (loadLimit - consumptionThisMonth) +" units to go.";
				
			}
			if(null != byDayUsage.getDeviceId()){
			pushNotification.pushNotification(message,byDayUsage.getDeviceId());
			
			}
		}
	}
}

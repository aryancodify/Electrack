package com.electra.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PredictionScheduler {
	private Job myImportJob;
	 private JobLauncher jobLauncher;

	    @Autowired
	    public PredictionScheduler(JobLauncher jobLauncher, @Qualifier("predictionJob") Job myImportJob){
	        this.myImportJob = myImportJob; 
	        this.jobLauncher = jobLauncher;
	   }

	   @Scheduled(cron="0 30 20 10 * ?")
	   public void runJob(){
	       try {
			jobLauncher.run(myImportJob, new JobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException
				| JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
}

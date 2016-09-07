package com.electra.batchConfig;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.electra.domain.AnomalyBean;
import com.electra.domain.DayByDayUsage;
import com.electra.domain.PredictionBean;
import com.electra.domain.UserBean;
import com.electra.listener.AnomalyJobListener;
import com.electra.listener.ExceedJobNotificationListener;
import com.electra.processor.AnomalyProcessor;
import com.electra.processor.ExceedUsageProcessor;
import com.electra.processor.PredictionProcessor;
import com.electra.repository.UnitsRepository;
import com.electra.repository.UserRepository;
import com.electra.service.ElectraService;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration{

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MongoTemplate mongoTemplate;

	@Autowired
	UnitsRepository unitsRepos;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ElectraService electraService;
	
    // tag::readerwriterprocessor[]
    @Bean
    @StepScope
    public MongoItemReader<UserBean> reader() {
    	MongoItemReader<UserBean> reader = new MongoItemReader<UserBean>();
    	reader.setTemplate(mongoTemplate);
    	reader.setCollection("user");
    	reader.setQuery("{}");
    	reader.setSort(new HashMap<String,Direction>(){{put("_id", Direction.ASC);}});
    	reader.setTargetType(UserBean.class);
        return reader;
    }

    @Bean
    public ExceedUsageProcessor processor() {
        return new ExceedUsageProcessor(unitsRepos,electraService);
    }
    
    @Bean
    public AnomalyProcessor anomalyProcessor() {
        return new AnomalyProcessor(unitsRepos);
    }
    
    @Bean
    public PredictionProcessor predictionProcessor() {
        return new PredictionProcessor(electraService);
    }
    @Bean
    @StepScope
    public MongoItemWriter<DayByDayUsage> writer() {
    	MongoItemWriter<DayByDayUsage> writer = new MongoItemWriter<DayByDayUsage>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("usage");
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::listener[]

    @Bean
    @StepScope
    public MongoItemWriter<AnomalyBean> anomalyWriter() {
    	MongoItemWriter<AnomalyBean> writer = new MongoItemWriter<AnomalyBean>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("anomaly");
        return writer;
    }
 
    @Bean
    @StepScope
    public MongoItemWriter<PredictionBean> predictionWriter() {
    	MongoItemWriter<PredictionBean> writer = new MongoItemWriter<PredictionBean>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("prediction");
        return writer;
    }
    @Bean
    public ExceedJobNotificationListener listener() {
        return new ExceedJobNotificationListener(mongoTemplate);
    }

    @Bean
    public AnomalyJobListener anomalyListener(){
    	return new AnomalyJobListener(mongoTemplate,userRepository);
    	
    }
    // end::listener[]

    // tag::jobstep[]
    @Bean
    public Job notifyUserJob() {
        return jobBuilderFactory.get("notifyUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }
 
    @Bean
    public Job anomalyJob() {
        return jobBuilderFactory.get("anomalyJob")
                .incrementer(new RunIdIncrementer())
                .listener(anomalyListener())
                .flow(step2())
                .end()
                .build();
    }
    
    @Bean
    public Job predictionJob() {
        return jobBuilderFactory.get("predictionJob")
                .incrementer(new RunIdIncrementer())
                .flow(step3())
                .end()
                .build();
    }
    
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<UserBean, DayByDayUsage> chunk(50)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
				.throttleLimit(10)
				.allowStartIfComplete(true)
                .build();
    }
    // end::jobstep[]
    
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<UserBean, AnomalyBean> chunk(50)
                .reader(reader())
                .processor(anomalyProcessor())
                .writer(anomalyWriter())
                .taskExecutor(taskExecutor())
				.throttleLimit(10)
				.allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .<UserBean, PredictionBean> chunk(50)
                .reader(reader())
                .processor(predictionProcessor())
                .writer(predictionWriter())
				.allowStartIfComplete(true)
                .build();
    }
	@Bean
	public TaskExecutor taskExecutor() {
		// TODO Auto-generated method stub
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}
	
	@Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new org.hsqldb.jdbcDriver());
        dataSource.setUrl("jdbc:hsqldb:mem:testdb;sql.enforce_strict_size=true;hsqldb.tx=mvcc");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
	
}

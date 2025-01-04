package com.urbanShows.userService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.urbanShows.userService.testingData.InsertEventDataSample;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan("com.urbanShows.userService.entity")
@EnableJpaRepositories("com.urbanShows.userService.repository")
@EnableScheduling
@Slf4j
public class UserServiceApplication {

	private final Environment environment;
	private final InsertEventDataSample insertEventDataSample;

	UserServiceApplication(Environment environment, InsertEventDataSample insertEventDataSample) {
		this.environment = environment;
		this.insertEventDataSample = insertEventDataSample;
	}

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;
	
	@Value("${event.service.url}")
	private String eventServiceUrl;
	
	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String kafkaConsumerServer;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		log.info("User Service is started on port: {}, swagger URL: {}" , port , swaggerPath);
		log.info("Event Service URL: {}, Kafka Consumer URL: {}" , eventServiceUrl , kafkaConsumerServer);
		final String[] activeProfiles = environment.getActiveProfiles();
		if (activeProfiles.length == 0) {
			log.info("No active profile is set.");
		} else {
			log.info("Active profiles: ");
			for (String profile : activeProfiles) {
				log.info(profile);
			}
		}
		addInitialData();
	}
 
	private void addInitialData() {
//		insertEventDataSample.insertUserSampleDate();		
	}

}

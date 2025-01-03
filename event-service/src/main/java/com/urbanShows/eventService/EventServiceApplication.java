package com.urbanShows.eventService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.urbanShows.eventService.testingData.InsertEventDataSample;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan("com.urbanShows.eventService.entity")
@EnableJpaRepositories("com.urbanShows.eventService.repository")
@Slf4j
public class EventServiceApplication {

	private final Environment environment;
	private final InsertEventDataSample insertEventDataSample;

	EventServiceApplication(Environment environment, InsertEventDataSample insertEventDataSample) {
		this.environment = environment;
		this.insertEventDataSample = insertEventDataSample;
	}

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String kafkaConsumerServer;

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		log.info("Event Service is started on port: {}, swagger URL: {}", port, swaggerPath);
		log.info("User Service URL: {}, Kafka Consumer URL: {}", userServiceUrl, kafkaConsumerServer);
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
//		insertEventDataSample.insertEventTypeSampleDate();
//		insertEventDataSample.insertEventSampleDate();
		
	}

}
